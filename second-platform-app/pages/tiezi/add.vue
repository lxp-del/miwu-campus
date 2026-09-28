<template>
	<view class="publish-container">
		<view class="custom-nav" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-left" @click="goBack">
				<text class="iconfont icon-back">←</text>
			</view>
			<view class="nav-right">
				<button class="btn-draft" @click="handleDraft">暂存</button>
				<button class="btn-publish" @click="handlePublish">发布</button>
			</view>
		</view>

		<scroll-view scroll-y class="main-content">
			<view class="card content-card">
				<view class="image-grid-uploader">
					<view v-if="imageList.length < 5" class="img-grid-wrapper" @click="uploadImage">
						<view class="upload-btn-container">
							<view class="upload-btn-content">
								<text class="plus-icon">+</text>
								<text class="upload-tip">还可以传 {{ 5 - imageList.length }} 张~</text>
							</view>
						</view>
					</view>

					<view class="img-grid-wrapper" v-for="(img, index) in imageList" :key="index">
						<image :src="img" mode="aspectFill" class="uploaded-img" @click="previewImage(index)"></image>
						<view class="delete-btn" @click.stop="deleteImage(index)">×</view>
					</view>
				</view>



				<view class="input-area">
					<input class="title-input" v-model="form.title" placeholder="标题输入~"
						placeholder-class="placeholder-style" />
					<textarea class="content-input" v-model="form.content" placeholder="内容输入"
						placeholder-class="placeholder-style" :maxlength="-1"></textarea>
				</view>


			</view>

		</scroll-view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		watch
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import {
		uploadFile
	} from '@/apis/file/file.js';
	import {
		addPost,
		getPostDetail
	} from "@/apis/tiezi/tiezi.js"

	const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;
 

	const form = reactive({
		title: '',
		content: '',
		imageUrl: '',
		isPublished: 0
		 
	});

	const imageList = ref([]);

	onLoad(() => {

	});



	// 数据提交前的准备工作
	const prepareFormData = (status) => {
		form.isPublished = status;
		form.imageUrl = imageList.value.join(',');
	};

	const handleDraft = () => {
		prepareFormData(0);
		console.log('--- 暂存表单数据 ---', JSON.parse(JSON.stringify(form)));
		uni.showToast({
			title: '已暂存',
			icon: 'success'
		});
	};

	const handlePublish = async () => {

		if (!form.title || imageList.value.length === 0) {
			return uni.showToast({
				title: '请填写标题并上传图片',
				icon: 'none'
			});
		}

		prepareFormData(1);
		console.log('--- 开始发布提交 ---', JSON.parse(JSON.stringify(form)));

		uni.showLoading({
			title: '发布中...',
			mask: true
		});

		try {
			const res = await addPost(form);
			uni.hideLoading();
			uni.showToast({
				title: '发布成功',
				icon: 'success'
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);

		} catch (error) {
			uni.hideLoading();
			console.error('发布失败错误详情:', error);
			uni.showToast({
				title: error.msg || '发布失败，请稍后再试',
				icon: 'none'
			});
		}
	};


	const uploadImage = () => {
		const remainCount = 5 - imageList.value.length;
		if (remainCount <= 0) return;
		uni.chooseImage({
			count: remainCount,
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
						if (response) {
							imageList.value.push(response);
						}
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

	const deleteImage = (index) => {
		imageList.value.splice(index, 1);
	};
	const previewImage = (index) => {
		uni.previewImage({
			current: index,
			urls: imageList.value
		});
	};
	const goBack = () => {
		uni.navigateBack();
	};
</script>

<style lang="scss" scoped>
	/* 样式部分保持原样，无需改动 */
	$theme-color: #D4FB1B;

	.publish-container {
		min-height: 100vh;
		background-color: #F8FAF7;
		display: flex;
		flex-direction: column;
	}

	.custom-nav {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 10px 20px;
		background-color: #fff;
		z-index: 10;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.03);

		.nav-left {
			font-size: 20px;
			font-weight: bold;
			padding: 10px;
		}

		.nav-right {
			display: flex;
			gap: 12px;

			button {
				margin: 0;
				font-size: 14px;
				border-radius: 30px;
				line-height: 2.1;
				padding: 0 22px;
				border: none;

				&::after {
					display: none;
				}
			}

			.btn-draft {
				background-color: #F2F3F0;
				color: #666;
			}

			.btn-publish {
				background-color: $theme-color;
				color: #1A1A1A;
				font-weight: bold;
			}
		}
	}

	.main-content {
		flex: 1;
		padding: 16px;
		box-sizing: border-box;
	}

	.card {
		background: #FFFFFF;
		border-radius: 18px;
		padding: 24px 20px;
		margin-bottom: 16px;
		box-shadow: 0 5px 20px rgba(0, 0, 0, 0.025);
	}

	.image-grid-uploader {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 10px;
		margin-bottom: 24px;

		.img-grid-wrapper {
			position: relative;
			aspect-ratio: 1;
			border-radius: 12px;
			overflow: hidden;

			.upload-btn-container {
				width: 100%;
				height: 100%;
				background-color: #F8FAF7;
				border: 1.5px dashed #E4E7ED;
				border-radius: 12px;
				display: flex;
				justify-content: center;
				align-items: center;

				.upload-btn-content {
					display: flex;
					flex-direction: column;
					align-items: center;

					.plus-icon {
						font-size: 32px;
						color: $theme-color;
					}

					.upload-tip {
						font-size: 10px;
						color: #A8ABB2;
					}
				}
			}

			.uploaded-img {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}

			.delete-btn {
				position: absolute;
				top: 4px;
				right: 4px;
				width: 20px;
				height: 20px;
				background: rgba(0, 0, 0, 0.5);
				color: #fff;
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 14px;
				z-index: 10;
			}
		}
	}

	.topic-scroll {
		width: 100%;
		margin-bottom: 24px;

		.topic-wrapper {
			display: flex;
			gap: 10px;
		}

		.topic-tag {
			flex-shrink: 0;
			padding: 7px 16px;
			background-color: #F2F3F0;
			border-radius: 20px;
			font-size: 13px;

			&.active {
				background-color: rgba($theme-color, 0.15);
				color: darken($theme-color, 25%);
				font-weight: bold;
			}
		}
	}

	.input-area {
		.title-input {
			font-size: 19px;
			font-weight: bold;
			padding-bottom: 18px;
			border-bottom: 1px solid #F0F2F0;
			margin-bottom: 18px;
		}

		.content-input {
			width: 100%;
			min-height: 140px;
			font-size: 16px;
			line-height: 1.6;
		}
	}

	.ai-action-bar {
		.ai-btn {
			display: inline-flex;
			align-items: center;
			gap: 7px;
			background: linear-gradient(135deg, rgba($theme-color, 0.1) 0%, rgba($theme-color, 0.03) 100%);
			padding: 9px 18px;
			border-radius: 20px;
			color: darken($theme-color, 25%);
			font-size: 14px;
			font-weight: bold;
		}
	}

	.settings-card {
		padding: 12px 20px;

		.setting-row {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 18px 0;

			&.border-bottom {
				border-bottom: 1px solid #F5FAF5;
			}

			.label {
				font-size: 16px;
				color: #303133;
				font-weight: 500;
			}

			.value-text {
				font-size: 15px;
				color: #333;

				&.placeholder-text {
					color: #A8ABB2;
				}
			}

			.price-box {
				display: flex;
				align-items: baseline;
				color: #F56C6C;
				font-weight: bold;

				.currency {
					font-size: 20px;
					margin-right: 2px;
				}

				.price-input {
					width: 90px;
					text-align: right;
					font-size: 19px;
				}
			}
		}
	}
</style>