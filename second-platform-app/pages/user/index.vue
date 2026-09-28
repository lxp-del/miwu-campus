<template>
	<view class="user-container">
		<image class="page-bg" src="/static/user-back.png" mode="aspectFill"></image>

		<view class="content">
			<view class="header-section">
				<image class="avatar" :src="userInfo.avatar" mode="aspectFill"></image>
				<view class="user-info">
					<view class="name-row">
						<text class="username">{{ userInfo.name }}</text>
						<view class="vip-icon">💎</view>
					</view>
					<text class="reputation">信誉度：{{ userInfo.reputation }}</text>
				</view>
			</view>



			<view class="main-card">

				<view class="quick-actions">
					<view class="action-item" v-for="(item, index) in quickActions" :key="index" @click="select(item)">
						<view class="icon-box" :class="'icon-' + index">
							<text class="icon-emoji">{{ item.emoji }}</text>
						</view>
						<text class="action-text">{{ item.text }}</text>
					</view>
				</view>

				<view class="menu-list">
					<view class="menu-item" v-for="(item, index) in menuList" :key="index" @click="go(item)">
						<view class="menu-left">
							<text class="menu-text">{{ item.text }}</text>
						</view>
						<view class="menu-right">
							<view v-if="item.extra" class="extra-info">
								<view v-if="item.dot" class="dot"></view>
								<text class="extra-text">{{ item.extra }}</text>
							</view>
							<text class="arrow">></text>
						</view>
					</view>
				</view>

				<view class="logout-btn" @click="qiehuan">
					<text>换账号登录</text>
				</view>

			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		computed
	} from 'vue'
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app'

	import {
		getUserInfo
	} from '@/apis/user/userinfo.js'

	// 响应式数据定义
	const userInfo = ref({
		avatar: '',
		name: '',
		reputation: '',
	})

	const tabs = ref(['基本资料'])
	const activeTab = ref(0)

	const quickActions = ref([{
			text: '下单记录',
			emoji: '🎟'
		},
		{
			text: '收货地址',
			emoji: '🔖'
		},
		{
			text: '我的收藏',
			emoji: '🎟️'
		},
		{
			text: '我的钱包',
			emoji: '🪙'
		},
	])

	const menuList = ref([{
			icon: '🚀',
			text: '个人信息'
		},
		{
			icon: '⬆️',
			text: '检查更新',
			extra: 'V1.0.0',
			dot: true
		},
		{
			icon: '📝',
			text: '意见反馈'
		},
		{
			icon: '📄',
			text: '用户协议'
		},
		{
			icon: '🛡️',
			text: '隐私政策'
		},
		{
			icon: 'ℹ️',
			text: '关于我们'
		},
	])

	const go = (item) => {
		if (item.text === '个人信息') {
			uni.navigateTo({
				url: '/pages/gerenxinxi/index'
			});
		}
		if (item.text === '用户协议') {
			uni.navigateTo({
				url: '/pages/yonghuxieyi/index'
			});
		}
		if (item.text === '隐私政策') {
			uni.navigateTo({
				url: '/pages/yszc/index'
			});
		}
		if (item.text === '关于我们') {
			uni.navigateTo({
				url: '/pages/about/index'
			});
		}
	}

	const select = (item) => {
		if (item.text === '下单记录') {
			uni.navigateTo({
				url: '/pages/mycart/index'
			});
		}
		if (item.text === '收货地址') {
			uni.navigateTo({
				url: '/pages/dizhi/index'
			});
		}
		if (item.text === '我的钱包') {
			uni.navigateTo({
				url: '/pages/money/index'
			});
		}
	}

	const qiehuan = () => {
		uni.navigateTo({
			url: '/pages/login/index'
		});
	}

	// 方法
	const switchTab = (index) => {
		activeTab.value = index
	}

	// 获取列表数据的方法
	const getList = async () => {
		try {
			const res = await getUserInfo();
			// 检查响应数据结构
			if (res && res.data) {
				userInfo.value = res.data;
				userInfo.value.reputation = 99
			} else {
				uni.showToast({
					title: '获取信息失败',
					icon: 'none'
				});
			}
		} catch (error) {
			uni.showToast({
				title: '网络错误，请稍后重试',
				icon: 'none'
			});
		}
	};

	// 页面生命周期 onLoad
	onShow(() => {
		getList()
	})
</script>

<style lang="scss" scoped>
	/* 页面基础配置 */
	page {
		background-color: #F7F8FC;
	}

	/* 主题变量 */
	$theme-color: #D4FB1B;
	$text-main: #333333;
	$text-sub: #999999;

	.user-container {
		position: relative;
		width: 100%;
		min-height: 100vh;
		overflow: hidden;

		.page-bg {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 50vh;
			z-index: 0;
			opacity: 0.8;
		}

		.content {
			position: relative;
			z-index: 1;
			padding-top: 100rpx;
		}
	}

	/* 1. 顶部用户信息 */
	.header-section {
		display: flex;
		align-items: center;
		padding: 0 40rpx;
		margin-bottom: 50rpx;

		.avatar {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
			border: 4rpx solid rgba(255, 255, 255, 0.8);
			box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		}

		.user-info {
			flex: 1;
			margin-left: 30rpx;
			display: flex;
			flex-direction: column;
			justify-content: center;

			.name-row {
				display: flex;
				align-items: center;

				.username {
					font-size: 40rpx;
					font-weight: bold;
					color: $text-main;
				}

				.vip-icon {
					margin-left: 10rpx;
					font-size: 24rpx;
					background: rgba(212, 251, 27, 0.3);
					padding: 4rpx 8rpx;
					border-radius: 20rpx;
				}
			}

			.reputation {
				margin-top: 10rpx;
				font-size: 26rpx;
				color: #666666;
			}
		}


	}

	/* 2. 标签栏 */
	.tabs-section {
		display: flex;
		position: relative;
		padding: 0 20rpx;
		margin-bottom: 0;

		.tab-item {
			flex: 1;
			text-align: center;
			padding-bottom: 30rpx;
			z-index: 2;

			.tab-text {
				font-size: 30rpx;
				color: #666;
				transition: all 0.3s ease;

				&.active {
					font-size: 34rpx;
					font-weight: bold;
					color: $text-main;
				}
			}
		}

		.curved-triangle {
			position: absolute;
			bottom: -19rpx;
			width: 32rpx;
			height: 32rpx;
			background-color: #FFFFFF;
			border-radius: 6rpx;
			transform: translateX(-50%) rotate(45deg);
			transition: left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
			z-index: 1;
			box-shadow: -2rpx -2rpx 4rpx rgba(0, 0, 0, 0.02);
		}
	}

	/* 3. 主体卡片 */
	.main-card {
		background-color: #FFFFFF;
		border-top-left-radius: 50rpx;
		border-top-right-radius: 50rpx;
		min-height: 60vh;
		padding: 50rpx 40rpx;
		position: relative;
		z-index: 2;
		box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.03);

		.quick-actions {
			display: flex;
			justify-content: space-between;
			margin-bottom: 50rpx;

			.action-item {
				display: flex;
				flex-direction: column;
				align-items: center;

				.icon-box {
					width: 90rpx;
					height: 90rpx;
					border-radius: 28rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					margin-bottom: 16rpx;
					background: #F8F8F8;

					.icon-emoji {
						font-size: 44rpx;
					}
				}

				.icon-0 {
					background: linear-gradient(135deg, #FFE6E6, #FFCACA);
				}

				.icon-1 {
					background: linear-gradient(135deg, #E6F3FF, #BFE1FF);
				}

				.icon-2 {
					background: linear-gradient(135deg, #F3E6FF, #DDBFFF);
				}

				.icon-3 {
					background: linear-gradient(135deg, #FFF5E6, #FFE1B3);
				}

				.action-text {
					font-size: 26rpx;
					color: $text-main;
					font-weight: 500;
				}
			}
		}

		/* --- 3D 营销Banner 样式 (已更新) --- */


		.menu-list {
			margin-bottom: 60rpx;

			.menu-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 36rpx 0;
				border-bottom: 2rpx solid #F5F5F5;

				&:last-child {
					border-bottom: none;
				}

				.menu-left {
					display: flex;
					align-items: center;

					.menu-icon {
						font-size: 40rpx;
						margin-right: 24rpx;
						color: #888;
					}

					.menu-text {
						font-size: 30rpx;
						color: $text-main;
						font-weight: 500;
					}
				}

				.menu-right {
					display: flex;
					align-items: center;

					.extra-info {
						display: flex;
						align-items: center;
						margin-right: 16rpx;

						.dot {
							width: 12rpx;
							height: 12rpx;
							background-color: #FF4D4F;
							border-radius: 50%;
							margin-right: 10rpx;
						}

						.extra-text {
							font-size: 26rpx;
							color: $text-sub;
						}
					}

					.arrow {
						font-size: 32rpx;
						color: #CCCCCC;
						font-weight: 300;
					}
				}
			}
		}

		.logout-btn {
			width: 80%;
			margin: 0 auto;
			padding: 28rpx 0;
			background: linear-gradient(135deg, $theme-color, darken($theme-color, 10%));
			border-radius: 50rpx;
			text-align: center;
			box-shadow: 0 12rpx 24rpx rgba(212, 251, 27, 0.3);

			text {
				font-size: 32rpx;
				color: #333333;
				font-weight: bold;
				letter-spacing: 2rpx;
			}

			&:active {
				opacity: 0.8;
				transform: scale(0.98);
			}
		}
	}
</style>