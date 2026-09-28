<template>
	<view class="page-container">
		<view class="status-bar-placeholder"></view>

		<view class="header-user-info">
			<view class="back-btn-wrap" @click="goBack">
				<text class="back-icon">‹</text>
			</view>
			<image class="avatar" :src="userInfo.avatar" mode="aspectFill"></image>
			<view class="info-content">
				<view class="top-row">
					<text class="name">{{ userInfo.name }}</text>
					<text class="crown-icon">👑</text>
				</view>
				<view class="bottom-row">
					<view class="credit-score">
						<view class="score-tag">
							<text class="shield-icon">🛡️</text>
							<text class="score-text">信誉极好 {{ userInfo.score }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="main-card">
			<view class="price-wrap">
				<text class="currency">¥</text>
				<text class="price">{{ productData.price }}</text>
			</view>

			<view class="tags-wrap">
				<text class="tag" v-for="(tag, index) in productData.tags" :key="index">{{ tag }}</text>
			</view>

			<view class="image-gallery-custom" v-if="productData.images && productData.images.length > 0">
				<image class="gallery-item" v-for="(imgUrl, index) in productData.images.slice(0, 5)" :key="index"
					:src="imgUrl" mode="aspectFill" @click="previewImage(index)"></image>
			</view>

			<view class="desc-content">
				<view class="desc-title">{{ productData.title }}</view>
				<text class="desc-text">{{ productData.desc }}</text>
			</view>
		</view>

		<view class="review-card">
			<view class="review-header">
				<text class="title">宝贝评价 ({{ reviews.length }})</text>
				<view class="view-all" @click="openReviewPopup">
					<text>查看全部</text>
					<text class="arrow">></text>
				</view>
			</view>

			<view class="review-list">
				<view class="review-item" v-for="(item, index) in topReviews" :key="index">
					<image class="reviewer-avatar" :src="item.avatar" mode="aspectFill"></image>
					<view class="review-content">
						<view class="reviewer-info">
							<text class="reviewer-name">{{ item.name }}</text>
							<text class="reviewer-meta">{{ item.time }} · {{ item.address }}</text>
						</view>
						<text class="review-text">{{ item.content }}</text>
					</view>
				</view>
			</view>

			<view class="divider"></view>

			<view class="review-input-area">
				<image class="current-avatar" :src="currentUser.avatar || '/static/logo.png'" mode="aspectFill"></image>
				<view class="input-box">
					<input class="uni-input" type="text" v-model="commentContent" placeholder="看对眼就留言，问问更多细节~"
						confirm-type="send" @confirm="submitComment" />
				</view>
				<button class="send-btn" @click="submitComment" :disabled="!commentContent.trim()">发布</button>
			</view>
		</view>

		<view class="safe-area-bottom-placeholder"></view>

		<view class="footer-action">
			<view class="action-left">
				<view class="icon-btn">
					<text class="icon">⭐</text>
					<text class="text">收藏</text>
				</view>
			</view>
			<view class="action-right">
				<button class="btn btn-chat" @click="goChat" v-if="!isMerchant">聊一聊</button>
				<PurchasePopup v-model="showBuyPopup" :product-data="productData" @confirm="handlePurchaseConfirm" />
				<button class="btn btn-buy" @click="showBuyPopup = true">立即购买</button>
			</view>
		</view>

		<view class="popup-mask" v-if="isPopupVisible" @click="closeReviewPopup" :class="{ 'show': isPopupAnimation }">
		</view>
		<view class="popup-container" :class="{ 'show': isPopupAnimation }">
			<view class="popup-header">
				<text class="popup-title">全部评价 ({{ reviews.length }})</text>
				<text class="close-btn" @click="closeReviewPopup">✕</text>
			</view>
			<scroll-view scroll-y class="popup-scroll-view">
				<view class="popup-review-list">
					<view class="review-item border-line" v-for="(item, index) in reviews" :key="index">
						<image class="reviewer-avatar" :src="item.avatar" mode="aspectFill"></image>
						<view class="review-content">
							<view class="reviewer-info">
								<text class="reviewer-name">{{ item.name }}</text>
								<text class="reviewer-meta">{{ item.time }} · {{ item.address }}</text>
							</view>
							<text class="review-text">{{ item.content }}</text>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		computed,
		nextTick
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import {
		getGoodsById
	} from "@/apis/goods/goodstopandtype.js";
	import {
		addCartGoods
	} from "/apis/cart/cart.js";

	import {
		getCommentList,
		addComment
	} from "@/apis/comment/comment.js"

	import PurchasePopup from '@/components/PurchasePopup.vue';

	const showBuyPopup = ref(false);
	const goodId = ref(null);
	const commentContent = ref('');

	const userInfo = ref({
		id: '',
		avatar: '',
		name: '',
		score: '',
		dateDiffer: '',
		address: ''
	});

	const currentUser = ref(uni.getStorageSync('currentUser') || {});
	const currentUserId = computed(() => currentUser.value.id);
	const isMerchant = computed(() => currentUser.value.userTagId === 2);

	const productData = ref({
		id: '',
		title: '',
		price: '',
		tags: [],
		views: '',
		desc: '',
		images: []
	});

	const reviews = ref([]);
	const topReviews = computed(() => reviews.value.slice(0, 3));

	const handlePurchaseConfirm = (payload) => {
		payload.goodsId = goodId.value
		addCartGoods(payload).then(res => {
			uni.showToast({
				title: '下单成功'
			});
		})
	}

	const getList = () => {
		getCommentList(goodId.value).then(res => {
			reviews.value = res.data;
		})
	}
	const submitComment = async () => {
		const content = commentContent.value.trim();
		if (!content) {
			uni.showToast({
				title: '请输入评论内容',
				icon: 'none'
			});
			return;
		}
		uni.showLoading({
			title: '发布中...'
		});
		const postData = {
			goodId: goodId.value,
			userId: currentUserId.value,
			content: content
		};
		try {
			const res = await addComment(postData);
			uni.showToast({
				title: '评论成功',
				icon: 'success'
			});
			getList()
			commentContent.value = ''
		} catch (err) {
			uni.showToast({
				title: '网络请求失败',
				icon: 'none'
			});
		} finally {
			uni.hideLoading();
		}
	};

	const goBack = () => uni.navigateBack();
	const goChat = () => {
		if (currentUserId.value == userInfo.value.id) {
			uni.showToast({
				title: '不能和自己聊天哦',
				icon: 'none',
				duration: 2000
			});
			return;
		}
		uni.navigateTo({
			url: `/pages/chat/index?goodsId=${productData.value.id}&merchantId=${userInfo.value.id}`
		});
	}
	const previewImage = (index) => uni.previewImage({
		current: index,
		urls: productData.value.images
	});

	const isPopupVisible = ref(false);
	const isPopupAnimation = ref(false);
	const openReviewPopup = () => {
		isPopupVisible.value = true;
		nextTick(() => {
			setTimeout(() => {
				isPopupAnimation.value = true;
			}, 50);
		});
	};
	const closeReviewPopup = () => {
		isPopupAnimation.value = false;
		setTimeout(() => {
			isPopupVisible.value = false;
		}, 300);
	};

	const getDetail = () => {
		getGoodsById(goodId.value).then(res => {
			const d = res.data;
			userInfo.value = {
				...d.sysUser,
				dateDiffer: d.dateDiffer,
				id: d.sysUser?.id || d.userId
			};
			productData.value = {
				id: d.id,
				price: d.price,
				title: d.title,
				desc: d.content,
				tags: d.topic ? d.topic.split(',') : [],
				images: d.imageUrls ? d.imageUrls.split(',') : [],
				views: d.views || 0
			};
		});
	}

	onLoad((opt) => {
		if (opt.id) {
			goodId.value = opt.id;
			getDetail();
			getList();
		}
	});
</script>

<style lang="scss" scoped>
	$theme-color: #D4FB1B;
	$text-main: #333333;
	$text-sub: #999999;
	$border-color: #F0F0F0;

	.page-container {
		min-height: 100vh;
		background-color: #F7F8FA;
		padding-bottom: 140rpx;
	}

	.status-bar-placeholder {
		height: 25px;
		background: #fff;
	}

	.header-user-info {
		display: flex;
		align-items: center;
		padding: 20rpx 30rpx;
		background: #fff;

		.back-btn-wrap {
			margin-right: 20rpx;
			.back-icon { font-size: 50rpx; }
		}

		.avatar {
			width: 90rpx;
			height: 90rpx;
			border-radius: 50%;
			border: 2rpx solid #f0f0f0;
		}

		.info-content {
			flex: 1;
			margin-left: 20rpx;
			display: flex;
			flex-direction: column;
			justify-content: center;

			.top-row {
				display: flex;
				align-items: center;
				margin-bottom: 8rpx;
				.name { font-weight: bold; font-size: 32rpx; color: $text-main; }
				.crown-icon { font-size: 24rpx; margin-left: 8rpx; }
			}

			.bottom-row {
				.credit-score {
					display: inline-block;
					.score-tag {
						display: flex;
						align-items: center;
						background: linear-gradient(135deg, rgba(0, 178, 106, 0.1), rgba(0, 178, 106, 0.05));
						border: 1rpx solid rgba(0, 178, 106, 0.15);
						border-radius: 6rpx;
						padding: 2rpx 12rpx;
						.shield-icon { font-size: 20rpx; margin-right: 6rpx; }
						.score-text { font-size: 22rpx; color: #00B26A; font-weight: 500; }
					}
				}
			}
		}
	}

	.main-card {
		background: #fff;
		margin: 20rpx;
		padding: 30rpx;
		border-radius: 24rpx;

		.price-wrap {
			color: #FF4A4A;
			font-weight: bold;
			font-size: 40rpx;
			margin-bottom: 20rpx;
		}

		/* --- 标签部分样式修改 --- */
		.tags-wrap {
			display: flex;
			flex-wrap: wrap;
			gap: 12rpx;
			margin-bottom: 24rpx;

			.tag {
				padding: 6rpx 16rpx;
				background-color: #F2F3F5;
				color: #606266;
				font-size: 22rpx;
				border-radius: 8rpx;
				font-weight: 400;
				/* 轻微阴影增加层次感 */
				box-shadow: 0 2rpx 4rpx rgba(0,0,0,0.02);
			}
		}

		/* --- 图片三行排列布局 (1 + 2 + 2) --- */
		.image-gallery-custom {
			display: grid;
			grid-template-columns: repeat(2, 1fr);
			gap: 12rpx;
			margin: 24rpx 0;

			.gallery-item {
				width: 100%;
				height: 340rpx;
				border-radius: 16rpx;
				background-color: #f8f8f8;
			}

			.gallery-item:first-child {
				grid-column: span 2;
				height: 420rpx;
			}
		}

		.desc-title {
			font-weight: bold;
			font-size: 32rpx;
			margin-bottom: 10rpx;
		}
	}

	.review-card {
		background: #fff;
		margin: 20rpx;
		padding: 30rpx;
		border-radius: 24rpx;

		.review-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			font-weight: bold;
		}

		.divider {
			height: 1rpx;
			background: $border-color;
			margin: 20rpx 0;
		}

		.review-input-area {
			display: flex;
			align-items: center;
			gap: 20rpx;
			.current-avatar { width: 60rpx; height: 60rpx; border-radius: 50%; }
			.input-box {
				flex: 1;
				background: #f5f5f5;
				height: 70rpx;
				border-radius: 35rpx;
				padding: 0 25rpx;
				display: flex;
				align-items: center;
				.uni-input { font-size: 26rpx; width: 100%; }
			}
			.send-btn {
				background: $theme-color;
				font-size: 24rpx;
				font-weight: bold;
				border-radius: 30rpx;
				height: 60rpx;
				line-height: 60rpx;
			}
		}
	}

	.review-item {
		display: flex;
		padding: 24rpx 0;
		&.border-line { border-bottom: 1rpx solid #f0f0f0; }
		.reviewer-avatar { width: 70rpx; height: 70rpx; border-radius: 50%; flex-shrink: 0; margin-right: 20rpx; }
		.review-content {
			flex: 1;
			min-width: 0;
			.reviewer-info {
				display: flex;
				justify-content: space-between;
				align-items: flex-start;
				margin-bottom: 8rpx;
				.reviewer-name { font-size: 28rpx; font-weight: 500; color: $text-main; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-right: 20rpx; }
				.reviewer-meta { font-size: 22rpx; color: $text-sub; flex-shrink: 0; text-align: right; white-space: nowrap; }
			}
			.review-text { font-size: 27rpx; color: #444; line-height: 1.5; word-break: break-all; }
		}
	}

	.footer-action {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 110rpx;
		background: #fff;
		padding: 0 30rpx env(safe-area-inset-bottom);
		display: flex;
		align-items: center;
		box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
		.action-right {
			margin-left: auto;
			display: flex;
			gap: 20rpx;
			.btn { width: 180rpx; height: 75rpx; line-height: 75rpx; font-size: 26rpx; border-radius: 40rpx; font-weight: bold; }
			.btn-chat { background: #1a1a1a; color: #fff; }
			.btn-buy { background: $theme-color; }
		}
	}

	.popup-mask {
		position: fixed;
		inset: 0;
		background: rgba(0, 0, 0, 0.5);
		z-index: 998;
		opacity: 0;
		transition: 0.3s;
		&.show { opacity: 1; }
	}

	.popup-container {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 75vh;
		background: #fff;
		border-radius: 32rpx 32rpx 0 0;
		z-index: 999;
		transform: translateY(100%);
		transition: 0.3s;
		&.show { transform: translateY(0); }
		.popup-header {
			height: 100rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			border-bottom: 1rpx solid #eee;
			.popup-title { font-weight: bold; }
			.close-btn { position: absolute; right: 30rpx; font-size: 40rpx; color: #999; }
		}
		.popup-scroll-view {
			height: calc(75vh - 100rpx);
			.popup-review-list { padding: 0 30rpx 40rpx; }
		}
	}

	.safe-area-bottom-placeholder {
		height: 120rpx;
	}
</style>