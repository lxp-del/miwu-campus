<template>
	<view class="chat-list-page">
		<view class="header">
			<text class="title">消息</text>
			<view class="header-right">
				<text class="action-text">清除未读</text>
			</view>
		</view>

		<scroll-view class="list-container" scroll-y enhanced :show-scrollbar="false">
			<view v-if="threadList.length === 0" class="empty-state">
				<image class="empty-img" src="/static/empty-msg.png" mode="aspectFit"></image>
				<text class="empty-text">暂无消息，去逛逛吧</text>
			</view>

			<view class="thread-item" v-for="item in threadList" :key="item.thread_id" @click="goToChat(item)"
				hover-class="item-hover" :hover-stay-time="50">
				<view class="avatar-wrap">
					<image class="avatar" :src="item.other_avatar || '/static/default-avatar.png'" mode="aspectFill">
					</image>
					<view class="badge" v-if="item.display_unread > 0">
						{{ item.display_unread > 99 ? '99+' : item.display_unread }}
					</view>
				</view>

				<view class="content-wrap">
					<view class="top-row">
						<text class="nickname">{{ item.other_nickname }}</text>
						<text class="time">{{ formatTime(item.last_message_time) }}</text>
					</view>
					<view class="bottom-row">
						<text class="last-msg">
							{{
					            item.last_message 
					            ? (item.last_message.includes('imageUrls') ? '[图片内容]' : item.last_message) 
					            : '暂无消息' 
					        }}
						</text>
					</view>
				</view>
			</view>

			<view class="safe-area-inset-bottom"></view>
		</scroll-view>
	</view>
</template>
<script setup>
	import {
		ref,
		computed,
		onUnmounted
	} from 'vue'
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app'
	import {
		getThreadList,
		markAsRead
	} from "@/apis/chat/chat.js"

	// 数据定义
	const threadList = ref([])
	let socketTask = null
	let reconnectTimer = null

	const currentUser = ref(uni.getStorageSync('currentUser') || {
		id: null
	})
	const currentUserId = computed(() => currentUser.value.id)

	// 初始化 WebSocket
	const initWebSocket = () => {
		if (!currentUserId.value) return

		// 避免重复连接
		if (socketTask) {
			socketTask.close()
		}

		const wsUrl = `ws://localhost:9090/ws/chat/${currentUserId.value}`
		socketTask = uni.connectSocket({
			url: wsUrl,
			complete: () => {}
		})

		socketTask.onOpen(() => {
			console.log('列表页 WebSocket 已连接')
			if (reconnectTimer) clearInterval(reconnectTimer)
		})

		socketTask.onMessage((res) => {
			try {
				const data = JSON.parse(res.data)
				// 兼容处理：无论是 list_update 还是具体的 chat 消息，都触发列表刷新
				if (data.type === 'list_update' || data.type === 'chat') {
					loadThreadList()
				}
			} catch (e) {
				console.error('消息解析错误', e)
			}
		})

		// 异常关闭自动重连
		socketTask.onClose(() => {
			console.log('WebSocket 已断开，准备重连...')
			startReconnect()
		})
	}

	const startReconnect = () => {
		if (reconnectTimer) return
		reconnectTimer = setInterval(() => {
			initWebSocket()
		}, 5000) // 每5秒尝试重连一次
	}

	const loadThreadList = async () => {
		if (!currentUserId.value) return
		try {
			const res = await getThreadList(currentUserId.value)
			if (res && res.code === 200) {
				threadList.value = res.data.map(thread => {
					// 统一字段名，防止后端字段不一致导致的渲染失败
					let unread = 0
					if (thread.participant_a_id == currentUserId.value) {
						unread = thread.participant_a_unread_count || 0
					} else {
						unread = thread.participant_b_unread_count || 0
					}
					return {
						...thread,
						thread_id: thread.id,
						display_unread: unread,
						// 确保即使没有最后一条消息，也不会显示 null
						last_message: thread.last_message || '暂无消息'
					}
				})
			}
		} catch (err) {
			console.error('列表加载异常', err)
		}
	}

	const goToChat = (item) => {
		// 体验优化：前端先瞬间清空红点
		const target = threadList.value.find(t => t.thread_id === item.thread_id)
		if (target) target.display_unread = 0

		// 异步调用已读接口，不阻塞跳转
		markAsRead({
			threadId: item.thread_id,
			userId: currentUserId.value
		})

		uni.navigateTo({
			url: `/pages/chat/detail/index?threadId=${item.thread_id}&targetId=${item.other_user_id}&goodId=${item.good_id}&targetName=${encodeURIComponent(item.other_nickname)}`
		})
	}

	// 格式化时间函数优化
	const formatTime = (timeStr) => {
		if (!timeStr) return ''
		const date = new Date(timeStr)
		const now = new Date()

		// 如果是今天，显示 12:30
		if (date.toDateString() === now.toDateString()) {
			return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
		}
		// 如果是昨天
		const yesterday = new Date(now - 86400000)
		if (date.toDateString() === yesterday.toDateString()) {
			return '昨天'
		}
		// 今年内
		if (date.getFullYear() === now.getFullYear()) {
			return `${date.getMonth() + 1}月${date.getDate()}日`
		}
		return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
	}

	onLoad(() => {
		initWebSocket()
	})

	onShow(() => {
		loadThreadList()
	})

	onUnmounted(() => {
		if (socketTask) socketTask.close()
		if (reconnectTimer) clearInterval(reconnectTimer)
	})
</script>

<style lang="scss" scoped>
	.chat-list-page {
		height: 100vh;
		background-color: #f7f8fa; // 淘宝背景色通常略带灰色
		display: flex;
		flex-direction: column;
	}

	.header {
		padding: 20rpx 32rpx;
		padding-top: calc(var(--status-bar-height) + 20rpx);
		background-color: #fff;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.title {
			font-size: 36rpx;
			font-weight: 600;
			color: #111;
		}

		.action-text {
			font-size: 26rpx;
			color: #666;
		}
	}

	.list-container {
		flex: 1;
		overflow: hidden;
	}

	.thread-item {
		display: flex;
		align-items: center;
		padding: 24rpx 32rpx;
		background-color: #fff;
		position: relative;
		transition: background-color 0.2s;

		// 底部细线条
		&::after {
			content: "";
			position: absolute;
			bottom: 0;
			right: 0;
			width: 80%; // 线条不通到底，显得更高级
			height: 1rpx;
			background-color: #f0f0f0;
		}

		&:last-child::after {
			display: none;
		}
	}

	// 点击态
	.item-hover {
		background-color: #f2f2f2;
	}

	.avatar-wrap {
		position: relative;
		flex-shrink: 0;
		margin-right: 24rpx;

		.avatar {
			width: 104rpx;
			height: 104rpx;
			border-radius: 16rpx; // 淘宝现在多用圆角矩形，而非纯圆
			background-color: #f0f0f0;
			border: 1rpx solid rgba(0, 0, 0, 0.05);
		}

		.badge {
			position: absolute;
			top: -12rpx;
			right: -12rpx;
			background-color: #ff4d4f;
			color: #fff;
			font-size: 20rpx;
			font-weight: bold;
			height: 32rpx;
			line-height: 32rpx;
			padding: 0 10rpx;
			border-radius: 16rpx;
			min-width: 32rpx;
			text-align: center;
			border: 2rpx solid #fff; // 增加白边，突出红点
			box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
		}
	}

	.content-wrap {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		height: 88rpx; // 固定高度使对齐更统一
		overflow: hidden;

		.top-row {
			display: flex;
			justify-content: space-between;
			align-items: center;

			.nickname {
				font-size: 30rpx;
				font-weight: 500;
				color: #333;
				max-width: 360rpx;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}

			.time {
				font-size: 22rpx;
				color: #bbbbbb;
			}
		}

		.bottom-row {
			margin-top: 4rpx;

			.last-msg {
				font-size: 26rpx;
				color: #999;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				display: block;
			}
		}
	}

	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 200rpx;

		.empty-img {
			width: 240rpx;
			height: 240rpx;
			margin-bottom: 20rpx;
			opacity: 0.6;
		}

		.empty-text {
			font-size: 28rpx;
			color: #999;
		}
	}

	.safe-area-inset-bottom {
		height: constant(safe-area-inset-bottom);
		height: env(safe-area-inset-bottom);
	}
</style>