<template>
	<view class="container">
		<view class="custom-header">
			<view class="header-content">
				<view class="left-action" @tap="goBack">
					<text class="back-icon">‹</text>
				</view>

				<text class="nav-title">校园贴吧</text>

				<view class="right-action">
					<view class="mini-fab" @tap="goToAddPage">
						<text class="plus">+</text>
					</view>
				</view>
			</view>
		</view>

		<view class="post-list-wrapper">
			<view v-for="(item, index) in postData" :key="index" class="post-card">
				<view class="header-row">
					<image :src="item.createSysUser.avatar" class="avatar" mode="aspectFill" />
					<view class="author-meta">
						<text class="username">{{ item.createSysUser.name }}</text>
						<text class="publish-time">{{ item.createTime }}</text>
					</view>
				</view>

				<view class="main-body">
					<view v-if="item.title" class="post-title">{{ item.title }}</view>
					<view v-if="item.content" class="post-content">{{ item.content }}</view>

					<view v-if="getImageList(item.imageUrl).length > 0" class="media-container">
						<image v-if="getImageList(item.imageUrl).length === 1" :src="getImageList(item.imageUrl)[0]"
							mode="widthFix" class="single-img" @tap="previewImage(getImageList(item.imageUrl), 0)" />
						<view v-else class="multi-grid">
							<image v-for="(img, idx) in getImageList(item.imageUrl).slice(0, 9)" :key="idx" :src="img"
								mode="aspectFill" @tap="previewImage(getImageList(item.imageUrl), idx)" />
						</view>
					</view>
				</view>

				<view class="footer-action">
					<view class="stats-group">
						<view class="stat-item">
							<text class="label">浏览</text>
							<text class="value">
								{{ item.views + getRandomInt(1, 100) }}
							</text>
						</view>
						<view class="stat-item">
							<text class="label">点赞</text>
							<text class="value">{{ item.likes+ getRandomInt(1, 100) }}</text>
						</view>
					</view>
					<view class="like-btn" :class="{ 'is-liked': item.isLiked }" @tap="handleLike(index)">
						<text class="heart-icon">{{ item.isLiked ? '❤️' : '🤍' }}</text>
						<text class="like-text">{{ item.isLiked ? '已赞' : '点赞' }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {
		getPostList
	} from "@/apis/tiezi/tiezi.js"
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app';

	const postData = ref([]);

	onShow(() => {
		getList();
	});

	const getList = () => {
		getPostList().then(res => {
			postData.value = res.data
		})
	}
	const getRandomInt = (min, max) => {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	};

	const getImageList = (str) => str ? str.split(',') : [];
	const previewImage = (urls, current) => uni.previewImage({
		urls,
		current
	});
	const handleLike = (index) => {
		const item = postData.value[index];
		item.isLiked = !item.isLiked;
		item.isLiked ? item.likes++ : item.likes--;
		uni.vibrateShort();
	};

	const goBack = () => {
		uni.navigateBack({
			delta: 1
		});
	};

	const goToAddPage = () => uni.navigateTo({
		url: '/pages/tiezi/add'
	});
</script>

<style lang="scss" scoped>
	.container {
		background-color: #f6f7f9;
		min-height: 100vh;
	}

	/* --- 自定义 Header 样式 --- */
	.custom-header {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		background-color: #ffffff;
		z-index: 1000;
		padding-top: var(--status-bar-height); // 适配手机状态栏
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.05);

		.header-content {
			height: 88rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 32rpx;

			.left-action {
				width: 80rpx;

				.back-icon {
					font-size: 60rpx;
					color: #333;
					font-weight: 300;
				}
			}

			.nav-title {
				font-size: 34rpx;
				font-weight: bold;
				color: #1a1a1a;
			}

			.right-action {
				width: 80rpx;
				display: flex;
				justify-content: flex-end;

				.mini-fab {
					width: 64rpx;
					height: 64rpx;
					background-color: #D4FB1B; // 你的主题色
					border-radius: 50%;
					display: flex;
					justify-content: center;
					align-items: center;
					box-shadow: 0 4rpx 12rpx rgba(212, 251, 27, 0.4);

					.plus {
						font-size: 44rpx;
						color: #000;
						font-weight: bold;
						margin-top: -4rpx;
					}

					&:active {
						transform: scale(0.9);
					}
				}
			}
		}
	}

	/* 列表容器：适配 Header 高度 */
	.post-list-wrapper {
		padding-top: calc(var(--status-bar-height) + 100rpx);
		padding-bottom: 40rpx;
	}

	.post-card {
		background-color: #ffffff;
		padding: 32rpx;
		border-bottom: 1px solid rgba(0, 0, 0, 0.03);
		margin-bottom: 2rpx;

		.header-row {
			display: flex;
			align-items: center;
			margin-bottom: 24rpx;

			.avatar {
				width: 80rpx;
				height: 80rpx;
				border-radius: 16rpx;
				background: #f0f0f0;
			}

			.author-meta {
				margin-left: 20rpx;

				.username {
					font-size: 30rpx;
					font-weight: 600;
					color: #333;
					display: block;
				}

				.publish-time {
					font-size: 24rpx;
					color: #b2b2b2;
					margin-top: 4rpx;
				}
			}
		}

		.post-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #1a1a1a;
			margin-bottom: 12rpx;
		}

		.post-content {
			font-size: 28rpx;
			color: #4f4f4f;
			line-height: 1.6;
			margin-bottom: 20rpx;
		}
	}

	.media-container {
		margin-bottom: 24rpx;

		.single-img {
			width: 260rpx;
			border-radius: 12rpx;
		}

		.multi-grid {
			display: grid;
			grid-template-columns: repeat(3, 1fr);
			gap: 12rpx;

			image {
				width: 100%;
				height: 200rpx;
				border-radius: 12rpx;
				background: #f8f8f8;
			}
		}
	}

	.footer-action {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-top: 24rpx;
		border-top: 1rpx solid #f2f2f2;

		.stats-group {
			display: flex;

			.stat-item {
				margin-right: 32rpx;

				.label {
					font-size: 24rpx;
					color: #999;
					margin-right: 6rpx;
				}

				.value {
					font-size: 26rpx;
					color: #666;
					font-weight: 500;
				}
			}
		}

		.like-btn {
			display: flex;
			align-items: center;
			padding: 12rpx 28rpx;
			background: #f7f8fa;
			border-radius: 30rpx;

			.heart-icon {
				font-size: 28rpx;
				margin-right: 8rpx;
			}

			.like-text {
				font-size: 26rpx;
				color: #666;
			}

			&.is-liked {
				background: rgba(255, 77, 79, 0.08);

				.like-text {
					color: #ff4d4f;
					font-weight: bold;
				}
			}
		}
	}
</style>