<template>
	<view class="chat-container">
		<view class="back-btn" @tap="goHome">
			<text class="back-icon">‹</text>
		</view>

		<scroll-view scroll-y class="chat-body" :scroll-top="scrollTop" scroll-with-animation>
			<view class="chat-list" id="chatList">
				<view style="height: 160rpx;"></view>

				<view v-for="(msg, index) in messageList" :key="index" :class="['msg-row', msg.role]">
					<image v-if="msg.role === 'ai'" class="avatar" src="/static/indexro.png" mode="aspectFill"></image>
					<view class="bubble-container">
						<view class="ai-plain-text" v-if="msg.role === 'ai' && msg.content">
							<text>{{ msg.content }}</text>
							<text class="cursor" v-if="msg.typing">|</text>
						</view>

						<view class="bubble" v-if="msg.role === 'user' && msg.content">
							<text>{{ msg.content }}</text>
						</view>

						<view class="swiper-wrapper" v-if="msg.role === 'ai' && msg.goods && msg.goods.length > 0">
							<swiper class="goods-swiper" previous-margin="0rpx" next-margin="80rpx"
								:current="msg.currentIndex || 0" @change="onSwiperChange($event, index)">
								<swiper-item v-for="(item, gidx) in msg.goods" :key="gidx">
									<view class="card-item-container" @tap="goToDetail(item.id)">
										<view class="goods-card-inner">
											<image class="goods-image" :src="item.img" mode="aspectFill"></image>
											<view class="goods-info">
												<text class="goods-title">{{ item.title }}</text>
												<view class="goods-footer">
													<text class="price-tag">精选推荐</text>
													<view class="detail-link">
														<text>查看详情</text>
														<text class="arrow">→</text>
													</view>
												</view>
											</view>
										</view>
									</view>
								</swiper-item>
							</swiper>
							<view class="swiper-dots" v-if="msg.goods.length > 1">
								<view v-for="(dot, dIdx) in msg.goods" :key="dIdx"
									:class="['dot', (msg.currentIndex || 0) === dIdx ? 'active' : '']"></view>
							</view>
						</view>
					</view>
				</view>

				<view class="msg-row ai" v-if="isWaiting">
					<view class="gemini-icon">
						<view class="star-sparkle"></view>
					</view>
					<view class="loading-text-wrapper">
						<text class="shimmer-text">根据您的年纪和专业以及购物习惯，正在为您推荐下列物品...</text>
					</view>
				</view>

				<view style="height: 240rpx;"></view>
			</view>
		</scroll-view>

		<view class="fixed-bottom-input">
			<view class="input-container">
				<view class="plus-btn" @tap="toggleActionSheet">
					<text class="plus-icon">+</text>
				</view>

				<view class="input-box-wrapper">
					<input class="input-box" type="text" v-model="inputText" confirm-type="send"
						placeholder="输入关键词继续探索..." placeholder-style="color:#bbb" @confirm="handleSend"
						:disabled="isTyping || isWaiting" />

					<view class="send-icon-btn" v-if="inputText.trim().length > 0" @tap="handleSend">
						<text class="send-arrow">↑</text>
					</view>
				</view>
			</view>
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
				showActionSheet: false
			};
		},
		onLoad() {
			this.executeChat('数学', true);
		},
		methods: {
			goHome() {
				uni.reLaunch({
					url: '/pages/robot/index'
				});
			},
			toggleActionSheet() {
				this.showActionSheet = !this.showActionSheet;
			},
			onSwiperChange(e, msgIndex) {
				this.$set(this.messageList[msgIndex], 'currentIndex', e.detail.current);
			},
			handleSend() {
				const text = this.inputText.trim();
				if (!text || this.isTyping || this.isWaiting) return;
				this.inputText = '';
				this.executeChat(text, false);
			},
			async executeChat(queryText, isInitial = false) {
				if (!isInitial) {
					this.messageList.push({
						role: 'user',
						content: queryText
					});
					this.scrollToBottom();
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
					this.simulateTypewriter("获取资料失败，请重试。", []);
				}
			},
			/**
			 * 核心修复：解析多商品逻辑
			 */
			parseAiResponse(rawText) {
				const goods = [];
				// 分解回复：前半部分是文字，后半部分是商品数据
				const parts = rawText.split('--- 相关商品推荐 ---');
				const chatText = parts[0].trim();
				const goodsData = parts[1] || '';

				// 正则提取：匹配 [ID] 和 [IMG]
				const idMatches = [...goodsData.matchAll(/\[ID\](.*?)\[\/ID\]/g)];
				const imgMatches = [...goodsData.matchAll(/\[IMG\](.*?)\[\/IMG\]/g)];

				// 提取商品标题（通常位于 [ID] 标签上方）
				// 这里采用一种更健壮的方法：根据 \n 分割 goodsData 寻找非标签行
				const lines = goodsData.split('\n').map(l => l.trim()).filter(l => l && !l.includes('[ID]') && !l.includes(
					'[IMG]'));

				// 循环组装所有商品
				idMatches.forEach((match, index) => {
					goods.push({
						id: match[1],
						img: imgMatches[index] ? imgMatches[index][1] : '',
						title: lines[index] || '精选数学资料'
					});
				});

				this.simulateTypewriter(chatText, goods);
			},
			simulateTypewriter(fullText, goods) {
				const newMsgIndex = this.messageList.length;
				this.messageList.push({
					role: 'ai',
					content: '',
					goods: [],
					typing: true,
					currentIndex: 0
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
						this.messageList[newMsgIndex].goods = goods; // 渲染商品数组
						this.isTyping = false;
						clearInterval(timer);
						setTimeout(() => this.scrollToBottom(), 100);
					}
				}, 15);
			},
			scrollToBottom() {
				this.$nextTick(() => {
					uni.createSelectorQuery().in(this).select('#chatList').boundingClientRect(data => {
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
	/* 延续之前的精美样式 */
	.chat-container {
		width: 100%;
		height: 100vh;
		display: flex;
		flex-direction: column;
		background: linear-gradient(180deg, rgba(212, 251, 27, 0.05) 0%, #ffffff 15%, #ffffff 85%, rgba(212, 251, 27, 0.03) 100%);
	}

	.back-btn {
		position: absolute;
		left: 30rpx;
		top: 80rpx;
		width: 76rpx;
		height: 76rpx;
		background: #fff;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 100;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);

		.back-icon {
			font-size: 60rpx;
			color: #333;
			margin-top: -6rpx;
		}
	}

	.chat-body {
		flex: 1;

		.chat-list {
			padding: 20rpx 40rpx;
		}
	}

	.msg-row {
		display: flex;
		margin-bottom: 50rpx;

		.avatar {
			width: 80rpx;
			height: 80rpx;
			border-radius: 50%;
			flex-shrink: 0;
			border: 1rpx solid #f0f0f0;
		}

		.bubble-container {
			max-width: 85%;
			margin-left: 20rpx;
			flex: 1;
		}

		&.user {
			flex-direction: row-reverse;

			.bubble {
				background: #f4f4f4;
				color: #333;
				margin-right: 20rpx;
				padding: 24rpx 32rpx;
				border-radius: 36rpx 4rpx 36rpx 36rpx;
				font-size: 28rpx;
			}
		}

		&.ai .ai-plain-text {
			padding: 10rpx 10rpx 20rpx;
			font-size: 29rpx;
			color: #222;
			line-height: 1.6;
		}
	}

	/* Swiper 卡片 */
	.swiper-wrapper {
		width: 100%;
		margin-top: 10rpx;
	}

	.goods-swiper {
		height: 440rpx;
		width: 100%;
	}

	.card-item-container {
		height: 100%;
		padding-right: 20rpx;
		box-sizing: border-box;
	}

	.goods-card-inner {
		background: #fff;
		border-radius: 24rpx;
		height: 100%;
		overflow: hidden;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
		border: 1rpx solid #f0f0f0;
		display: flex;
		flex-direction: column;

		.goods-image {
			width: 100%;
			height: 260rpx;
			background: #f9f9f9;
		}

		.goods-info {
			padding: 20rpx;
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.goods-title {
				font-size: 26rpx;
				color: #333;
				font-weight: 500;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 2;
				overflow: hidden;
			}

			.goods-footer {
				display: flex;
				justify-content: space-between;
				align-items: center;

				.price-tag {
					font-size: 20rpx;
					color: #D4FB1B;
					background: #333;
					padding: 4rpx 12rpx;
					border-radius: 8rpx;
				}

				.detail-link {
					font-size: 22rpx;
					color: #999;
					display: flex;
					align-items: center;

					.arrow {
						margin-left: 6rpx;
					}
				}
			}
		}
	}

	.swiper-dots {
		display: flex;
		padding: 20rpx 10rpx;

		.dot {
			width: 10rpx;
			height: 10rpx;
			border-radius: 50%;
			background: #e0e0e0;
			margin-right: 12rpx;
			transition: all 0.3s;

			&.active {
				width: 24rpx;
				border-radius: 6rpx;
				background: #333;
			}
		}
	}

	/* 加载 & 输入框 */
	.gemini-icon {
		width: 80rpx;
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-right: 20rpx;

		.star-sparkle {
			width: 36rpx;
			height: 36rpx;
			background: #D4FB1B;
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
		color: #888;
		background: linear-gradient(90deg, #888 25%, #222 50%, #888 75%);
		background-size: 200% 100%;
		animation: shimmer 2s infinite;
		-webkit-background-clip: text;
		background-clip: text;
	}

	@keyframes shimmer {
		0% {
			background-position: 200% 0;
		}

		100% {
			background-position: -200% 0;
		}
	}

	.fixed-bottom-input {
		padding: 24rpx 30rpx 60rpx;
		background: rgba(255, 255, 255, 0.85);
		backdrop-filter: blur(15rpx);
		border-top: 1rpx solid rgba(0, 0, 0, 0.03);

		.input-container {
			display: flex;
			align-items: center;
		}

		.plus-btn {
			width: 80rpx;
			height: 80rpx;
			border-radius: 50%;
			background: #fff;
			border: 1rpx solid #eee;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-right: 20rpx;

			.plus-icon {
				font-size: 50rpx;
				color: #ccc;
			}
		}

		.input-box-wrapper {
			flex: 1;
			display: flex;
			align-items: center;
			background: #f5f5f5;
			border-radius: 40rpx;
			padding: 0 10rpx 0 30rpx;
			height: 90rpx;
		}

		.input-box {
			flex: 1;
			font-size: 28rpx;
			color: #333;
		}

		.send-icon-btn {
			width: 70rpx;
			height: 70rpx;
			background: #333;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;

			.send-arrow {
				color: #D4FB1B;
				font-size: 38rpx;
			}
		}
	}

	.cursor {
		animation: blink 0.8s infinite;
		color: #D4FB1B;
		margin-left: 4rpx;
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
</style>