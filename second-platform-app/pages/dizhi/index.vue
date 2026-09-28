<template>
	<view class="address-page">
		<view class="nav-bar">
			<view class="nav-left" @tap="goBack">
				<text class="iconfont icon-back">←</text>
			</view>
			<text class="nav-title">收货地址</text>
			<text class="manage-btn" @tap="isManaging = !isManaging"> {{ isManaging ? '完成' : '管理' }} </text>
		</view>

		<view class="add-section" @tap="openEditDrawer(null)">
			<view class="add-btn">
				<text class="plus">+</text>
				<text>新建收货地址</text>
			</view>
		</view>

		<scroll-view scroll-y class="address-list">
			<view class="address-item" v-for="(item, index) in addressList" :key="item.id"
				@longpress="handleLongPress(item)">
				<view class="delete-icon" v-if="isManaging" @tap="deleteAddress(item)">
					<view class="minus-circle">-</view>
				</view>
				<view class="info-content">
					<view class="user-info">
						<text class="name">{{ item.name }}</text>
						<text class="phone">{{ item.phone }}</text>
						<text class="tag" v-if="item.tag">{{ item.tag }}</text>
						<text class="default-tag" v-if="item.isDefault == 1">默认</text>
					</view>
					<view class="detail-addr">
						{{ item.region }} {{ item.detail }}
					</view>
				</view>
				<view class="edit-icon" @tap="openEditDrawer(item)"> - </view>
			</view>
			<view style="height: 50rpx;"></view>
		</scroll-view>

		<view class="mask" v-if="showDrawer" @tap="closeDrawer"></view>
		<view :class="['edit-drawer', showDrawer ? 'drawer-show' : '']">
			<view class="drawer-header">
				<text>{{ currentEditData.id ? '编辑地址' : '新建地址' }}</text>
				<text class="close-x" @tap="closeDrawer">×</text>
			</view>

			<view class="form-container">
				<view class="form-item">
					<text class="label">收货人</text>
					<input v-model="currentEditData.name" placeholder="名字" />
				</view>

				<view class="form-item">
					<text class="label">手机号码</text>
					<input v-model="currentEditData.phone" type="number" placeholder="手机号" />
				</view>

				<!-- 修改为手动输入：所在地区 -->
				<view class="form-item">
					<text class="label">所在地区</text>
					<input v-model="currentEditData.region" placeholder="例如：北京市朝阳区" class="manual-input" />
				</view>

				<!-- 修改为手动输入：详细地址 -->
				<view class="form-item">
					<text class="label">详细地址</text>
					<textarea v-model="currentEditData.detail" placeholder="如街道、楼牌号、小区名称等" auto-height
						class="manual-textarea" />
				</view>

				<view class="form-item">
					<text class="label">标签</text>
					<view class="tag-group">
						<text v-for="t in ['家', '公司', '学校']" :key="t"
							:class="['tag-opt', currentEditData.tag === t ? 'active' : '']"
							@tap="currentEditData.tag = t">{{ t }}</text>
					</view>
				</view>
			</view>

			<view class="save-box">
				<button class="save-btn" @tap="saveAddress">保存并使用</button>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		onUnmounted,
		nextTick,
		computed,
		reactive
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		addAddress,
		updateAddress,
		deleteAddresss,
		getAddressInfo,
		getAddressList,
		morenAddresss
	} from "@/apis/address/address.js"

	// 基础数据
	const currentUser = ref(uni.getStorageSync('currentUser') || {
		id: null,
		avatar: '',
		name: ''
	})
	const isManaging = ref(false);
	const showDrawer = ref(false);

	// 模拟数据
	const addressList = ref([]);
	const initialForm = {
		id: null,
		name: '',
		phone: '',
		region: '',
		detail: '',
		tag: '',
		isDefault: 0,
		latitude: '', // 保留字段，防止后端报错
		longitude: '' // 保留字段，防止后端报错
	};
	const currentEditData = reactive({
		...initialForm
	});

	const getList = () => {
		getAddressList().then(res => {
			console.log(res.data);
			addressList.value = res.data
		})
	}

	onLoad(() => {
		getList()
	})

	const goBack = () => uni.navigateBack();

	const openEditDrawer = (item) => {
		if (item) {
			Object.assign(currentEditData, item);
		} else {
			Object.assign(currentEditData, initialForm);
			currentEditData.id = null;
		}
		showDrawer.value = true;
	};

	const closeDrawer = () => {
		showDrawer.value = false;
	};

	// 移除了 chooseLocation 函数，不再需要地图选点

	const saveAddress = () => {
		if (!currentEditData.name || !currentEditData.phone || !currentEditData.region) {
			return uni.showToast({
				title: '请填写完整信息',
				icon: 'none'
			});
		}
		closeDrawer();
		if (currentEditData.id) {
			updateAddress(currentEditData).then(res => {
				uni.showToast({
					title: '保存成功',
					icon: 'success',
					duration: 2000
				});
				getList()
			})
		} else {
			addAddress(currentEditData).then(res => {
				uni.showToast({
					title: '新增成功',
					icon: 'success',
					duration: 2000
				});
				getList()
			})
		}
	};

	// 处理长按设置默认地址
	const handleLongPress = (item) => {
		if (item.isDefault == 1) return; // 已经是默认地址则不触发
		uni.showModal({
			title: '提示',
			content: '是否设为默认地址？',
			success: (res) => {
				if (res.confirm) {
					morenAddresss(item.id).then(res => {
						getList()
					})
				}
			}
		});
	};

	const deleteAddress = async (item) => {
		console.log(item);
		const index = addressList.value.findIndex(addr => addr.id === item.id);
		if (index === -1) return;
		uni.showModal({
			title: '提示',
			content: '确定删除该地址吗？',
			success: async (res) => {
				if (res.confirm) {
					try {
						// 使用重命名后的API
						await deleteAddresss(item.id);
						addressList.value.splice(index, 1);
						uni.showToast({
							title: '删除成功',
							icon: 'success',
							duration: 2000
						});
					} catch (error) {
						console.error('删除失败:', error);
						uni.showToast({
							title: '删除失败',
							icon: 'error',
							duration: 2000
						});
					}
				}
			}
		});
	};
</script>

<style lang="scss" scoped>
	$theme-color: #D4FB1B;

	.address-page {
		min-height: 100vh;
		background-color: #f7f7f7;
		padding-top: calc(var(--status-bar-height) + 100rpx);
	}

	/* 导航栏 */
	.nav-bar {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		height: 100rpx;
		background: #fff;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 30rpx;
		padding-top: var(--status-bar-height);
		z-index: 100;

		.nav-title {
			font-weight: bold;
			font-size: 32rpx;
		}

		.nav-left {
			width: 60rpx;

			.icon-back {
				font-size: 44rpx;
				font-weight: bold;
				color: #333;
			}
		}

		.manage-btn {
			font-size: 28rpx;
			color: #666;
		}

		.back-btn {
			font-size: 40rpx;
		}
	}

	/* 新增按钮区域 */
	.add-section {
		background: #fff;
		padding: 30rpx;
		margin-bottom: 20rpx;

		.add-btn {
			height: 90rpx;
			background: $theme-color;
			border-radius: 45rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			font-weight: bold;
			font-size: 30rpx;
			box-shadow: 0 4rpx 12rpx rgba(212, 251, 27, 0.3);

			.plus {
				font-size: 40rpx;
				margin-right: 10rpx;
			}
		}
	}

	/* 地址列表 */
	.address-list {
		.address-item {
			background: #fff;
			margin: 0 20rpx 20rpx;
			border-radius: 16rpx;
			padding: 30rpx;
			display: flex;
			align-items: center;

			.delete-icon {
				margin-right: 20rpx;

				.minus-circle {
					width: 36rpx;
					height: 36rpx;
					background: #ff4d4f;
					color: #fff;
					border-radius: 50%;
					text-align: center;
					line-height: 32rpx;
				}
			}

			.info-content {
				flex: 1;

				.user-info {
					display: flex;
					align-items: center;
					margin-bottom: 10rpx;

					.name {
						font-weight: bold;
						font-size: 30rpx;
						margin-right: 20rpx;
					}

					/* 默认地址标签样式 */
					.default-tag {
						margin-left: 12rpx;
						padding: 2rpx 10rpx;
						font-size: 20rpx;
						color: #ff4d4f;
						background: rgba(255, 77, 79, 0.1);
						border: 1rpx solid rgba(255, 77, 79, 0.3);
						border-radius: 6rpx;
						line-height: 1;
						display: flex;
						align-items: center;
						justify-content: center;
					}

					.phone {
						color: #999;
						font-size: 26rpx;
					}

					.tag {
						background: $theme-color;
						font-size: 20rpx;
						padding: 2rpx 12rpx;
						border-radius: 6rpx;
						margin-left: 15rpx;
					}
				}

				.detail-addr {
					font-size: 26rpx;
					color: #333;
					line-height: 1.4;
				}
			}

			.edit-icon {
				padding: 20rpx;
				color: #ccc;
				font-size: 32rpx;
			}
		}
	}

	/* 抽屉弹出层 */
	.mask {
		position: fixed;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		background: rgba(0, 0, 0, 0.5);
		z-index: 199;
	}

	.edit-drawer {
		position: fixed;
		left: 0;
		right: 0;
		bottom: -100%;
		background: #fff;
		border-radius: 30rpx 30rpx 0 0;
		z-index: 200;
		transition: bottom 0.3s ease;
		padding-bottom: 60rpx;

		&.drawer-show {
			bottom: 0;
		}

		.drawer-header {
			display: flex;
			justify-content: space-between;
			padding: 30rpx;
			border-bottom: 1rpx solid #eee;
			font-weight: bold;

			.close-x {
				font-size: 44rpx;
				color: #999;
				font-weight: normal;
			}
		}
	}

	.form-container {
		padding: 0 30rpx;

		.form-item {
			display: flex;
			padding: 30rpx 0;
			border-bottom: 1rpx solid #f5f5f5;
			align-items: flex-start;

			.label {
				width: 160rpx;
				font-size: 28rpx;
				color: #333;
			}

			input,
			.addr-picker {
				flex: 1;
				font-size: 28rpx;
			}

			textarea {
				flex: 1;
				font-size: 28rpx;
				min-height: 100rpx;
			}

			.tag-group {
				display: flex;

				.tag-opt {
					padding: 6rpx 30rpx;
					border: 1rpx solid #ddd;
					border-radius: 30rpx;
					margin-right: 20rpx;
					font-size: 24rpx;

					&.active {
						background: $theme-color;
						border-color: $theme-color;
					}
				}
			}
		}
	}

	.save-box {
		padding: 40rpx 30rpx;

		.save-btn {
			background: $theme-color;
			border-radius: 45rpx;
			font-weight: bold;
			height: 90rpx;
			line-height: 90rpx;

			&::after {
				border: none;
			}
		}
	}
</style>