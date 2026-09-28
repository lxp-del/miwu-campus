<template>
	<view v-if="visible" class="purchase-popup-mask" @click="close" :class="{ 'mask-show': animation }">
		<view class="popup-content" @click.stop :class="{ 'content-show': animation }">
			<view class="credit-header">
				<text class="credit-icon">🛡️</text>
				<text class="credit-text">卖家信用极好</text>
			</view>

			<view class="product-summary">
				<image class="prod-img" :src="productData.images[0]" mode="aspectFill"></image>
				<view class="prod-info">
					<view class="prod-title">{{ productData.title }}</view>
					<view class="prod-price">
						<text class="unit">¥</text>
						<text class="val">{{ productData.price }}</text>
					</view>
				</view>
			</view>



			<view class="address-card" @click="handleAddressClick">
				<view class="card-body">
					<view class="user-line">
						<text class="user-name">{{ addressData.adrName }}</text>
						<text class="user-phone">{{ addressData.adrPhone }}</text>
					</view>
					<view class="address-detail">{{ addressData.adrDetail }}</view>
				</view>
				<text class="arrow">›</text>
			</view>
			<view class="quantity-card">
				<text class="label">购买数量</text>
				<view class="stepper">
					<view class="minus" :class="{ disabled: quantity <= 1 }" @click="changeQty(-1)">-</view>
					<input class="input" type="number" v-model="quantity" @blur="onQtyBlur" />
					<view class="plus" @click="changeQty(1)">+</view>
				</view>
			</view>
			<view class="info-row">
				<text class="label">运费</text>
				<text class="value">包邮</text>
			</view>

			<view class="pay-section">
				<radio-group @change="onPayChange">
					<label class="pay-item">
						<view class="pay-left">
							<text class="pay-icon alipay">支</text>
							<text>支付宝支付</text>
						</view>
						<radio value="alipay" :checked="payType === 'alipay'" color="#D4FB1B" />
					</label>
					<label class="pay-item">
						<view class="pay-left">
							<text class="pay-icon wechat">微</text>
							<text>微信支付</text>
						</view>
						<radio value="wechat" :checked="payType === 'wechat'" color="#D4FB1B" />
					</label>
				</radio-group>
			</view>

			<view class="bottom-bar">
				<button class="confirm-btn" @click="handleConfirm">确认下单</button>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		watch
	} from 'vue';

	const props = defineProps({
		modelValue: Boolean,
		productData: {
			type: Object,
			default: () => ({
				title: '',
				price: '0.00',
				images: []
			})
		}
	});

	const emit = defineEmits(['update:modelValue', 'confirm']);

	// --- 状态定义 ---
	const visible = ref(false);
	const animation = ref(false);
	const payType = ref('alipay');
	const quantity = ref(1); // 购买数量

	// 地址基础数据（用于内部展示）
	const addressData = ref({
		adrName: '张三',
		adrPhone: '138****8888',
		adrDetail: '广东省广州市天河区珠江新城某某大厦 B 座 808 室',
		adrId: 'mock_123'
	});

	// --- 显隐动画 ---
	watch(() => props.modelValue, (newVal) => {
		if (newVal) {
			visible.value = true;
			setTimeout(() => {
				animation.value = true;
			}, 50);
		} else {
			animation.value = false;
			setTimeout(() => {
				visible.value = false;
			}, 300);
		}
	});

	const close = () => emit('update:modelValue', false);

	// --- 数量逻辑 ---
	const changeQty = (val) => {
		const res = quantity.value + val;
		if (res >= 1) quantity.value = res;
	};

	const onQtyBlur = () => {
		if (!quantity.value || quantity.value < 1) quantity.value = 1;
	};

	const onPayChange = (e) => {
		payType.value = e.detail.value;
	};

	const handleAddressClick = () => {
		uni.navigateTo({
			url: '/pages/address/edit'
		});
	};

	// --- 指令2：修改后的返回逻辑 ---
	const handleConfirm = () => {
		const orderPayload = {
			payType: payType.value,
			adrId: addressData.value.adrId, // 只取 ID
			quantity: quantity.value // 包含数量
		};

		emit('confirm', orderPayload);
		close();
	};
</script>

<style lang="scss" scoped>
	.purchase-popup-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.6);
		z-index: 1000;
		opacity: 0;
		transition: opacity 0.3s;
		display: flex;
		align-items: flex-end;

		&.mask-show {
			opacity: 1;
		}
	}

	.popup-content {
		width: 100%;
		background: #FFFFFF;
		border-radius: 32rpx 32rpx 0 0;
		padding: 40rpx 30rpx calc(40rpx + env(safe-area-inset-bottom));
		transform: translateY(100%);
		transition: transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

		&.content-show {
			transform: translateY(0);
		}
	}

	.credit-header {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-bottom: 30rpx;

		.credit-text {
			font-size: 24rpx;
			color: #D2691E;
			font-weight: bold;
			margin-left: 8rpx;
		}
	}

	.product-summary {
		display: flex;
		margin-bottom: 30rpx;

		.prod-img {
			width: 140rpx;
			height: 140rpx;
			border-radius: 12rpx;
			flex-shrink: 0;
			background: #f9f9f9;
		}

		.prod-info {
			margin-left: 20rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.prod-title {
				font-size: 28rpx;
				font-weight: bold;
				color: #333;
				line-height: 1.4;
			}

			.prod-price {
				color: #FF4A4A;

				.unit {
					font-size: 24rpx;
				}

				.val {
					font-size: 38rpx;
					font-weight: bold;
				}
			}
		}
	}

	// 数量卡片样式
	.quantity-card {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx 24rpx;
		background: #F7F8FA;
		border-radius: 20rpx;
		margin-bottom: 24rpx;

		.label {
			font-size: 28rpx;
			color: #333;
			font-weight: 500;
		}

		.stepper {
			display: flex;
			align-items: center;
			background: #fff;
			border-radius: 10rpx;
			overflow: hidden;
			border: 1rpx solid #eee;

			.minus,
			.plus {
				width: 60rpx;
				height: 60rpx;
				line-height: 56rpx;
				text-align: center;
				font-size: 36rpx;
				color: #333;
				background: #f9f9f9;

				&.disabled {
					color: #ccc;
				}

				&:active:not(.disabled) {
					background: #eee;
				}
			}

			.input {
				width: 80rpx;
				height: 60rpx;
				text-align: center;
				font-size: 28rpx;
				border-left: 1rpx solid #eee;
				border-right: 1rpx solid #eee;
			}
		}
	}

	.address-card {
		background: #F7F8FA;
		border-radius: 20rpx;
		padding: 24rpx;
		display: flex;
		align-items: center;
		margin-bottom: 24rpx;

		.card-body {
			flex: 1;

			.user-line {
				margin-bottom: 4rpx;

				.user-name {
					font-weight: bold;
					font-size: 28rpx;
					margin-right: 16rpx;
				}

				.user-phone {
					color: #666;
					font-size: 26rpx;
				}
			}

			.address-detail {
				font-size: 24rpx;
				color: #999;
				line-height: 1.4;
			}
		}

		.arrow {
			font-size: 40rpx;
			color: #CCC;
			margin-left: 20rpx;
		}
	}

	.info-row {
		display: flex;
		justify-content: space-between;
		padding: 24rpx 10rpx;
		border-bottom: 1rpx solid #F0F0F0;

		.label {
			font-size: 28rpx;
			color: #333;
		}

		.value {
			font-size: 28rpx;
			color: #999;
		}
	}

	.pay-section {
		margin-top: 10rpx;

		.pay-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 24rpx 10rpx;

			.pay-left {
				display: flex;
				align-items: center;
				font-size: 28rpx;

				.pay-icon {
					width: 40rpx;
					height: 40rpx;
					line-height: 40rpx;
					text-align: center;
					border-radius: 8rpx;
					margin-right: 20rpx;
					font-size: 22rpx;
					color: #fff;

					&.alipay {
						background: #00A0E9;
					}

					&.wechat {
						background: #09BB07;
					}
				}
			}
		}
	}

	.bottom-bar {
		margin-top: 40rpx;

		.confirm-btn {
			background: #D4FB1B;
			color: #000;
			height: 96rpx;
			line-height: 96rpx;
			border-radius: 48rpx;
			font-weight: bold;
			font-size: 32rpx;

			&::after {
				border: none;
			}
		}
	}
</style>