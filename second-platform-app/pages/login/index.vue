<template>
	<view class="login-container">
		<view class="visual-section">
			<image src="/static/login.png" mode="aspectFit" class="avatar-image" />
			<view class="welcome-text">
				<text class="title">欢迎回来</text>
				<text class="subtitle">拍一下，快速登录</text>
			</view>
		</view>

		<view class="action-section">
			<button class="main-button" @tap="handlePhotoLogin">
				<uni-icons type="camera-filled" size="20" color="#fff"></uni-icons>
				<text class="button-text">拍照 / 上传登录</text>
			</button>

			<view class="alternative-option" @tap="goToPasswordLogin">
				<text>遇到问题？尝试账号密码登录</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		login
	} from '@/apis/login'

	/**
	 * 处理拍照或从相册选择图片登录
	 */
	const handlePhotoLogin = () => {
		uni.chooseImage({
			count: 1,
			sizeType: ['compressed'],
			sourceType: ['camera', 'album'],
			success: (res) => {
				const file = res.tempFiles[0] // H5 返回 File 对象
				const formData = new FormData()
				formData.append('image', file)

				uni.showLoading({
					title: '正在识别中...'
				})

				login(formData)
					.then(res => {
						console.log(res);
						uni.setStorageSync('token', res.token)
						uni.setStorageSync('currentUser', res)
						uni.showToast({
							title: '登录成功',
							icon: 'success'
						})
						uni.switchTab({
							url: '/pages/index/index'
						})
					})
					.catch(err => {
						console.error(err)
						uni.showToast({
							title: '登录失败',
							icon: 'none'
						})
					})
					.finally(() => uni.hideLoading())
			}
		})
	}

	const goToPasswordLogin = () => {
		console.log("跳转至账号密码登录页面");
	};
</script>

<style lang="scss" scoped>
	.login-container {
		/* 渐变背景配置 */
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		background: linear-gradient(135deg, #C3E88D, #FFE485, #B2F0E1);
		padding: 0 40rpx;
	}

	.visual-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 80rpx;
		animation: fadeInDown 0.8s ease-out;

		.avatar-image {
			width: 280rpx;
			height: 280rpx;
			margin-bottom: 30rpx;
		}

		.welcome-text {
			text-align: center;

			.title {
				display: block;
				font-size: 48rpx;
				font-weight: bold;
				color: #333;
				margin-bottom: 12rpx;
			}

			.subtitle {
				font-size: 28rpx;
				color: #666;
			}
		}
	}

	.action-section {
		width: 100%;
		max-width: 600rpx;

		.main-button {
			height: 100rpx;
			background-color: #71b150;
			/* 采用与顶部绿色呼应的深色调 */
			border-radius: 50rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			box-shadow: 0 10rpx 20rpx rgba(0, 0, 0, 0.1);
			border: none;
			transition: transform 0.2s;

			&:active {
				transform: scale(0.98);
			}

			.button-text {
				color: #ffffff;
				font-size: 32rpx;
				font-weight: 500;
				margin-left: 10rpx;
			}
		}

		.alternative-option {
			margin-top: 40rpx;
			text-align: center;

			text {
				font-size: 24rpx;
				color: #777;
				text-decoration: underline;
			}
		}
	}

	/* 简单的进入动画 */
	@keyframes fadeInDown {
		from {
			opacity: 0;
			transform: translateY(-20px);
		}

		to {
			opacity: 1;
			transform: translateY(0);
		}
	}
</style>