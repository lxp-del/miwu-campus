<template>
	<view class="user-container">
		<image class="page-bg" src="/static/user-back.png" mode="aspectFill"></image>

		<view class="content">
			<view class="header-section">
				<view class="avatar-wrapper">
					<image class="avatar" :src="userInfo.avatar" mode="aspectFill"></image>
					<view class="vip-tag">V</view>
				</view>
				<view class="user-info">
					<view class="name-row">
						<text class="username">{{ userInfo.username }}</text>
						<view class="level-icon">💎 Premium</view>
					</view>
					<view class="reputation-box">
						<text class="reputation-label">信誉度</text>
						<text class="reputation-value">{{ userInfo.reputation }}</text>
					</view>
				</view>
				<view class="setting-icon">⚙️</view>
			</view>

			<view class="tabs-wrapper">
				<view class="tabs-nav">
					<view class="tab-item" v-for="(item, index) in tabs" :key="index" @click="switchTab(index)">
						<text :class="['tab-text', activeTab === index ? 'active' : '']">{{ item }}</text>
					</view>
					<view class="jelly-line" :style="{ left: sliderPosition }"></view>
				</view>
			</view>

			<view class="main-card">

				<view class="quick-actions">
					<view class="action-item" v-for="(item, index) in quickActions" :key="index">
						<view class="icon-circle" :style="{ background: item.bgColor }">
							<text class="emoji">{{ item.emoji }}</text>
						</view>
						<text class="action-text">{{ item.text }}</text>
					</view>
				</view>

				<view class="promo-card-3d">
					<view class="promo-inner">
						<view class="promo-text">
							<view class="main-tip">限定活动 <text class="highlight">HOT</text></view>
							<view class="sub-tip">邀请好友领取专属皮肤</view>
						</view>
						<view class="promo-btn">立即前往</view>
					</view>
				</view>

				<view class="menu-list">
					<view class="menu-item" v-for="(item, index) in menuList" :key="index">
						<view class="menu-left">
							<view class="menu-icon-box">{{ item.icon }}</view>
							<text class="menu-text">{{ item.text }}</text>
						</view>
						<view class="menu-right">
							<view v-if="item.dot" class="update-dot"></view>
							<text class="arrow-text">></text>
						</view>
					</view>
				</view>

				<view class="footer-action">
					<view class="logout-button">切换账号</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// Localized user data object as requested
				userInfo: {
					avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix',
					username: '视觉合伙人',
					reputation: '极好 (99.9)',
				},
				tabs: ['基本资料', '我的关注', '关注我的'],
				activeTab: 0,
				quickActions: [{
						text: '我的动态',
						emoji: '🌟',
						bgColor: '#F0F9FF'
					},
					{
						text: '我的收藏',
						emoji: '💖',
						bgColor: '#FFF1F2'
					},
					{
						text: '浏览记录',
						emoji: '🕒',
						bgColor: '#F0FDF4'
					},
					{
						text: '红包福利',
						emoji: '🧧',
						bgColor: '#FFFBEB'
					}
				],
				menuList: [{
						icon: '📱',
						text: '我的动态'
					},
					{
						icon: '🔄',
						text: '检查更新',
						dot: true
					},
					{
						icon: '💬',
						text: '意见反馈'
					},
					{
						icon: '📜',
						text: '用户协议'
					},
					{
						icon: '🛡️',
						text: '隐私政策'
					},
					{
						icon: '🏢',
						text: '关于我们'
					}
				]
			};
		},
		computed: {
			sliderPosition() {
				// Calculate percentage and offset for the jelly bar
				const step = 100 / this.tabs.length;
				return (step * this.activeTab + step / 2) + '%';
			}
		},
		methods: {
			switchTab(index) {
				this.activeTab = index;
			}
		}
	}
</script>

<style lang="scss" scoped>
	$theme-color: #D4FB1B; // Your specified theme color
	$bg-main: #F8F9FB;

	.user-container {
		width: 100%;
		min-height: 100vh;
		background-color: $bg-main;

		.page-bg {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 550rpx;
			z-index: 0;
		}

		.content {
			position: relative;
			z-index: 1;
			padding-top: env(safe-area-inset-top); // Header padding for notch screens
		}
	}

	/* 1. Header Layout */
	.header-section {
		display: flex;
		align-items: center;
		padding: 60rpx 40rpx 40rpx;

		.avatar-wrapper {
			position: relative;

			.avatar {
				width: 140rpx;
				height: 140rpx;
				border-radius: 44rpx;
				border: 6rpx solid #fff;
				box-shadow: 0 12rpx 24rpx rgba(0, 0, 0, 0.1);
			}

			.vip-tag {
				position: absolute;
				bottom: -8rpx;
				right: -8rpx;
				background: $theme-color;
				font-size: 20rpx;
				font-weight: bold;
				color: #000;
				padding: 4rpx 12rpx;
				border-radius: 12rpx;
				box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.15);
			}
		}

		.user-info {
			flex: 1;
			margin-left: 32rpx;

			.name-row {
				display: flex;
				align-items: center;

				.username {
					font-size: 42rpx;
					font-weight: 800;
					color: #1A1A1A;
				}

				.level-icon {
					margin-left: 16rpx;
					font-size: 20rpx;
					background: rgba(0, 0, 0, 0.06);
					color: #555;
					padding: 4rpx 14rpx;
					border-radius: 100rpx;
				}
			}

			.reputation-box {
				margin-top: 12rpx;
				display: flex;
				align-items: center;

				.reputation-label {
					font-size: 24rpx;
					color: #777;
				}

				.reputation-value {
					font-size: 24rpx;
					color: #27AE60;
					font-weight: 600;
					margin-left: 10rpx;
				}
			}
		}

		.setting-icon {
			font-size: 48rpx;
			color: #333;
			opacity: 0.7;
		}
	}

	/* 2. Jelly Tabs */
	.tabs-wrapper {
		padding: 0 20rpx;
		margin-bottom: -2rpx; // Connect with card

		.tabs-nav {
			display: flex;
			position: relative;
			padding-bottom: 24rpx;

			.tab-item {
				flex: 1;
				text-align: center;
				z-index: 2;

				.tab-text {
					font-size: 28rpx;
					color: #666;
					transition: all 0.3s;

					&.active {
						font-size: 32rpx;
						font-weight: bold;
						color: #000;
					}
				}
			}

			.jelly-line {
				position: absolute;
				bottom: 0;
				width: 48rpx;
				height: 8rpx;
				background: $theme-color;
				border-radius: 10rpx;
				transform: translateX(-50%);
				/* Jelly physics animation curve */
				transition: left 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
				box-shadow: 0 4rpx 10rpx rgba(212, 251, 27, 0.5);
			}
		}
	}

	/* 3. Main Card Container */
	.main-card {
		background: #ffffff;
		border-radius: 60rpx 60rpx 0 0;
		padding: 60rpx 40rpx env(safe-area-inset-bottom);
		min-height: 900rpx;
		box-shadow: 0 -12rpx 40rpx rgba(0, 0, 0, 0.04);

		/* 4-Icon Grid */
		.quick-actions {
			display: flex;
			justify-content: space-between;
			margin-bottom: 50rpx;

			.action-item {
				display: flex;
				flex-direction: column;
				align-items: center;

				.icon-circle {
					width: 100rpx;
					height: 100rpx;
					border-radius: 36rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					margin-bottom: 16rpx;

					.emoji {
						font-size: 46rpx;
					}
				}

				.action-text {
					font-size: 24rpx;
					color: #444;
					font-weight: 500;
				}
			}
		}

		/* Premium 3D Card */
		.promo-card-3d {
			background: #1C1C1E;
			border-radius: 36rpx;
			margin-bottom: 50rpx;
			padding: 4rpx;
			box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.15);

			.promo-inner {
				background: linear-gradient(135deg, #2C2C2E, #1C1C1E);
				border-radius: 34rpx;
				padding: 34rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;
				border: 1px solid rgba(255, 255, 255, 0.08);
			}

			.promo-text {
				.main-tip {
					color: #FFFFFF;
					font-size: 32rpx;
					font-weight: bold;

					.highlight {
						background: #FF3B30;
						font-size: 18rpx;
						padding: 4rpx 10rpx;
						border-radius: 8rpx;
						margin-left: 12rpx;
						vertical-align: middle;
					}
				}

				.sub-tip {
					color: rgba(255, 255, 255, 0.5);
					font-size: 22rpx;
					margin-top: 10rpx;
				}
			}

			.promo-btn {
				background: $theme-color;
				color: #000;
				font-size: 24rpx;
				font-weight: 800;
				padding: 14rpx 28rpx;
				border-radius: 18rpx;
			}
		}

		/* List Items */
		.menu-list {
			.menu-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 36rpx 0;
				border-bottom: 1px solid #F2F2F7;

				&:last-child {
					border-bottom: none;
				}

				.menu-left {
					display: flex;
					align-items: center;

					.menu-icon-box {
						font-size: 38rpx;
						width: 50rpx;
					}

					.menu-text {
						font-size: 30rpx;
						color: #1C1C1E;
						margin-left: 24rpx;
					}
				}

				.menu-right {
					display: flex;
					align-items: center;

					.update-dot {
						width: 14rpx;
						height: 14rpx;
						background: #FF3B30;
						border-radius: 50%;
						margin-right: 20rpx;
					}

					.arrow-text {
						color: #C7C7CC;
						font-size: 32rpx;
						font-weight: 300;
					}
				}
			}
		}

		/* Bottom Area */
		.footer-action {
			margin-top: 60rpx;

			.logout-button {
				width: 100%;
				height: 104rpx;
				background: #F2F2F7;
				border-radius: 32rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				color: #FF3B30;
				font-size: 30rpx;
				font-weight: bold;

				&:active {
					opacity: 0.7;
				}
			}
		}
	}
</style>