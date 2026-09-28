<template>
	<view class="chat-detail-page">
		<view class="chat-header">
			<view class="header-left">
				<view class="back-btn" @click="goBack">
					<text class="iconfont">←</text>
				</view>
				<view class="user-info">
					<text class="username">{{ targetUserName || '正在加载...' }}</text>
					<view class="credit-tag">
						<text>信誉分: 98</text>
					</view>
				</view>
			</view>
			<view class="header-right">
				<text class="more-icon">···</text>
			</view>
		</view>

		<view class="goods-bar-container" v-if="goodInfo">
			<view class="goods-bar">
				<image class="goods-img" :src="firstImage" mode="aspectFill"></image>
				<view class="goods-detail">
					<text class="goods-price">￥{{ goodInfo.price }}</text>
					<text class="goods-title">{{ goodInfo.title }}</text>
				</view>
				<view class="quick-send-btn" @click="sendGoodsLink">
					<text>立即发送</text>
				</view>
			</view>
		</view>

		<scroll-view class="chat-content" scroll-y :scroll-into-view="scrollToView" scroll-with-animation>
			<view class="message-list">
				<view class="message-item" :class="{ self: item.senderId == currentUser.id }"
					v-for="item in messageList" :key="item.id" :id="'msg-' + item.id">
					<image class="avatar" :src="item.senderId == currentUser.id ? currentUser.avatar : targetUserAvatar"
						mode="aspectFill"></image>

					<view class="message-bubble">
						<view v-if="isGoodsMsg(item.content)" class="goods-msg-card">
							<image :src="parseGoods(item.content).img" mode="aspectFill"></image>
							<view class="msg-goods-info">
								<text class="msg-goods-title">{{ parseGoods(item.content).title }}</text>
								<text class="msg-goods-price">￥{{ parseGoods(item.content).price }}</text>
							</view>
						</view>
						<text v-else class="text">{{ item.content }}</text>
					</view>
				</view>
			</view>
			<view style="height: 20rpx;"></view>
		</scroll-view>

		<view class="input-area">
			<input type="text" v-model="inputMessage" placeholder="想了解商品更多细节..." confirm-type="send"
				@confirm="handleSend" />
			<view class="send-button" @click="handleSend" :class="{ active: inputMessage.trim() }">
				<text>发送</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		onUnmounted,
		nextTick,
		computed
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		getGoodsById
	} from "@/apis/goods/goodstopandtype.js"

	// 基础数据
	const currentUser = ref(uni.getStorageSync('currentUser') || {
		id: null,
		avatar: '',
		name: ''
	})
	const targetUserId = ref('')
	const targetUserName = ref('')
	const targetUserAvatar = ref('')
	const goodId = ref('')
	const goodInfo = ref(null)
	const messageList = ref([])
	const inputMessage = ref('')
	const scrollToView = ref('')

	// WebSocket 状态管理
	let socketTask = null
	const isConnected = ref(false)

	// 计算商品首图
	const firstImage = computed(() => {
		if (goodInfo.value && goodInfo.value.imageUrls) {
			return goodInfo.value.imageUrls.split(',')[0]
		}
		return ''
	})

	// 解析逻辑
	const isGoodsMsg = (content) => {
		return content && content.includes('imageUrls') && content.includes('price');
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
			return {};
		}
	}

	// 获取数据
	const getGoodDetail = async () => {
		if (!goodId.value) return
		try {
			const res = await getGoodsById(goodId.value)
			goodInfo.value = res.data
			targetUserId.value = res.data.sysUser.id
			targetUserName.value = res.data.sysUser.name
			targetUserAvatar.value = res.data.sysUser.avatar || ''
		} catch (error) {
			console.error('获取商品详情失败', error)
		}
	}

	// 连接服务器
	const connectWebSocket = () => {
		if (!currentUser.value.id) return;
		const wsUrl = `ws://127.0.0.1:9090/ws/chat/${currentUser.value.id}`

		socketTask = uni.connectSocket({
			url: wsUrl,
			success: () => {
				console.log('Socket 初始化成功');
			}
		})

		socketTask.onOpen(() => {
			isConnected.value = true;
		})
		socketTask.onClose(() => {
			isConnected.value = false;
		})
		socketTask.onError(() => {
			isConnected.value = false;
		})

		socketTask.onMessage((res) => {
			console.log('收到原始消息:', res.data);
			if (!res.data || res.data === "undefined") return;

			try {
				const data = JSON.parse(res.data);

				// --- 核心修复：只处理聊天内容的消息 ---
				// 只有当消息类型是 'chat' 或者包含具体的 content 时才存入列表
				if (data.type === 'chat' && data.content) {
					messageList.value.push(data);
					nextTick(() => scrollToBottom());
				} else if (data.type === 'list_update') {
					// 这里可以处理列表刷新逻辑，但不应该存入聊天气泡
					console.log('收到列表刷新通知，不显示气泡');
				}
			} catch (e) {
				console.error('解析后端消息失败', e);
			}
		});
	}

	// 核心发送逻辑 (修复 TypeError 和 undefined 报错)
	const sendMessage = (content, type = "text") => {
		if (!isConnected.value || !socketTask) {
			uni.showToast({
				title: '聊天连接中...',
				icon: 'none'
			});
			return;
		}
		if (!content) return;

		const sendData = {
			type: type,
			toUserId: parseInt(targetUserId.value),
			content: String(content), // 强制转字符串防止 FastJSON 报错
			goodId: goodId.value
		}

		socketTask.send({
			data: JSON.stringify(sendData),
			success: () => {
				messageList.value.push({
					id: Date.now(),
					senderId: currentUser.value.id,
					content: content,
					timestamp: Date.now()
				})
				nextTick(() => scrollToBottom())
			},
			fail: () => {
				uni.showToast({
					title: '发送失败',
					icon: 'none'
				});
			}
		})
	}

	const handleSend = () => {
		if (!inputMessage.value.trim()) return;
		sendMessage(inputMessage.value.trim(), "text");
		inputMessage.value = '';
	}

	const sendGoodsLink = () => {
		if (!goodInfo.value) return;
		const goodsData = {
			imageUrls: goodInfo.value.imageUrls || "",
			price: goodInfo.value.price || "0",
			title: goodInfo.value.title || "",
			id: goodInfo.value.id
		};
		sendMessage(JSON.stringify(goodsData), "goods");
	}

	const scrollToBottom = () => {
		if (messageList.value.length > 0) {
			const lastMsg = messageList.value[messageList.value.length - 1]
			scrollToView.value = `msg-${lastMsg.id}`
		}
	}

	onLoad((options) => {
		if (options.goodsId) goodId.value = options.goodsId
		getGoodDetail()
		connectWebSocket()
	})

	const goBack = () => {
		if (socketTask) socketTask.close();
		uni.navigateBack();
	}

	onUnmounted(() => {
		if (socketTask) socketTask.close();
	})
</script>

<style lang="scss" scoped>
	.chat-detail-page {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #F6F6F6;

		.chat-header {
			background-color: #fff;
			padding: 60rpx 30rpx 20rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 1rpx solid #F0F0F0;

			.header-left {
				display: flex;
				align-items: center;

				.back-btn {
					font-size: 40rpx;
					padding-right: 20rpx;
				}

				.user-info {
					.username {
						font-size: 32rpx;
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
				color: #666;
			}
		}

		.goods-bar-container {
			background-color: #fff;
			padding: 20rpx 30rpx;
			margin-bottom: 10rpx;

			.goods-bar {
				display: flex;
				align-items: center;
				background: #FAFAFA;
				padding: 16rpx;
				border-radius: 12rpx;

				.goods-img {
					width: 100rpx;
					height: 100rpx;
					border-radius: 8rpx;
				}

				.goods-detail {
					flex: 1;
					margin: 0 20rpx;

					.goods-price {
						font-size: 30rpx;
						color: #FF4400;
						font-weight: bold;
						display: block;
					}

					.goods-title {
						font-size: 24rpx;
						color: #666;
						line-height: 1.4;
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 1;
						overflow: hidden;
					}
				}

				.quick-send-btn {
					background: #FF4400;
					color: #fff;
					font-size: 24rpx;
					padding: 10rpx 20rpx;
					border-radius: 30rpx;
				}
			}
		}

		.chat-content {
			flex: 1;
			padding: 20rpx;

			.message-item {
				padding-right: 30px;
				display: flex;
				margin-bottom: 30rpx;

				&.self {
					flex-direction: row-reverse;

					.message-bubble {
						background-color: #D4FB1B;
						margin-right: 20rpx;
						margin-left: 0;
						border-radius: 20rpx 4rpx 20rpx 20rpx;
					}
				}

				.avatar {
					width: 84rpx;
					height: 84rpx;
					border-radius: 12rpx;
					background: #eee;
				}

				.message-bubble {
					max-width: 70%;
					padding: 20rpx;
					background-color: #fff;
					margin-left: 20rpx;
					border-radius: 4rpx 20rpx 20rpx 20rpx;

					.text {
						font-size: 28rpx;
						color: #333;
						word-break: break-all;
					}

					.goods-msg-card {
						width: 320rpx;

						image {
							width: 100%;
							height: 240rpx;
							border-radius: 8rpx;
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
			}
		}

		.input-area {
			background: #fff;
			padding: 20rpx 30rpx 60rpx;
			display: flex;
			align-items: center;

			input {
				flex: 1;
				background: #F2F2F2;
				height: 80rpx;
				border-radius: 12rpx;
				padding: 0 24rpx;
				font-size: 28rpx;
			}

			.send-button {
				margin-left: 20rpx;
				width: 120rpx;
				height: 80rpx;
				line-height: 80rpx;
				text-align: center;
				background: #F2F2F2;
				border-radius: 12rpx;

				text {
					color: #999;
					font-size: 28rpx;
				}

				&.active {
					background: #D4FB1B;

					text {
						color: #333;
						font-weight: bold;
					}
				}
			}
		}
	}
</style>