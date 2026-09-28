<template>
	<view class="order-container">
		<view class="custom-navbar" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-content">
				<view class="nav-left" @tap="goBack">
					<text class="iconfont icon-back">←</text>
				</view>
				<view class="nav-center">
					<text class="nav-title">全部订单</text>
				</view>
				<view class="nav-right"></view>
			</view>
		</view>

		<scroll-view scroll-y class="order-scroll" :style="{ marginTop: (statusBarHeight + 44) + 'px' }"
			@scrolltolower="onReachBottom">
			<view v-if="orderList.length > 0" class="list-wrapper">
				<view class="order-card" v-for="(item, index) in orderList" :key="index">
					<view class="order-head">
						<view class="shop-info">
							<text class="shop-icon">🏪</text>
							<text class="shop-name">校园精品自营</text>
							<text class="arrow">></text>
						</view>
						<text class="order-status">交易完成</text>
					</view>

					<view class="order-body">
						<image :src="getFirstImage(item.imageUrls)" mode="aspectFill" class="goods-img" />
						<view class="goods-detail">
							<view class="detail-row">
								<text class="goods-title">{{ item.title }}</text>
								<view class="price-info">
									<text class="symbol">￥</text>
									<text class="price-val">{{ item.price }}</text>
								</view>
							</view>
							<view class="detail-sub-row">
								<text class="delivery-tag">48小时发货</text>
								<text class="num">x{{ item.quantity }}</text>
							</view>
						</view>
					</view>

					<view class="order-footer">
						<view class="summary">
							总计 <text class="total">￥{{ (item.price * item.quantity).toFixed(2) }}</text>
						</view>
						
						<view class="footer-bottom">
							<text class="more-action" @click="openMorePopup(item)">更多</text>
							<view class="btn-group">
								<view class="btn btn-plain" @click="contactSeller(item)">联系卖家</view>
								<view class="btn btn-main" @click="buy(item)">再次购买</view>
							</view>
						</view>
					</view>
				</view>

				<view class="no-more">—— 已经到底啦 ——</view>
			</view>

			<view class="empty-box" v-else>
				<text class="empty-icon">📦</text>
				<text class="empty-text">暂时没有订单记录</text>
			</view>
		</scroll-view>

		<view class="popup-mask" v-if="isPopupVisible" @click="closeMorePopup">
			<view class="popup-content" @click.stop>
				<view class="popup-item black-text" @click="handleDeleteOrder">删除订单</view>
				<view class="popup-item black-text" @click="handleComplain">投诉卖家</view>
				<view class="popup-divider"></view>
				<view class="popup-item grey-text" @click="closeMorePopup">取消</view>
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
		getCartList
	} from '@/apis/cart/cart.js'

	const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;
	const orderList = ref([]);
	
	// 修改点1：弹窗状态与当前选中订单
	const isPopupVisible = ref(false);
	const currentOrder = ref(null);

	// 获取第一张图片：逗号分割
	const getFirstImage = (urls) => {
		if (!urls) return '';
		return urls.includes(',') ? urls.split(',')[0] : urls;
	};

	// 返回上一页
	const goBack = () => {
		uni.navigateBack();
	};

	const getList = () => {
		uni.showLoading({
			title: '加载中...',
			mask: true
		});
		getCartList().then(res => {
			if (res.data) {
				orderList.value = res.data;
			}
			uni.hideLoading();
		}).catch(() => uni.hideLoading());
	}

	const buy=(item)=>{
		uni.navigateTo({
			url: `/pages/gooddetail/index?id=${item.goodId}`
		})
	}
	
	// 修改点2：联系卖家逻辑
	const contactSeller = (item) => {
		uni.navigateTo({
			url: `/pages/chat/index?goodId=${item.goodId}`
		});
	};

	// 修改点1：弹窗及内部按钮逻辑
	const openMorePopup = (item) => {
		currentOrder.value = item;
		isPopupVisible.value = true;
	};

	const closeMorePopup = () => {
		isPopupVisible.value = false;
		currentOrder.value = null;
	};

	const handleDeleteOrder = () => {
		uni.showToast({
			title: '删除成功',
			icon: 'none'
		});
		closeMorePopup();
	};

	const handleComplain = () => {
		if (currentOrder.value) {
			uni.navigateTo({
				url: `/pages/complain/index?goodId=${currentOrder.value.goodId}`
			});
		}
		closeMorePopup();
	};

	onLoad(() => {
		getList();
	})
</script>

<style lang="scss" scoped>
	$theme: #D4FB1B; // 你的荧光绿主题色

	.order-container {
		min-height: 100vh;
		background-color: #F6F6F6;
	}

	/* 自定义导航栏 */
	.custom-navbar {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		background: rgba(255, 255, 255, 0.9);
		backdrop-filter: blur(10px);
		z-index: 999;

		.nav-content {
			height: 44px;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 30rpx;

			.nav-left {
				width: 60rpx;

				.icon-back {
					font-size: 44rpx;
					font-weight: bold;
					color: #333;
				}
			}

			.nav-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333;
			}

			.nav-right {
				width: 60rpx;
			}
		}
	}

	.list-wrapper {
		padding: 20rpx;
	}

	/* 淘宝卡片样式 */
	.order-card {
		background: #fff;
		border-radius: 24rpx;
		padding: 24rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.02);

		.order-head {
			display: flex;
			justify-content: space-between;
			margin-bottom: 24rpx;

			.shop-info {
				display: flex;
				align-items: center;

				.shop-name {
					font-size: 26rpx;
					font-weight: bold;
					margin: 0 8rpx;
					color: #333;
				}

				.arrow {
					font-size: 20rpx;
					color: #ccc;
				}
			}

			.order-status {
				font-size: 24rpx;
				color: #FF5000;
			}
		}

		.order-body {
			display: flex;
			gap: 20rpx;

			.goods-img {
				width: 160rpx;
				height: 160rpx;
				border-radius: 16rpx;
				background: #f8f8f8;
			}

			.goods-detail {
				flex: 1;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.detail-row {
					display: flex;
					justify-content: space-between;

					.goods-title {
						font-size: 28rpx;
						color: #333;
						width: 75%;
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2;
						overflow: hidden;
					}

					.price-info {
						text-align: right;

						.symbol {
							font-size: 20rpx;
						}

						.price-val {
							font-size: 28rpx;
							font-weight: 500;
						}
					}
				}

				.detail-sub-row {
					display: flex;
					justify-content: space-between;
					font-size: 22rpx;
					color: #999;
					align-items: center;

					/* 修改点3：发货标签样式替换原默认规格样式 */
					.delivery-tag {
						color: #FF0000;
						border: 1rpx solid #FF0000;
						padding: 4rpx 10rpx;
						border-radius: 4rpx;
						background: transparent;
					}
				}
			}
		}

		.order-footer {
			margin-top: 24rpx;

			.summary {
				text-align: right;
				font-size: 24rpx;
				color: #666;

				.total {
					font-size: 32rpx;
					color: #333;
					font-weight: bold;
					margin-left: 6rpx;
				}
			}
			
			/* 修改点1：底部区域重新布局，容纳左下角的“更多”文字 */
			.footer-bottom {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-top: 20rpx;
				
				.more-action {
					font-size: 24rpx;
					color: #999;
					padding: 10rpx 0;
				}

				.btn-group {
					display: flex;
					justify-content: flex-end;
					gap: 16rpx;
	
					.btn {
						padding: 12rpx 32rpx;
						border-radius: 40rpx;
						font-size: 24rpx;
	
						&.btn-plain {
							border: 1rpx solid #ddd;
							color: #666;
						}
	
						&.btn-main {
							background: $theme;
							color: #000;
							font-weight: bold;
							box-shadow: 0 4rpx 10rpx rgba(212, 251, 27, 0.2);
						}
					}
				}
			}
		}
	}

	.no-more {
		text-align: center;
		font-size: 22rpx;
		color: #ccc;
		padding: 30rpx 0;
	}

	.empty-box {
		padding-top: 300rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		color: #ccc;

		.empty-icon {
			font-size: 80rpx;
			margin-bottom: 20rpx;
		}
	}

	/* 修改点1：底部弹窗样式 */
	.popup-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		z-index: 1000;
		display: flex;
		flex-direction: column;
		justify-content: flex-end;
		
		.popup-content {
			background: #fff;
			border-radius: 24rpx 24rpx 0 0;
			padding-bottom: env(safe-area-inset-bottom);
			
			.popup-item {
				height: 110rpx;
				line-height: 110rpx;
				text-align: center;
				font-size: 30rpx;
				border-bottom: 1rpx solid #f5f5f5;
				background: #fff;
				
				&:first-child {
					border-radius: 24rpx 24rpx 0 0;
				}
			}
			
			.black-text {
				color: #333;
			}
			
			.grey-text {
				color: #999;
				border-bottom: none;
			}
			
			.popup-divider {
				height: 16rpx;
				background: #f6f6f6;
			}
		}
	}
</style>