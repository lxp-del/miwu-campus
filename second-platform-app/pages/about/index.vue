<template>
	<view class="about-wrapper">
		<view class="header-bg" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-bar">
				<text class="back-icon" @tap="goBack">←</text>
				<text class="nav-title">关于我们</text>
				<view class="placeholder"></view>
			</view>

			<view class="brand-info">
				<view class="logo-box">
					<text class="logo-text">C</text>
					<view class="logo-dot"></view>
				</view>
				<view class="app-name">校园二手交易市场</view>
				<view class="version">Version 1.2.5</view>
			</view>
		</view>

		<view class="content-body">
			<view class="info-card primary-card">
				<view class="card-header">
					<view class="title-line"></view>
					<text class="title-text">平台使命</text>
				</view>
				<view class="card-desc">
					在这里，我们不仅交易物品，更在传递信任。致力于为在校生打造一个<text class="highlight">纯净、安全、绿色</text>的闲置循环社区。
				</view>
			</view>

			<view class="feature-card">
				<view class="feature-item">
					<view class="f-icon">🛡️</view>
					<text class="f-title">实名认证</text>
					<text class="f-sub">仅限在校学生</text>
				</view>
				<view class="feature-item">
					<view class="f-icon">🤝</view>
					<text class="f-title">校内面交</text>
					<text class="f-sub">安全零距离</text>
				</view>
				<view class="feature-item">
					<view class="f-icon">♻️</view>
					<text class="f-title">绿色生活</text>
					<text class="f-sub">让闲置发光</text>
				</view>
			</view>

			<view class="list-card">
				<view class="list-item" @tap="toTerms">
					<view class="left">用户协议</view>
					<view class="right">
						<text class="arrow">></text>
					</view>
				</view>
				<view class="list-item" @tap="toPrivacy">
					<view class="left">隐私政策</view>
					<view class="right">
						<text class="arrow">></text>
					</view>
				</view>
				<view class="list-item no-border">
					<view class="left">官方合作</view>
					<view class="right">
						<text class="value">collaboration@campus.edu</text>
					</view>
				</view>
			</view>

			<view class="footer">
				<view class="footer-line">
					<text>由校园开发者团队诚意出品</text>
				</view>
				<view class="copyright">Copyright © 2026 Campus Trade.</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;

	const goBack = () => uni.navigateBack();

	const toTerms = () => {
		uni.navigateTo({
			url: '/pages/common/terms'
		});
	};

	const toPrivacy = () => {
		uni.navigateTo({
			url: '/pages/common/privacy'
		});
	};
</script>

<style lang="scss" scoped>
	$theme: #D4FB1B; // 你的专属荧光绿

	.about-wrapper {
		min-height: 100vh;
		background-color: #F7F8FA;
	}

	/* 顶部品牌区 - 模拟淘宝的高级深色背板 */
	.header-bg {
		background: #d4fb1b;
		padding-bottom: 60rpx;
		border-bottom-left-radius: 60rpx;
		border-bottom-right-radius: 60rpx;

		.nav-bar {
			height: 44px;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 40rpx;

			.back-icon {
				color: #fff;
				font-size: 48rpx;
			}

			.nav-title {
				color: #fff;
				font-size: 32rpx;
				font-weight: 500;
			}

			.placeholder {
				width: 48rpx;
			}
		}

		.brand-info {
			display: flex;
			flex-direction: column;
			align-items: center;
			margin-top: 40rpx;

			.logo-box {
				width: 140rpx;
				height: 140rpx;
				background: #fff;
				border-radius: 35rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				position: relative;

				.logo-text {
					font-size: 70rpx;
					color: #000;
					font-weight: 900;
				}

				.logo-dot {
					position: absolute;
					right: 25rpx;
					bottom: 25rpx;
					width: 15rpx;
					height: 15rpx;
					background: $theme;
					border-radius: 50%;
				}
			}

			.app-name {
				color: #fff;
				font-size: 36rpx;
				font-weight: bold;
				margin-top: 24rpx;
			}

			.version {
				color: rgba(255, 255, 255, 0.4);
				font-size: 22rpx;
				margin-top: 8rpx;
			}
		}
	}

	.content-body {
		margin-top: -40rpx;
		padding: 0 30rpx;
	}

	/* 核心使命卡片 */
	.info-card {
		background: #fff;
		border-radius: 30rpx;
		padding: 40rpx;
		box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.03);
		margin-bottom: 20rpx;

		.card-header {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;

			.title-line {
				width: 8rpx;
				height: 32rpx;
				background: $theme;
				border-radius: 4rpx;
				margin-right: 16rpx;
			}

			.title-text {
				font-size: 30rpx;
				font-weight: bold;
				color: #333;
			}
		}

		.card-desc {
			font-size: 26rpx;
			color: #666;
			line-height: 1.8;

			.highlight {
				color: #000;
				font-weight: bold;
				margin: 0 6rpx;
			}
		}
	}

	/* 优势矩阵 */
	.feature-card {
		display: flex;
		justify-content: space-around;
		background: #fff;
		border-radius: 30rpx;
		padding: 40rpx 20rpx;
		margin-bottom: 20rpx;

		.feature-item {
			display: flex;
			flex-direction: column;
			align-items: center;

			.f-icon {
				font-size: 50rpx;
				margin-bottom: 12rpx;
			}

			.f-title {
				font-size: 26rpx;
				font-weight: bold;
				color: #333;
			}

			.f-sub {
				font-size: 20rpx;
				color: #999;
				margin-top: 4rpx;
			}
		}
	}

	/* 列表入口 */
	.list-card {
		background: #fff;
		border-radius: 30rpx;
		padding: 10rpx 30rpx;

		.list-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 35rpx 0;
			border-bottom: 1rpx solid #F5F6F7;

			&.no-border {
				border-bottom: none;
			}

			.left {
				font-size: 28rpx;
				color: #333;
			}

			.right {
				display: flex;
				align-items: center;

				.arrow {
					color: #CCC;
					font-size: 24rpx;
				}

				.value {
					font-size: 26rpx;
					color: #999;
				}
			}
		}
	}

	.footer {
		padding: 60rpx 0 100rpx;
		text-align: center;

		.footer-line {
			font-size: 22rpx;
			color: #BBB;
			margin-bottom: 10rpx;
		}

		.copyright {
			font-size: 20rpx;
			color: #CCC;
			letter-spacing: 2rpx;
		}
	}
</style>