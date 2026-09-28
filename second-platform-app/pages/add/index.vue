<template>
	<view class="container">
		<view class="color-bg"></view>
		<view class="texture-mask"></view>

		<image src="/static/dog.png" mode="aspectFit" class="overlap-dog-img"></image>

		<view class="top-content-area">
			<view class="title-group-adaptive">
				<text class="main-title">AI拍照，识别价值</text>
				<text class="sub-title">一张照片就能卖，拍得多赚得多</text>
			</view>
		</view>

		<view class="bottom-white-content">
			<view class="camera-embed-box">
				<view class="glass-camera-btn" @tap="handleUpload">
					<view class="glass-inner">
						<uni-icons type="camera-filled" size="36" color="#333"></uni-icons>
						<text class="btn-text">AI-识价</text>
					</view>
					<view class="theme-glow"></view>
				</view>
			</view>

			<view class="sell-section">
				<view class="section-header">
					<text class="bold-title">快速发布</text>
					<text class="gradient-sub-text">拍照照片就能赚</text>
				</view>

				<view class="cool-card-adaptive first-card-special" @tap="goToPublish">
					<view class="card-left">
						<view class="icon-box-theme yellow-bg">
							<image src="/static/xianzhi.png" mode="" style="width: 90%;height: 90%;"></image>
						</view>
					</view>
					<view class="card-content">
						<text class="card-main-text">发闲置</text>
						<text class="card-sub-text">校园保证，诚信可靠</text>
					</view>
					<uni-icons type="right" size="16" color="#bfbfbf"></uni-icons>
				</view>

				<view class="cool-card-adaptive mt-30" @tap="goToPost">
					<view class="card-left">
						<view class="icon-box-theme blue-bg">
							<uni-icons type="chatboxes-filled" size="28" color="#fff"></uni-icons>
						</view>
					</view>
					<view class="card-content">
						<text class="card-main-text">发帖子</text>
						<text class="card-sub-text">分享生活，寻找同好</text>
					</view>
					<uni-icons type="right" size="16" color="#bfbfbf"></uni-icons>
				</view>

				<view class="safe-area-bottom"></view>
			</view>
		</view>

		<view v-if="showMask" class="ai-mask-overlay">
			<view class="close-btn" @tap="resetAI">
				<uni-icons type="closeempty" size="24" color="#fff"></uni-icons>
			</view>

			<view class="ai-panel">
				<view class="image-preview-container">
					<image :src="tempFilePath" mode="aspectFill" class="target-image"></image>
					<view v-if="isIdentifying" class="scanner-layer">
						<view class="scan-line"></view>
					</view>
				</view>

				<view class="ai-info-box">
					<view v-if="isIdentifying" class="loading-state">
						<text class="typing">正在评估价值...</text>
					</view>
					<view v-if="identifiedPrice" class="result-state">
						<view class="price-only">
							<text class="unit">￥</text>
							<text class="num">{{ identifiedPrice }}</text>
						</view>
						<button class="publish-btn" @tap="goToPublish">立即换钱</button>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue'

	const showMask = ref(false)
	const isIdentifying = ref(false)
	const identifiedPrice = ref(null)
	const tempFilePath = ref('')

	const handleUpload = () => {
		uni.chooseImage({
			count: 1,
			sizeType: ['compressed'],
			success: (res) => {
				tempFilePath.value = res.tempFilePaths[0]
				showMask.value = true
				isIdentifying.value = true
				identifiedPrice.value = null
				setTimeout(() => {
					isIdentifying.value = false
					identifiedPrice.value = (Math.random() * 800 + 100).toFixed(2)
				}, 2200)
			}
		})
	}

	const resetAI = () => {
		showMask.value = false
		tempFilePath.value = ''
		identifiedPrice.value = null
		isIdentifying.value = false
	}

	const goToPublish = () => {
		uni.navigateTo({
			url: '/pages/addgoods/index',
			success: () => {
				// 可选：跳转成功后的回调
			},
			fail: (err) => {
				// 跳转失败时的提示
				uni.showToast({
					title: '跳转失败',
					icon: 'none'
				});
				console.error('跳转失败:', err);
			}
		});
	}
	const goToPost = () => {
		uni.showToast({
			title: '去发帖子...',
			icon: 'none'
		})
	}
	// handleAction 函数目前在 HTML 中已无引用，可根据需要保留或删除
	const handleAction = (name) => {
		uni.showToast({
			title: '点击了' + name,
			icon: 'none'
		})
	}
</script>

<style lang="scss">
	/* 样式部分保持不变，冗余的样式类（如 .mini-card）不会影响页面显示 */
	.container {
		min-height: 100vh;
		background-color: #f8f9fb;
		position: relative;
		display: flex;
		flex-direction: column;
		overflow-x: hidden;
	}

	.color-bg {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 45vh;
		background: linear-gradient(100deg, #D4FB1B 60%, rgba(212, 251, 27, 0) 100%);
		z-index: 0;
	}

	.texture-mask {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 350rpx;
		background-image: radial-gradient(#c6e916 2px, transparent 2px);
		background-size: 20px 20px;
		opacity: 0.2;
		z-index: 1;
	}

	.overlap-dog-img {
		position: absolute;
		width: 260rpx;
		height: 300rpx;
		z-index: 25;
		top: calc(14vh - 100rpx);
		right: -2rpx;
	}

	.top-content-area {
		position: relative;
		z-index: 10;
		padding: 120rpx 40rpx 40rpx;

		.main-title {
			display: block;
			font-size: 52rpx;
			font-weight: 900;
			color: #333;
			font-style: italic;
			text-shadow: 2rpx 2rpx 0px #fff;
		}

		.sub-title {
			font-size: 26rpx;
			color: rgba(0, 0, 0, 0.4);
			margin-top: 16rpx;
			display: block;
			font-weight: bold;
		}
	}

	.bottom-white-content {
		flex: 1;
		background-color: #fff;
		position: relative;
		margin-top: 80rpx;
		z-index: 20;
		border-radius: 80rpx 80rpx 0 0;
		padding: 0 40rpx 40rpx;
	}

	.camera-embed-box {
		display: flex;
		justify-content: center;
		position: relative;
		top: -110rpx;
		margin-bottom: -80rpx;

		.glass-camera-btn {
			width: 220rpx;
			height: 220rpx;
			display: flex;
			justify-content: center;
			align-items: center;

			.glass-inner {
				width: 190rpx;
				height: 190rpx;
				background: #fff;
				border-radius: 50%;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				z-index: 2;
				border: 8rpx solid #D4FB1B;

				.btn-text {
					font-size: 26rpx;
					color: #333;
					font-weight: 900;
					margin-top: 10rpx;
				}
			}

			.theme-glow {
				position: absolute;
				width: 210rpx;
				height: 210rpx;
				background: #D4FB1B;
				border-radius: 50%;
				filter: blur(30rpx);
				opacity: 0.3;
				z-index: 1;
			}
		}
	}

	.sell-section {
		margin-top: 30rpx;

		.section-header {
			margin-bottom: 20rpx;
			display: flex;
			align-items: baseline;

			.bold-title {
				font-size: 36rpx;
				font-weight: 800;
				color: #333;
			}

			.bold-title-small {
				font-size: 38rpx;
				font-weight: 1000;
				color: #333;
			}

			.gradient-sub-text {
				font-size: 24rpx;
				margin-left: 330rpx;
				font-weight: bold;
				background: linear-gradient(90deg, #ff8d0e, #ff5000);
				-webkit-background-clip: text;
				color: transparent;
			}
		}

		.mt-30 {
			margin-top: 30rpx;
		}

		.cool-card-adaptive {
			display: flex;
			align-items: center;
			background: #fcfcfc;
			border-radius: 15rpx;
			padding: 35rpx;
			border: 1rpx solid #f2f2f2;
			box-sizing: border-box;

			&.first-card-special {
				background-color: #F8F3DD;
				border: none;
			}

			.card-left .icon-box-theme {
				width: 100rpx;
				height: 100rpx;
				border-radius: 24rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-right: 30rpx;

				&.yellow-bg {}

				&.blue-bg {
					background: #12d4ff;
				}
			}

			.card-content {
				flex: 1;

				.card-main-text {
					font-size: 34rpx;
					font-weight: bold;
					color: #333;
				}

				.card-sub-text {
					font-size: 24rpx;
					color: #999;
					margin-top: 6rpx;
					display: block;
				}
			}
		}
	}

	.ai-mask-overlay {
		position: fixed;
		inset: 0;
		background: rgba(0, 0, 0, 0.95);
		z-index: 1000;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;

		.close-btn {
			position: absolute;
			top: 100rpx;
			right: 40rpx;
		}

		.ai-panel {
			width: 85%;

			.image-preview-container {
				position: relative;
				width: 100%;
				height: 750rpx;
				border-radius: 40rpx;
				overflow: hidden;

				.target-image {
					width: 100%;
					height: 100%;
				}

				.scanner-layer {
					position: absolute;
					inset: 0;
					z-index: 10;

					.scan-line {
						position: absolute;
						top: 0;
						left: 0;
						width: 100%;
						height: 4rpx;
						background: #D4FB1B;
						box-shadow: 0 0 20rpx #D4FB1B;
						animation: scanMove 2.5s infinite linear;
					}
				}
			}
		}

		.ai-info-box {
			margin-top: 60rpx;
			text-align: center;

			.loading-state .typing {
				color: #D4FB1B;
				font-size: 34rpx;
				font-weight: 600;
			}

			.result-state {
				.price-only {
					color: #D4FB1B;
					text-shadow: 0 0 30rpx rgba(212, 251, 27, 0.8);
					margin-bottom: 40rpx;

					.unit {
						font-size: 56rpx;
						font-weight: bold;
					}

					.num {
						font-size: 110rpx;
						font-weight: 900;
					}
				}

				.publish-btn {
					background: #D4FB1B;
					color: #000;
					height: 100rpx;
					line-height: 100rpx;
					border-radius: 50rpx;
					font-weight: 800;
					font-size: 34rpx;
					padding: 0 120rpx;
				}
			}
		}
	}

	@keyframes scanMove {
		0% {
			top: 0;
		}

		100% {
			top: 100%;
		}
	}
</style>