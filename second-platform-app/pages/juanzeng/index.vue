<template>
	<view class="publish-page">
		<!-- 返回按钮 -->
		<view class="back-btn" @click="goBack">
			<text class="back-icon">‹</text>
		</view>

		<view class="card content-card">
			<textarea class="desc-textarea" v-model="formData.description" placeholder="描述一下你的闲置物品吧，比如新旧程度、适用人群等..."
				placeholder-class="placeholder-style" maxlength="500" auto-height />

			<view class="image-grid">
				<view class="image-item" v-for="(img, index) in formData.images" :key="index">
					<image :src="img" mode="aspectFill" class="uploaded-img"></image>
					<view v-if="index === 0" class="cover-tag">封面</view>
					<view class="delete-btn" @click="removeImage(index)">
						<text class="delete-icon">×</text>
					</view>
				</view>

				<view class="upload-btn" v-if="formData.images.length < 5" @click="uploadImage">
					<text class="plus-icon">+</text>
					<text class="upload-text">添加图片</text>
				</view>
			</view>
		</view>

		<view class="card info-card">
			<view class="form-item border-bottom">
				<text class="label">物品名称</text>
				<input class="input-box" v-model="formData.title" placeholder="请输入物品名称，如：八成新高数课本"
					placeholder-class="placeholder-style" />
			</view>

			<picker mode="selector" :range="categoryList" @change="onCategoryChange">
				<view class="form-item">
					<text class="label">物品分类</text>
					<view class="picker-value">
						<text :class="{'placeholder-style': !formData.category}">
							{{ formData.category || '请选择分类' }}
						</text>
						<text class="arrow-right">></text>
					</view>
				</view>
			</picker>
		</view>

		<view class="card settings-card">
			<picker mode="selector" :range="targetOrgList" @change="onTargetOrgChange">
				<view class="form-item border-bottom">
					<text class="label">受助去向</text>
					<view class="picker-value">
						<text :class="{'placeholder-style': !formData.target_org}">
							{{ formData.target_org || '请选择捐赠去向' }}
						</text>
						<text class="arrow-right">></text>
					</view>
				</view>
			</picker>

			<view class="form-item border-bottom radio-item">
				<text class="label">交接方式</text>
				<radio-group class="radio-group" @change="onDonationTypeChange">
					<label class="radio-label">
						<radio value="定点投递" :checked="formData.donation_type === '定点投递'" color="#D4FB1B"
							style="transform:scale(0.8)" /> 定点投递
					</label>
					<label class="radio-label">
						<radio value="上门回收" :checked="formData.donation_type === '上门回收'" color="#D4FB1B"
							style="transform:scale(0.8)" /> 上门回收
					</label>
				</radio-group>
			</view>

			<view class="form-item">
				<text class="label">联系方式</text>
				<input class="input-box" v-model="formData.contact_info" type="number" placeholder="请输入手机号/微信号"
					placeholder-class="placeholder-style" />
			</view>
		</view>

		<view class="safe-padding"></view>

		<view class="bottom-bar">
			<view class="points-info">
				<text class="points-label">预计获得</text>
				<text class="points-value">+50 爱心积分</text>
			</view>
			<button class="submit-btn" :loading="isSubmitting" @click="submitDonation">
				立即捐赠
			</button>
		</view>
	</view>
</template>

<script setup lang="ts">
	import { ref, reactive } from 'vue';
	// 假设这里是你系统的统一上传接口
	import { uploadFile } from '@/apis/file/file.js';
	import { createDonation } from '@/apis/donation/donation.js';
	// ================= 数据结构定义 =================
	interface DonationForm {
		item_name : string;
		category : string;
		images : string[];
		description : string;
		contact_info : string;
		donation_type : string;
		target_org : string;
	}

	// 表单状态响应式对象
	const formData = reactive<DonationForm>({
		item_name: '',
		category: '',
		images: [], // 多图上传
		description: '',
		contact_info: '13800000000', // 默认读取当前用户信息 (Mock)
		donation_type: '定点投递',   // 默认方式
		target_org: ''
	});

	const isSubmitting = ref(false);

	// 常量配置
	const categoryList = ['书籍', '衣物', '数码', '其他'];
	const targetOrgList = ['校内贫困生', '流浪动物救助站', '山区小学'];

	// ================= 事件处理方法 =================

	// 返回上一页
	const goBack = () => {
		uni.navigateBack();
	};

	// 分类选择
	const onCategoryChange = (e : any) => {
		formData.category = categoryList[e.detail.value];
	};

	// 目标机构选择
	const onTargetOrgChange = (e : any) => {
		formData.target_org = targetOrgList[e.detail.value];
	};

	// 捐赠交接方式选择
	const onDonationTypeChange = (e : any) => {
		formData.donation_type = e.detail.value;
	};

	// 图片删除
	const removeImage = (index : number) => {
		formData.images.splice(index, 1);
	};

	// ================= 系统提供的图片上传逻辑 =================
	const uploadImage = () => {
		const remainCount = 5 - formData.images.length;
		if (remainCount <= 0) {
			uni.showToast({ title: '最多只能上传5张图片', icon: 'none' });
			return;
		}

		uni.chooseImage({
			count: remainCount,
			success: async (res : any) => {
				const tempFiles = res.tempFiles;
				for (let i = 0; i < tempFiles.length; i++) {
					uni.showLoading({ title: '上传中...' });
					try {
						const fileData = new FormData();
						fileData.append('file', tempFiles[i]);

						const response = await uploadFile(fileData);
						// 假设后端返回的是图片的绝对URL
						if (response) {
							formData.images.push(response);
						}
					} catch (error) {
						uni.showToast({ title: '部分上传失败', icon: 'none' });
					} finally {
						uni.hideLoading();
					}
				}
			}
		});
	};

	// ================= 表单提交 =================
	const submitDonation = async () => {
		// 1. 简单的表单校验
		if (!formData.images.length) return uni.showToast({ title: '请至少上传一张图片', icon: 'none' });
		 
		if (!formData.category) return uni.showToast({ title: '请选择分类', icon: 'none' });
		if (!formData.target_org) return uni.showToast({ title: '请选择受助去向', icon: 'none' });
	
		// 2. 模拟请求后端
		isSubmitting.value = true;
		uni.showLoading({ title: '正在提交爱心...' });
	
		try {
			// 模拟网络延迟
			await new Promise(resolve => setTimeout(resolve, 1500));
			
			// 创建请求数据的副本，避免修改原始 formData
			const requestData = {
				...formData,
				images: Array.isArray(formData.images) 
					? formData.images.join(',') 
					: (typeof formData.images === 'string' ? formData.images : '')
			};
			
			// 调试：查看实际发送的数据
			console.log('【提交给后端的 JSON 数据】:', JSON.stringify(requestData, null, 2));
			console.log('images字段类型:', typeof requestData.images);
			console.log('images字段值:', requestData.images);
			
			// 调用API
			createDonation(requestData).then(res => {
				console.log('API响应:', res);
				
				// 成功提示
				uni.showToast({ title: '捐赠发布成功！', icon: 'success' });
	
				// 延迟跳转或返回上一页
				setTimeout(() => {
				  uni.navigateTo({
				    url: '/pages/juanzeng/list'  // 替换为你的详情页路径
				  });
				}, 1500);
			}).catch(error => {
				console.error('API请求错误:', error);
				uni.showToast({ title: '发布失败，请重试', icon: 'error' });
			});
	
		} catch (error) {
			console.error('提交过程错误:', error);
			uni.showToast({ title: '发布失败，请重试', icon: 'error' });
		} finally {
			isSubmitting.value = false;
			uni.hideLoading();
		}
	};
</script>

<style scoped>
	/* 主题色设置为 #D4FB1B */
	:root {
		--theme-color: #D4FB1B;
	}

	/* 全局页面背景：闲鱼灰 */
	.publish-page {
		min-height: 100vh;
		background-color: #F5F5F5;
		padding: 20rpx;
		box-sizing: border-box;
		position: relative;
	}

	/* 返回按钮 */
	.back-btn {
		position: fixed;
		top: 40rpx;
		left: 30rpx;
		width: 60rpx;
		height: 60rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		z-index: 100;
	}

	.back-icon {
		font-size: 40rpx;
		color: #333;
		font-weight: bold;
		margin-left: -4rpx;
		margin-top: -2rpx;
	}

	/* 统一卡片样式：圆角+微阴影 */
	.card {
		background-color: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.02);
	}

	/* 输入框占位符通用颜色 */
	.placeholder-style {
		color: #B0B0B0;
		font-size: 28rpx;
	}

	/* --- 卡片1: 文本与图片区域 --- */
	.content-card {
		padding-bottom: 20rpx;
		margin-top: 100rpx;
	}

	.desc-textarea {
		width: 100%;
		min-height: 180rpx;
		font-size: 30rpx;
		line-height: 1.5;
		color: #333;
		margin-bottom: 30rpx;
	}

	/* 图片网格布局 */
	.image-grid {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}

	.image-item,
	.upload-btn {
		width: 210rpx;
		height: 210rpx;
		border-radius: 16rpx;
		position: relative;
		overflow: hidden;
	}

	.uploaded-img {
		width: 100%;
		height: 100%;
		background-color: #F0F0F0;
	}

	/* 首图封面角标 - 使用新主题色 */
	.cover-tag {
		position: absolute;
		left: 0;
		bottom: 0;
		background: rgba(212, 251, 27, 0.9);
		/* 使用新主题色 #D4FB1B */
		color: #333;
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		border-top-right-radius: 16rpx;
		font-weight: bold;
	}

	/* 删除图片按钮 */
	.delete-btn {
		position: absolute;
		top: 8rpx;
		right: 8rpx;
		width: 40rpx;
		height: 40rpx;
		background: rgba(0, 0, 0, 0.5);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.delete-icon {
		color: #FFF;
		font-size: 28rpx;
		line-height: 1;
		margin-top: -4rpx;
	}

	/* 上传按钮样式 */
	.upload-btn {
		background-color: #F8F8F8;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		border: 2rpx dashed #E0E0E0;
	}

	.plus-icon {
		font-size: 60rpx;
		color: #999;
		font-weight: 300;
		margin-bottom: 10rpx;
	}

	.upload-text {
		font-size: 24rpx;
		color: #999;
	}

	/* --- 卡片2 & 3: 表单项 --- */
	.form-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 0;
	}

	.border-bottom {
		border-bottom: 1rpx solid #F0F0F0;
	}

	.label {
		font-size: 30rpx;
		color: #333;
		font-weight: 500;
		width: 140rpx;
	}

	.input-box {
		flex: 1;
		text-align: right;
		font-size: 30rpx;
		color: #333;
	}

	.picker-value {
		flex: 1;
		text-align: right;
		font-size: 30rpx;
		color: #333;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.arrow-right {
		margin-left: 10rpx;
		color: #CCC;
		font-size: 28rpx;
	}

	/* 单选框定制 - 使用新主题色 */
	.radio-item {
		justify-content: space-between;
	}

	.radio-group {
		display: flex;
		gap: 30rpx;
	}

	.radio-label {
		font-size: 28rpx;
		color: #333;
		display: flex;
		align-items: center;
	}

	/* --- 底部悬浮操作栏 --- */
	.safe-padding {
		height: 140rpx;
		/* 留出底部空间，防止被遮挡 */
	}

	.bottom-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		background-color: #FFFFFF;
		padding: 20rpx 30rpx;
		padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
		box-sizing: border-box;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.04);
		z-index: 99;
	}

	.points-info {
		display: flex;
		flex-direction: column;
	}

	.points-label {
		font-size: 24rpx;
		color: #999;
		margin-bottom: 4rpx;
	}

	.points-value {
		font-size: 32rpx;
		color: #FF5A5F;
		/* 强调色，凸显公益回报 */
		font-weight: bold;
	}

	/* 提交按钮使用新主题色 */
	.submit-btn {
		background-color: #D4FB1B;
		/* 使用新主题色 */
		color: #333;
		font-size: 32rpx;
		font-weight: bold;
		border-radius: 40rpx;
		padding: 0 60rpx;
		height: 80rpx;
		line-height: 80rpx;
		margin: 0;
		/* 覆写uni按钮默认margin */
	}

	.submit-btn::after {
		border: none;
		/* 去除边框 */
	}
</style>