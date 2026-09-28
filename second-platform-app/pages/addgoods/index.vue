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

				<scroll-view scroll-x class="topic-scroll" :show-scrollbar="false">
					<view class="topic-wrapper">
						<view class="topic-tag" :class="{ 'active': selectedTopicIds.includes(item.id) }"
							v-for="item in topicRange" :key="item.id" @click="toggleTopic(item.id)">
							# {{ item.topic }}
						</view>
					</view>
				</scroll-view>

				<view class="input-area">
					<input class="title-input" v-model="form.title" placeholder="填写好标题会有更多赞哦~"
						placeholder-class="placeholder-style" />
					<textarea class="content-input" v-model="form.content" placeholder="描述一下你的闲置物品详情吧..."
						placeholder-class="placeholder-style" :maxlength="-1"></textarea>
				</view>

				<view class="ai-action-bar">
					<view class="ai-btn" :class="{ 'is-writing': isWriting }" @click="handleAiWrite">
						<text class="sparkle">✨</text>
						<text>{{ isWriting ? 'AI续写中...' : 'AI续写' }}</text>
					</view>
				</view>
			</view>

			<view class="card settings-card">
				<view class="setting-row border-bottom">
					<text class="label">价格</text>
					<view class="value-box price-box">
						<text class="currency">￥</text>
						<input type="digit" v-model="form.price" placeholder="0.00" class="price-input" />
					</view>
				</view>

				<picker :range="categoryRange" range-key="label" @change="onCategoryChange">
					<view class="setting-row border-bottom">
						<text class="label">所属类型</text>
						<text class="value-text" :class="{'placeholder-text': !categoryNameDisplay}">
							{{ categoryNameDisplay || '请选择分类' }} >
						</text>
					</view>
				</picker>

				<picker :range="shippingRange" range-key="label" @change="onShippingChange">
					<view class="setting-row border-bottom">
						<text class="label">发货方式</text>
						<text class="value-text" :class="{'placeholder-text': !shippingNameDisplay}">
							{{ shippingNameDisplay || '快递' }} >
						</text>
					</view>
				</picker>

				<picker :range="visibilityRange" range-key="label" @change="onVisibilityChange">
					<view class="setting-row">
						<text class="label">查看权限</text>
						<text class="value-text" :class="{'placeholder-text': !visibilityNameDisplay}">
							{{ visibilityNameDisplay || '公开' }} >
						</text>
					</view>
				</picker>
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
		getGoodsTypeList,
		getGoodsTopicList,
		addOrderGoods
	} from '@/apis/goods/goodstopandtype.js';

	const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;

	// 表单数据汇总
	const form = reactive({
		title: '',
		content: '',
		topic: '', // 提交字段：话题ID逗号拼接
		price: '',
		typeId: null, // 提交字段：类型ID
		deliveryMethod: 1, // 提交字段：发货方式ID
		viewPermission: 1, // 提交字段：查看权限ID
		imageUrls: '', // 提交字段：图片URL逗号拼接
		isPublished: 0
	});

	// 辅助展示和内部处理的状态
	const imageList = ref([]); // 用于预览的图片数组
	const selectedTopicIds = ref([]); // 用于多选逻辑的ID数组
	const topicRange = ref([]); // 接口获取的话题列表
	const categoryRange = ref([]); // 接口获取的分类列表

	const categoryNameDisplay = ref('');
	const shippingNameDisplay = ref('快递');
	const visibilityNameDisplay = ref('公开');

	const shippingRange = [{
		id: 1,
		label: '快递'
	}, {
		id: 2,
		label: '自提'
	}, {
		id: 3,
		label: '无需物流'
	}];
	const visibilityRange = [{
		id: 1,
		label: '公开'
	}, {
		id: 2,
		label: '仅好友'
	}, {
		id: 3,
		label: '仅自己'
	}];

	// 价格上限校验
	watch(() => form.price, (newVal) => {
		if (parseFloat(newVal) > 999) {
			uni.showToast({
				title: '价格最高上限为999元',
				icon: 'none'
			});
			setTimeout(() => {
				form.price = '999';
			}, 0);
		}
	});

	onLoad(() => {
		getList();
	});

	function getList() {
		getGoodsTypeList().then(res => {
			categoryRange.value = res.data;
		});
		getGoodsTopicList().then(res => {
			topicRange.value = res.data;
		});
	}

	// 数据提交前的准备工作
	const prepareFormData = (status) => {
		form.isPublished = status;
		form.imageUrls = imageList.value.join(',');
		form.topic = selectedTopicIds.value.join(',');
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
		// 1. 基础校验
		if (!form.title || imageList.value.length === 0) {
			return uni.showToast({
				title: '请填写标题并上传图片',
				icon: 'none'
			});
		}

		// 2. 价格最终确认（防止越过 watch）
		if (parseFloat(form.price) > 999) {
			form.price = '999';
		}

		// 3. 准备提交的数据 (设置 isPublished 为 1，拼接图片和话题)
		prepareFormData(1);

		console.log('--- 开始发布提交 ---', JSON.parse(JSON.stringify(form)));

		// 4. 调用接口
		uni.showLoading({
			title: '发布中...',
			mask: true // 加上遮罩防止重复点击
		});

		try {
			// 直接将处理好的 form 对象传给接口
			const res = await addOrderGoods(form);

			// 5. 根据后端返回结果处理
			// 假设后端成功 code 为 200 或根据你的业务逻辑判断
			uni.hideLoading();
			uni.showToast({
				title: '发布成功',
				icon: 'success'
			});

			uni.switchTab({
			    url: '/pages/index/index'
			})

		} catch (error) {
			uni.hideLoading();
			console.error('发布失败错误详情:', error);
			uni.showToast({
				title: error.msg || '发布失败，请稍后再试',
				icon: 'none'
			});
		}
	};

	// 图片上传逻辑保持不变
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

	// 话题选择逻辑（存储ID）
	const toggleTopic = (id) => {
		const index = selectedTopicIds.value.indexOf(id);
		if (index === -1) {
			selectedTopicIds.value.push(id);
		} else {
			selectedTopicIds.value.splice(index, 1);
		}
	};

	// 选择器变更
	const onCategoryChange = (e) => {
		const idx = e.detail.value;
		form.typeId = categoryRange.value[idx].id;
		categoryNameDisplay.value = categoryRange.value[idx].label;
	};
	const onShippingChange = (e) => {
		const idx = e.detail.value;
		form.deliveryMethod = shippingRange[idx].id;
		shippingNameDisplay.value = shippingRange[idx].label;
	};
	const onVisibilityChange = (e) => {
		const idx = e.detail.value;
		form.viewPermission = visibilityRange[idx].id;
		visibilityNameDisplay.value = visibilityRange[idx].label;
	};

	// AI 续写逻辑
	const isWriting = ref(false);
	const handleAiWrite = () => {
		// 1. 新增：判断标题是否为空
		if (!form.title) {
			uni.showToast({
				title: '请适当添加标题以供续写',
				icon: 'none', // 使用 'none' 图标以显示纯文本提示
				duration: 2000
			});
			return; // 直接终止函数执行，不继续后续逻辑
		}

		// 2. 原有的防抖和内容检查
		if (isWriting.value || (!form.title && !form.content)) return;

		isWriting.value = true;
		const mockRes = "经过AI的深度润色：这是一件非常棒的闲置物品，成色极佳，平价转让。";
		let i = 0;

		// 3. 内容拼接前的换行处理
		if (form.content.length > 0) form.content += '\n';

		const timer = setInterval(() => {
			if (i < mockRes.length) {
				form.content += mockRes.charAt(i++);
			} else {
				clearInterval(timer);
				isWriting.value = false;
			}
		}, 60);
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