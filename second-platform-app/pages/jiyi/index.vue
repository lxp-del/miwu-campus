<template>
	<view class="chat-container">
		<view class="back-btn" @tap="goHome">
			<text class="back-icon">‹</text>
		</view>

		<scroll-view scroll-y class="chat-body" :scroll-top="scrollTop" scroll-with-animation>
			<view class="chat-list" id="chatList">
				<view style="height: 140rpx;"></view>

				<view v-for="(msg, index) in messageList" :key="index" :class="['msg-row', msg.role]">
					<image v-if="msg.role === 'ai'" class="avatar" src="/static/indexro.png" mode="aspectFill"></image>
					<view class="bubble-container">
						<view :class="msg.role === 'ai' ? 'ai-plain-text' : 'bubble'" v-if="msg.content">
							<text>{{ msg.content }}</text>
							<text class="cursor" v-if="msg.typing">|</text>
						</view>

						<scroll-view scroll-x class="ai-goods-scroll" :show-scrollbar="false" :enhanced="true"
							v-if="msg.role === 'ai' && msg.goods && msg.goods.length > 0">
							<view class="goods-flex-container">
								<view class="custom-goods-card" v-for="(item, gidx) in msg.goods" :key="gidx"
									@tap="goToDetail(item.id)">
									<view class="img-wrapper">
										<image class="g-img" :src="item.img" mode="aspectFill"></image>
										<view class="title-mask">
											<text class="mask-title">{{ item.title }}</text>
										</view>
									</view>
									<view class="detail-btn">查看详情</view>
								</view>
							</view>
						</scroll-view>
					</view>
				</view>

				<view class="msg-row ai" v-if="isWaiting">
					<view class="gemini-icon">
						<view class="star-sparkle"></view>
					</view>
					<view class="loading-text-wrapper">
						<text class="shimmer-text">正在分析记忆...</text>
					</view>
				</view>

				<view style="height: 180rpx;"></view>
			</view>
		</scroll-view>

		<view class="fixed-bottom-input">
			<view class="input-container">


				<view class="input-box-wrapper">
					<input class="input-box" type="text" v-model="inputText" :focus="inputFocus"
						placeholder="回复‘确认’以查看商品..." placeholder-style="color:#999" @confirm="sendUserMsg"
						:disabled="isTyping || isWaiting" />

					<view class="send-icon-btn" v-if="inputText.trim().length > 0" @tap="sendUserMsg">
						<text class="send-arrow">↑</text>
					</view>
				</view>
			</view>
		</view>

		<view class="action-mask" v-if="showActionSheet" @tap="toggleActionSheet"></view>
		<view class="action-sheet" :class="{ 'show': showActionSheet }">
			<view class="action-item" @tap="handleCamera"><text class="action-icon">📷</text><text>相机</text></view>
			<view class="action-item" @tap="handleGallery"><text class="action-icon">🖼️</text><text>图库</text></view>
			<view class="action-item" @tap="handleFile"><text class="action-icon">📁</text><text>文件</text></view>
			<view class="action-cancel" @tap="toggleActionSheet">取消</view>
		</view>
	</view>
</template>

<script>
	import {
		chatDeepSeek
	} from "@/apis/robot/robot.js"

	export default {
		data() {
			return {
				inputText: '',
				messageList: [],
				scrollTop: 0,
				isTyping: false,
				isWaiting: false,
				showActionSheet: false,
				inputFocus: false,
				lastRecommendedKey: '镜子'
			};
		},
		onLoad() {
			this.initMemoryChat();
		},
		methods: {
			goHome() {
				uni.switchTab({
					url: '/pages/robot/index'
				});
			},
			toggleActionSheet() {
				this.showActionSheet = !this.showActionSheet;
			},
			initMemoryChat() {
				const welcomeText = `根据您的聊天记录，我发现您近期在寻找家居好物，尤其对“${this.lastRecommendedKey}”很感兴趣。需要现在为您推荐吗？`;
				this.simulateTypewriter(welcomeText, []);
			},
			sendUserMsg() {
				const text = this.inputText.trim();
				if (!text || this.isTyping || this.isWaiting) return;
				this.inputText = '';
				this.executeChat(text);
			},
			async executeChat(text) {
				this.messageList.push({
					role: 'user',
					content: text
				});
				this.scrollToBottom();

				let queryText = text;
				if (text === '确认' || text.includes('好的') || text.includes('查看')) {
					queryText = this.lastRecommendedKey;
				}

				this.isWaiting = true;
				try {
					const res = await chatDeepSeek({
						message: queryText
					});
					this.isWaiting = false;
					const rawData = (typeof res === 'object' && res.data) ? res.data : res;
					this.parseAiResponse(String(rawData));
				} catch (err) {
					this.isWaiting = false;
					this.simulateTypewriter("获取推荐失败，请稍后再试。", []);
				}
			},
			parseAiResponse(rawText) {
				const goods = [];
				let textContent = rawText;
				const reg = /([^\n]+)\n\[ID\](.*?)\[\/ID\]\n\[IMG\](.*?)\[\/IMG\]/g;
				let match;
				while ((match = reg.exec(textContent)) !== null) {
					goods.push({
						title: match[1].replace('--- 相关商品推荐 ---', '').trim(),
						id: match[2].trim(),
						img: match[3].trim()
					});
					textContent = textContent.replace(match[0], '');
				}
				textContent = textContent.replace('--- 相关商品推荐 ---', '').trim();
				this.simulateTypewriter(textContent || "为您找到以下相关商品：", goods);
			},
			simulateTypewriter(fullText, goods) {
				const newMsgIndex = this.messageList.length;
				this.messageList.push({
					role: 'ai',
					content: '',
					goods: [],
					typing: true
				});
				this.isTyping = true;
				let currentIdx = 0;
				const timer = setInterval(() => {
					if (currentIdx < fullText.length) {
						this.messageList[newMsgIndex].content += fullText[currentIdx];
						currentIdx++;
						if (currentIdx % 5 === 0) this.scrollToBottom();
					} else {
						this.messageList[newMsgIndex].typing = false;
						this.messageList[newMsgIndex].goods = goods;
						this.isTyping = false;
						clearInterval(timer);
						setTimeout(() => this.scrollToBottom(), 100);
					}
				}, 20);
			},
			scrollToBottom() {
				this.$nextTick(() => {
					const query = uni.createSelectorQuery().in(this);
					query.select('#chatList').boundingClientRect(data => {
						if (data) this.scrollTop = data.height + 1000;
					}).exec();
				});
			},
			goToDetail(id) {
				uni.navigateTo({
					url: `/pages/gooddetail/index?id=${id}`
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	/* 页面整体背景：采用 #D4FB1B 渐变色 */
	.chat-container {
		width: 100%;
		height: 100vh;
		display: flex;
		flex-direction: column;
		/* 顶部颜色深，向下透明过渡 */
		background: linear-gradient(180deg, rgba(212, 251, 27, 0.1) 0%, #ffffff 20%, #ffffff 85%, rgba(212, 251, 27, 0.08) 100%);
		overflow: hidden;
		position: relative;
	}

	.back-btn {
		position: absolute;
		left: 30rpx;
		top: 80rpx;
		width: 70rpx;
		height: 70rpx;
		background-color: rgba(255, 255, 255, 0.6);
		backdrop-filter: blur(8rpx);
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 100;

		.back-icon {
			font-size: 56rpx;
			color: #333;
			margin-top: -6rpx;
		}
	}

	.chat-body {
		margin-top: 50rpx;
		flex: 1;

		.chat-list {
			padding: 20rpx 30rpx;
		}
	}

	.msg-row {
		display: flex;
		margin-bottom: 40rpx;

		.avatar {
			width: 74rpx;
			height: 74rpx;
			border-radius: 50%;
			flex-shrink: 0;
			box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.05);
		}

		.bubble-container {
			max-width: 82%;
			margin-left: 20rpx;
		}

		/* 用户气泡：深色背景突出 */
		&.user {
			flex-direction: row-reverse;

			.bubble {
				background: #333;
				color: #D4FB1B;
				margin-right: 20rpx;
				padding: 24rpx 32rpx;
				border-radius: 36rpx 4rpx 36rpx 36rpx;
				font-size: 28rpx;
				box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.1);
			}
		}

		/* AI 文本：半透明磨砂质感 */
		&.ai .ai-plain-text {
			padding: 20rpx 28rpx;
			font-size: 28rpx;
			color: #333;
			background: rgba(255, 255, 255, 0.5);
			border-radius: 4rpx 36rpx 36rpx 36rpx;
			backdrop-filter: blur(10rpx);
		}
	}

	.fixed-bottom-input {
		padding: 24rpx 30rpx 60rpx;
		background: rgba(255, 255, 255, 0.9);
		backdrop-filter: blur(20rpx);
		border-top: 1rpx solid rgba(0, 0, 0, 0.05);

		.input-container {
			display: flex;
			align-items: center;
		}

		.plus-btn {
			width: 76rpx;
			height: 76rpx;
			border-radius: 50%;
			background: #fff;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-right: 20rpx;

			.plus-icon {
				font-size: 48rpx;
				color: #666;
			}
		}

		.input-box-wrapper {
			flex: 1;
			display: flex;
			align-items: center;
			background: #fff;
			border-radius: 45rpx;
			padding: 0 10rpx 0 25rpx;
			height: 90rpx;
			box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.03);
		}

		.input-box {
			flex: 1;
			font-size: 28rpx;
		}

		.send-icon-btn {
			width: 66rpx;
			height: 66rpx;
			background: #333;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;

			.send-arrow {
				color: #D4FB1B;
				font-size: 34rpx;
			}
		}
	}

	/* 商品卡片样式 */
	.ai-goods-scroll {
		width: 100%;
		margin-top: 20rpx;
	}

	.goods-flex-container {
		display: flex;
		gap: 24rpx;
	}

	.custom-goods-card {
		width: 340rpx;
		flex-shrink: 0;
		background: #fff;
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.08);

		.img-wrapper {
			width: 340rpx;
			height: 260rpx;
			position: relative;
		}

		.g-img {
			width: 100%;
			height: 100%;
			background: #eee;
		}

		.title-mask {
			position: absolute;
			bottom: 0;
			left: 0;
			right: 0;
			background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
			padding: 20rpx 14rpx 10rpx;
		}

		.mask-title {
			color: #fff;
			font-size: 22rpx;
			display: block;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			font-weight: 500;
		}

		.detail-btn {
			text-align: center;
			padding: 16rpx 0;
			font-size: 24rpx;
			color: #333;
			font-weight: bold;
			background: #fafafa;
		}
	}

	/* 加载动画 */
	.gemini-icon {
		width: 74rpx;
		height: 74rpx;
		background: rgba(255, 255, 255, 0.6);
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-right: 20rpx;

		.star-sparkle {
			width: 32rpx;
			height: 32rpx;
			background: #333;
			clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
			animation: gemini-spin 2s infinite linear;
		}
	}

	@keyframes gemini-spin {
		from {
			transform: rotate(0deg);
		}

		to {
			transform: rotate(360deg);
		}
	}

	.shimmer-text {
		font-size: 26rpx;
		color: #666;
	}

	.cursor {
		animation: blink 0.8s infinite;
		color: #333;
		font-weight: bold;
	}

	@keyframes blink {

		0%,
		100% {
			opacity: 1;
		}

		50% {
			opacity: 0;
		}
	}

	/* Action Sheet */
	.action-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.3);
		z-index: 1000;
	}

	.action-sheet {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		background: #fff;
		border-radius: 40rpx 40rpx 0 0;
		z-index: 1001;
		transform: translateY(100%);
		transition: transform 0.3s cubic-bezier(0.25, 1, 0.5, 1);

		&.show {
			transform: translateY(0);
		}

		.action-item {
			display: flex;
			align-items: center;
			padding: 38rpx 50rpx;
			font-size: 32rpx;
			border-bottom: 1rpx solid #f2f2f2;

			.action-icon {
				margin-right: 24rpx;
				font-size: 40rpx;
			}
		}

		.action-cancel {
			padding: 38rpx;
			text-align: center;
			color: #999;
			border-top: 12rpx solid #f8f8f8;
		}
	}
</style>