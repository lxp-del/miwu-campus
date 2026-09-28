<template>
	<view class="edit-container">
		<view class="custom-nav" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-left" @tap="goBack">
				<text class="back-icon">✕</text>
			</view>
			<view class="nav-title">修改资料</view>
			<view class="nav-empty"></view>
		</view>

		<scroll-view scroll-y class="main-content">
			<view class="avatar-box">
				<view class="avatar-inner" @tap="handleUploadAvatar">
					<image :src="form.avatar || '/static/default-avatar.png'" mode="aspectFill" class="img" />
					<view class="camera-badge">
						<text class="icon">📷</text>
					</view>
				</view>
				<text class="tip">更换头像</text>
			</view>

			<view class="form-card">
				<view class="cell">
					<text class="label">昵称</text>
					<input v-model="form.name" placeholder="设置你的昵称" placeholder-class="p-color" />
				</view>

				<picker :range="genderRange" range-key="label" @change="onGenderChange">
					<view class="cell">
						<text class="label">性别</text>
						<view class="value-row">
							<text :class="form.gender === null ? 'placeholder' : 'val'">
								{{ form.gender !== null ? (form.gender == 1 ? '男' : '女') : '未设置' }}
							</text>
							<text class="arrow">></text>
						</view>
					</view>
				</picker>

				<picker mode="date" :value="form.birthday" @change="onDateChange">
					<view class="cell">
						<text class="label">生日</text>
						<view class="value-row">
							<text :class="!form.birthday ? 'placeholder' : 'val'">
								{{ form.birthday || '添加生日' }}
							</text>
							<text class="arrow">></text>
						</view>
					</view>
				</picker>

				<view class="cell">
					<text class="label">手机号</text>
					<input v-model="form.phone" type="number" placeholder="绑定手机号" placeholder-class="p-color" />
				</view>

				<view class="cell no-border">
					<text class="label">邮箱</text>
					<input v-model="form.email" placeholder="设置联系邮箱" placeholder-class="p-color" />
				</view>
			</view>

			<view style="height: 200rpx;"></view>
		</scroll-view>

		<view class="fixed-footer">
			<view class="save-btn-capsule" @tap="handleSave">
				<text>确认保存</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		computed
	} from 'vue'
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app'
	import {
		uploadFile
	} from '@/apis/file/file.js'
	import {
		getUserInfo,
		updateUserInfo
	} from '@/apis/user/userinfo.js'

	const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;

	// 登录信息
	const currentUser = ref(uni.getStorageSync('currentUser') || {});
	const currentUserId = computed(() => currentUser.value.id);

	// 表单对象
	const form = reactive({
		id: '',
		name: '',
		avatar: '',
		birthday: '',
		phone: '',
		gender: null,
		email: ''
	});

	const genderRange = [{
		value: 1,
		label: '男'
	}, {
		value: 0,
		label: '女'
	}];

	// 获取详情数据并回显
	const getDetail = () => {
		if (!currentUserId.value) return;
		getUserInfo(currentUserId.value).then(res => {
			if (res.data) {
				// 将后端数据同步到表单
				form.id = res.data.id;
				form.name = res.data.name;
				form.avatar = res.data.avatar;
				form.birthday = res.data.birthday;
				form.phone = res.data.phone;
				form.gender = res.data.gender;
				form.email = res.data.email;
			}
		})
	}

	onShow(() => {
		getDetail()
	})

	// 头像上传
	// editProfile.vue 中的 handleUploadAvatar
	const handleUploadAvatar = () => {
		uni.chooseImage({
			count: 1,
			success: async (res) => {
				const tempFiles = res.tempFiles;
				for (let i = 0; i < tempFiles.length; i++) {
					uni.showLoading({
						title: '上传中...'
					});
					try {
						const formData = new FormData();
						formData.append('file', tempFiles[i]);
						const response = await uploadFile(formData);
						console.log(response);
						form.avatar = response
					} catch (error) {
						uni.showToast({
							title: '上传失败',
							icon: 'none'
						});
					} finally {
						uni.hideLoading();
					}
				}
			}
		});
	};

	const onGenderChange = (e) => {
		form.gender = genderRange[e.detail.value].value;
	};

	const onDateChange = (e) => {
		form.birthday = e.detail.value;
	};

	const goBack = () => uni.navigateBack();

	const handleSave = () => {
		if (!form.name) return uni.showToast({
			title: '请输入昵称',
			icon: 'none'
		});

		uni.showLoading({
			title: '保存中...',
			mask: true
		});
		 
			 
		 updateUserInfo(form).then(res=>{
			 console.log(res);
			 uni.hideLoading();
			 uni.showToast({
			 	title: '保存成功',
			 	icon: 'success'
			 });
			 getUserInfo(currentUserId.value).then(res => {
			  uni.setStorageSync('currentUser',res.data )
			 })
			 
		 })

		 
	};
</script>

<style lang="scss" scoped>
	$theme: #D4FB1B;

	.edit-container {
		min-height: 100vh;
		background-color: #F7F8FA;
		display: flex;
		flex-direction: column;
	}

	/* 导航 */
	.custom-nav {
		background: #fff;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;

		.back-icon {
			font-size: 40rpx;
			color: #333;
		}

		.nav-title {
			font-size: 32rpx;
			font-weight: bold;
		}

		.nav-empty {
			width: 40rpx;
		}
	}

	/* 头像 */
	.avatar-box {
		padding: 60rpx 0;
		display: flex;
		flex-direction: column;
		align-items: center;

		.avatar-inner {
			position: relative;
			width: 160rpx;
			height: 160rpx;

			.img {
				width: 100%;
				height: 100%;
				border-radius: 50%;
				border: 4rpx solid #fff;
				box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.05);
			}

			.camera-badge {
				position: absolute;
				right: 0;
				bottom: 0;
				background: #000;
				width: 48rpx;
				height: 48rpx;
				border-radius: 50%;
				display: flex;
				justify-content: center;
				align-items: center;
				border: 4rpx solid #fff;

				.icon {
					font-size: 20rpx;
				}
			}
		}

		.tip {
			margin-top: 20rpx;
			font-size: 24rpx;
			color: #999;
		}
	}

	/* 表单卡片 */
	.form-card {
		background: #fff;
		margin: 0 30rpx;
		border-radius: 40rpx;
		padding: 10rpx 30rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.01);

		.cell {
			display: flex;
			justify-content: space-between;
			align-items: center;
			height: 110rpx;
			border-bottom: 1rpx solid #F5F6F7;

			&.no-border {
				border-bottom: none;
			}

			.label {
				font-size: 28rpx;
				color: #333;
				font-weight: 500;
			}

			input {
				flex: 1;
				text-align: right;
				font-size: 28rpx;
				color: #333;
			}

			.p-color {
				color: #ccc;
			}

			.value-row {
				flex: 1;
				display: flex;
				justify-content: flex-end;
				align-items: center;

				.val {
					font-size: 28rpx;
					color: #333;
				}

				.placeholder {
					font-size: 28rpx;
					color: #ccc;
				}

				.arrow {
					font-size: 24rpx;
					color: #ccc;
					margin-left: 10rpx;
				}
			}
		}
	}

	/* 底部固定居中保存按钮 */
	.fixed-footer {
		position: fixed;
		bottom: 60rpx;
		left: 0;
		width: 100%;
		display: flex;
		justify-content: center;
		pointer-events: none;
		/* 穿透点击，不影响背景 */

		.save-btn-capsule {
			pointer-events: auto;
			/* 恢复点击 */
			width: 600rpx;
			height: 100rpx;
			background: #1A1A1A;
			/* 黑色胶囊，更显高端 */
			color: $theme;
			border-radius: 50rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 30rpx;
			font-weight: bold;
			box-shadow: 0 16rpx 32rpx rgba(0, 0, 0, 0.2);
			transition: all 0.2s;

			&:active {
				transform: scale(0.96);
				opacity: 0.9;
			}
		}
	}
</style>