<template>
	<view class="chat-detail-page">
		<view class="chat-header">
			<view class="header-left">
				<view class="back-btn" @click="goBack">
					<text class="iconfont">←</text>
				</view>
				<view class="user-info">
					<text class="target-name">{{ targetUserName }}</text>
					<view class="credit-tag">
						<text>信誉分: 98</text>
					</view>
				</view>
			</view>
			<view class="header-right">
				<text class="more-icon">···</text>
			</view>
		</view>

		<scroll-view class="chat-content" scroll-y :scroll-into-view="scrollToView" scroll-with-animation>
			<view class="message-list">
				<view class="loading-tip">
					<text>—— 聊天开始啦 ——</text>
				</view>

				<view v-for="item in messageList" :key="item.id" :id="'msg-' + item.id" class="message-item"
					:class="{ self: item.senderId == currentUser.id }">
					<image class="avatar" :src="item.senderId == currentUser.id ? currentUser.avatar : targetUserAvatar"
						mode="aspectFill"></image>

					<view class="message-bubble">
						<view v-if="isGoodsMsg(item.content)" class="goods-msg-card">
							<image :src="parseGoods(item.content).img" mode="aspectFill" class="msg-goods-img"></image>
							<view class="msg-goods-info">
								<text class="msg-goods-title">{{ parseGoods(item.content).title }}</text>
								<text class="msg-goods-price">￥{{ parseGoods(item.content).price }}</text>
							</view>
						</view>

						<block v-else-if="isImageMsg(item.content)">
							<image :src="item.content" mode="widthFix" class="content-img"
								@click="previewImg(item.content)"></image>
						</block>

						<block v-else>
							<text class="text">{{ item.content }}</text>
						</block>
					</view>
				</view>
				<view style="height: 20rpx;"></view>
			</view>
		</scroll-view>

		<view class="input-area">
			<input class="input-box" type="text" v-model="inputMessage" placeholder="想跟TA说点什么..." confirm-type="send"
				@confirm="handleSend" />
			<view class="send-btn" :style="{backgroundColor: inputMessage.trim() ? '#D4FB1B' : '#eeeeee'}"
				@click="handleSend">
				<text :style="{color: inputMessage.trim() ? '#000' : '#999'}">发送</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		nextTick,
		onUnmounted,
		computed
	} from 'vue'
	import {
		onLoad,
		onUnload,
		onHide
	} from '@dcloudio/uni-app'
	import {
		getUserById
	} from "@/apis/user/userinfo.js"
	import {
		getHistoyByThreadId,
		getThreadList,
		markAsRead
	} from "@/apis/chat/chat.js"



	// --- 1. 数据定义 ---
	const currentUser = ref(uni.getStorageSync('currentUser') || {
		id: null,
		avatar: '',
		name: ''
	})
	const targetUserId = ref()
	const targetUserName = ref('')
	const targetUserAvatar = ref()
	const messageList = ref([])
	const inputMessage = ref('')
	const scrollToView = ref('')
	const threadId = ref()
	let socketTask = null
	const goodId = ref(0)

	const productInfo = ref({
		imageUrl: '',
		price: '0.00',
		title: ''
	})

	// --- 2. 商品解析逻辑 (新增) ---
	const isGoodsMsg = (content) => {
		if (!content) return false;
		// 判断是否包含商品的关键 JSON 字段
		return content.includes('imageUrls') && content.includes('price');
	}

	const parseGoods = (content) => {
		try {
			const data = JSON.parse(content);
			return {
				img: data.imageUrls ? data.imageUrls.split(',')[0] : '',
				title: data.title || '商品信息',
				price: data.price || '0.00'
			}
		} catch (e) {
			return {
				img: '',
				title: '解析失败',
				price: '0'
			};
		}
	}

	// --- 3. 业务逻辑 ---
	const getdetail = () => {
		getUserById(targetUserId.value).then(res => {
			targetUserAvatar.value = res.data.avatar
			targetUserName.value = res.data.name
		})
	}

	const loadHistory = () => {
		getHistoyByThreadId(threadId.value).then(res => {
			if (res.data && Array.isArray(res.data)) {
				messageList.value = res.data.map(item => ({
					id: item.id,
					content: item.content,
					time: item.time,
					senderId: item.senderId
				}))
				scrollToBottom()
			}
		})
	}

	onLoad((options) => {
		targetUserId.value = Number(options.targetId)
		targetUserName.value = decodeURIComponent(options.targetName || '商家')
		goodId.value = Number(options.goodId || 0)
		threadId.value = Number(options.threadId)

		// 获取路由携带的商品信息用于展示在顶部栏
		if (options.productImg) productInfo.value.imageUrl = options.productImg
		if (options.price) productInfo.value.price = options.price
		if (options.title) productInfo.value.title = options.title

		getdetail()
		connectWebSocket()
		loadHistory()
	})

	onUnmounted(() => {
		if (socketTask) socketTask.close()


	})
	// 建议使用 onHide，因为涵盖了更多离开场景
	onHide(() => {
	    shedu();
	});
	
	// 如果是直接销毁页面（比如关闭），也会执行
	onUnload(() => {
	    shedu();
	});

	const shedu = () => {
		 
		markAsRead({
			threadId: threadId.value,
			userId: currentUser.value.id
		}).then(res => {

		})
	}

	// --- 4. WebSocket ---
	// --- 4. WebSocket ---
	const connectWebSocket = () => {
		const wsUrl = `ws://localhost:9090/ws/chat/${currentUser.value.id}`
		socketTask = uni.connectSocket({
			url: wsUrl,
			success: () => console.log('WS尝试连接')
		})

		socketTask.onMessage((res) => {
			try {
				const data = JSON.parse(res.data)

				// --- 核心修复：增加类型判断 ---
				// 只有当消息类型是 chat 且有 content 时才展示气泡
				if (data.type === 'chat' && data.content) {
					messageList.value.push({
						id: data.time || Date.now(),
						senderId: data.senderId,
						content: data.content,
						time: data.time
					})
					scrollToBottom()
				}
			} catch (e) {
				console.error('消息解析失败', e)
			}
		})
	}

	// --- 5. 发送逻辑 ---
	const handleSend = (textOverride = null) => {
		const content = typeof textOverride === 'string' ? textOverride : inputMessage.value.trim()
		if (!content || !socketTask) return

		const sendData = {
			type: "text",
			toUserId: targetUserId.value,
			content: content,
			goodId: goodId.value
		}

		socketTask.send({
			data: JSON.stringify(sendData),
			success: () => {
				messageList.value.push({
					id: Date.now(),
					senderId: currentUser.value.id,
					content: content,
					time: Date.now()
				})
				inputMessage.value = ''
				scrollToBottom()
			}
		})
	}

	// 发送商品链接 (修改为发送 JSON 字符串)
	const sendProductLink = () => {
		if (!productInfo.value.title) return;
		const goodsData = {
			imageUrls: productInfo.value.imageUrl,
			price: productInfo.value.price,
			title: productInfo.value.title,
			id: goodId.value
		};
		handleSend(JSON.stringify(goodsData));
	}

	// --- 6. 工具方法 ---
	const scrollToBottom = () => {
		nextTick(() => {
			if (messageList.value.length > 0) {
				const lastId = messageList.value[messageList.value.length - 1].id
				scrollToView.value = 'msg-' + lastId
			}
		})
	}

	const isImageMsg = (content) => {
		if (!content || typeof content !== 'string') return false;
		return content.match(/\.(jpeg|jpg|gif|png)$/) != null || (content.startsWith('http') && content.length < 200 &&
			!content.includes(' '));
	}

	const previewImg = (url) => {
		uni.previewImage({
			urls: [url]
		})
	}

	const goBack = () => uni.navigateBack()
</script>

<style lang="scss" scoped>
	.chat-detail-page {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #F8F8F8;
	}

	.chat-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 60rpx 30rpx 20rpx;
		background-color: #fff;

		.header-left {
			display: flex;
			align-items: center;

			.back-btn {
				font-size: 40rpx;
				padding-right: 20rpx;
			}

			.user-info {
				.target-name {
					font-size: 30rpx;
					font-weight: bold;
					color: #333;
				}

				.credit-tag {
					background: #E8F9F1;
					color: #07C160;
					font-size: 20rpx;
					padding: 2rpx 10rpx;
					border-radius: 4rpx;
					margin-top: 4rpx;
					display: inline-block;
				}
			}
		}

		.more-icon {
			font-size: 26rpx;
			color: #333;
			font-weight: bold;
		}
	}

	/* 顶部商品卡片 */
	.product-card {
		display: flex;
		align-items: center;
		padding: 20rpx;
		background-color: #fff;
		margin: 10rpx 20rpx;
		border-radius: 16rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

		.p-img {
			width: 100rpx;
			height: 100rpx;
			border-radius: 8rpx;
			margin-right: 20rpx;
		}

		.p-info {
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			height: 90rpx;

			.p-price {
				color: #ff5000;
				font-size: 28rpx;
				font-weight: bold;
			}

			.p-title {
				font-size: 24rpx;
				color: #333;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}

		.send-product-btn {
			background-color: #ff5000;
			color: #fff;
			font-size: 24rpx;
			padding: 10rpx 24rpx;
			border-radius: 30rpx;
			margin-left: 10rpx;
		}
	}

	.chat-content {
		flex: 1;
		overflow: hidden;
	}

	.message-list {
		padding: 20rpx;

		.loading-tip {
			text-align: center;
			color: #999;
			font-size: 22rpx;
			margin: 20rpx 0;
		}
	}

	.message-item {
		display: flex;
		margin-bottom: 40rpx;

		.avatar {
			width: 72rpx;
			height: 72rpx;
			border-radius: 12rpx;
			flex-shrink: 0;
		}

		.message-bubble {
			max-width: 70%;
			padding: 18rpx 24rpx;
			margin-left: 20rpx;
			background-color: #fff;
			border-radius: 0 20rpx 20rpx 20rpx;

			.text {
				font-size: 28rpx;
				color: #333;
				line-height: 1.5;
				word-break: break-all;
			}

			.content-img {
				width: 300rpx;
				border-radius: 10rpx;
			}

			/* 商品消息内部样式 (新增) */
			.goods-msg-card {
				width: 320rpx;

				.msg-goods-img {
					width: 100%;
					height: 240rpx;
					border-radius: 8rpx;
					background: #f4f4f4;
				}

				.msg-goods-info {
					margin-top: 10rpx;

					.msg-goods-title {
						font-size: 24rpx;
						color: #333;
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2;
						overflow: hidden;
					}

					.msg-goods-price {
						color: #FF4400;
						font-weight: bold;
						font-size: 28rpx;
						margin-top: 6rpx;
						display: block;
					}
				}
			}
		}

		&.self {
			flex-direction: row-reverse;

			.message-bubble {
				margin-left: 0;
				margin-right: 20rpx;
				background-color: #D4FB1B;
				border-radius: 20rpx 0 20rpx 20rpx;
			}
		}
	}

	.input-area {
		display: flex;
		align-items: center;
		padding: 20rpx 30rpx calc(20rpx + env(safe-area-inset-bottom));
		background-color: #fff;
		border-top: 1rpx solid #eee;

		.input-box {
			flex: 1;
			height: 76rpx;
			padding: 0 30rpx;
			background-color: #F5F5F5;
			border-radius: 38rpx;
			font-size: 28rpx;
		}

		.send-btn {
			margin-left: 20rpx;
			padding: 0 34rpx;
			height: 68rpx;
			line-height: 68rpx;
			border-radius: 34rpx;

			text {
				font-size: 26rpx;
				font-weight: bold;
			}
		}
	}
</style>