<template>
	<view class="match-page">
		<view class="nav-header">
			<view class="status-bar"></view>
			<view class="nav-content">
				<view class="nav-left" @tap="goBack">
					<text class="back-icon">❮</text>
				</view>

				<view class="nav-title">校园匹配</view>

				<view class="nav-right" @tap="handleRefresh">
					<text class="refresh-icon">🔄</text>
				</view>
			</view>
		</view>

		<view class="filter-sticky">
			<view class="search-bar">
				<text class="search-icon"></text>
				<input class="search-input" v-model="searchQuery" placeholder="搜索你需要的商品或需求..."
					@confirm="onFilterChange" />
			</view>
			<scroll-view scroll-x class="tab-scroll" :show-scrollbar="false">
				<view class="tab-container">
					<view v-for="tab in categories" :key="tab" :class="['tab-item', activeTab === tab ? 'active' : '']"
						@tap="switchTab(tab)">
						{{ tab }}
					</view>
				</view>
			</scroll-view>
		</view>

		<scroll-view scroll-y class="list-container" @scrolltolower="loadMore" refresher-enabled
			:refresher-triggered="isRefreshing" @refresherrefresh="handleRefresh">
			<view class="match-list">
				<view class="match-card" v-for="item in filteredList" :key="item.id" @tap="goToDetail(item.id)">
					<image class="card-img" :src="item.images[0]" mode="aspectFill" />

					<view class="card-content">
						<view class="card-title">{{ item.title }}</view>

						<view class="match-tag-box">
							<text :class="['match-tag', getMatchClass(item.matchScore)]">
								{{ item.matchScore }}% 匹配
							</text>
							<text class="campus-tag">{{ item.campus }}</text>
						</view>

						<view class="card-footer">
							<view class="price-box">
								<text class="price-symbol">¥</text>
								<text class="price-value">{{ item.price }}</text>
							</view>

							<view class="seller-info">
								<text class="seller-name">{{ item.sellerName }}</text>
								<button class="contact-btn" @tap.stop="handleContact">联系</button>
							</view>
						</view>
					</view>
				</view>
			</view>

			<view class="load-status">
				<text v-if="isLoading">加载中...</text>
				<text v-else-if="filteredList.length === 0">暂无匹配信息</text>
				<text v-else>已经到底啦 ~</text>
			</view>
		</scroll-view>
	</view>
</template>

<script setup lang="ts">
	import { ref, reactive, computed } from 'vue';

	// --- 数据结构定义 ---
	interface IMatchItem {
		id : string | number;
		title : string;
		price : number;
		images : string[];
		sellerName : string;
		campus : string;
		category : string;
		matchScore : number;
		status : 'available' | 'trading';
	}

	// --- 状态管理 ---
	const searchQuery = ref('');
	const activeTab = ref('全部');
	const isRefreshing = ref(false);
	const isLoading = ref(false);
	const categories = ['全部', '数码', '书籍', '生活用品', '运动健身'];

	// 模拟列表数据
	const matchList = ref<IMatchItem[]>([
		{
			id: 1,
			title: '考研英语红宝书 99新，附带学霸笔记',
			price: 35,
			images: ['https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/sssss8.jpg'],
			sellerName: '来晓璞',
			campus: '松江校区',
			category: '书籍',
			matchScore: 98,
			status: 'available'
		},
		{
			id: 2,
			title: '洗面奶',
			price: 4200,
			images: ['https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/xmn0.jpg'],
			sellerName: '来晓璞',
			campus: '延安路校区',
			category: '数码',
			matchScore: 85,
			status: 'available'
		},
		{
			id: 3,
			title: '游戏耳机',
			price: 15,
			images: ['https://lxp-windows.oss-cn-beijing.aliyuncs.com/upload/erji.jpg'],
			sellerName: '毕业甩卖',
			campus: '松江校区',
			category: '生活用品',
			matchScore: 72,
			status: 'available'
		},
		{
			id: 4,
			title: '罗技机械键盘 只有几个键位轻微磨损',
			price: 120,
			images: ['https://picsum.photos/200/200?random=14'],
			sellerName: '沉迷代码',
			campus: '松江校区',
			category: '数码',
			matchScore: 92,
			status: 'available'
		}
	]);

	// --- 交互逻辑 ---

	// 返回上一页
	const goBack = () => {
		uni.navigateBack({
			fail: () => {
				// 如果没有上一页，可以跳转到首页
				uni.reLaunch({ url: '/pages/index/index' });
			}
		});
	};

	const filteredList = computed(() => {
		return matchList.value.filter(item => {
			const matchCategory = activeTab.value === '全部' || item.category === activeTab.value;
			const matchSearch = item.title.toLowerCase().includes(searchQuery.value.toLowerCase());
			return matchCategory && matchSearch;
		});
	});

	const getMatchClass = (score : number) => {
		if (score >= 90) return 'high-match';
		if (score >= 70) return 'mid-match';
		return 'low-match';
	};

	const switchTab = (tab : string) => {
		activeTab.value = tab;
	};

	const handleContact = () => {
		uni.showToast({ title: '联系功能开发中', icon: 'none' });
	};

	const goToDetail = (id : string | number) => {
		uni.showToast({ title: '进入详情: ' + id, icon: 'none' });
	};

	const handleRefresh = () => {
		isRefreshing.value = true;
		setTimeout(() => {
			isRefreshing.value = false;
			uni.showToast({ title: '刷新成功' });
		}, 1000);
	};

	const loadMore = () => {
		if (isLoading.value) return;
		isLoading.value = true;
		setTimeout(() => {
			isLoading.value = false;
		}, 1000);
	};

	const onFilterChange = () => {
		console.log('执行搜索');
	};
</script>

<style scoped lang="scss">
	$bg-gray: #f5f5f5;
	$primary-yellow: #D4FB1B;
	$text-main: #333;
	$text-sub: #999;
	$price-red: #ff4d4f;

	.match-page {
		background-color: $bg-gray;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
	}

	/* 导航栏修改：实现三段式分布 */
	.nav-header {
		background-color: #ffffff;
		position: sticky;
		top: 0;
		z-index: 100;

		.status-bar {
			height: var(--status-bar-height);
		}

		.nav-content {
			height: 88rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			/* 左右分布 */
			padding: 0 30rpx;

			.nav-left {
				width: 60rpx;
				display: flex;
				align-items: center;

				.back-icon {
					font-size: 38rpx;
					color: $text-main;
					font-weight: bold;
				}
			}

			.nav-title {
				font-size: 34rpx;
				font-weight: bold;
				color: $text-main;
				flex: 1;
				text-align: center;
			}

			.nav-right {
				width: 60rpx;
				display: flex;
				align-items: center;
				justify-content: flex-end;

				.refresh-icon {
					font-size: 36rpx;
				}
			}
		}
	}

	/* 筛选与搜索吸顶 */
	.filter-sticky {
		background-color: #ffffff;
		padding: 20rpx 0 10rpx;

		.search-bar {
			margin: 0 30rpx 20rpx;
			background-color: #f2f2f2;
			height: 72rpx;
			border-radius: 36rpx;
			display: flex;
			align-items: center;
			padding: 0 30rpx;

			.search-icon {
				font-size: 28rpx;
				margin-right: 15rpx;
			}

			.search-input {
				flex: 1;
				font-size: 26rpx;
			}
		}

		.tab-scroll {
			white-space: nowrap;

			.tab-container {
				display: inline-flex;
				padding: 0 30rpx;

				.tab-item {
					padding: 10rpx 30rpx;
					font-size: 28rpx;
					color: #666;

					&.active {
						color: $text-main;
						font-weight: bold;
						position: relative;

						&::after {
							content: '';
							position: absolute;
							bottom: 0;
							left: 30%;
							width: 40%;
							height: 6rpx;
							background-color: $primary-yellow;
							border-radius: 3rpx;
						}
					}
				}
			}
		}
	}

	.list-container {
		flex: 1;
	}

	.match-list {
		padding: 24rpx;
	}

	.match-card {
		background-color: #ffffff;
		border-radius: 30rpx;
		padding: 24rpx;
		margin-bottom: 24rpx;
		display: flex;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.02);

		.card-img {
			width: 200rpx;
			height: 200rpx;
			border-radius: 20rpx;
			background-color: #f0f0f0;
		}

		.card-content {
			flex: 1;
			margin-left: 24rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.card-title {
				font-size: 28rpx;
				color: $text-main;
				font-weight: 500;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
			}

			.match-tag-box {
				margin-top: 10rpx;
				display: flex;
				align-items: center;
				gap: 12rpx;

				.match-tag {
					font-size: 20rpx;
					padding: 4rpx 12rpx;
					border-radius: 8rpx;

					&.high-match {
						background: #e6fffa;
						color: #00b894;
					}

					&.mid-match {
						background: #fff9db;
						color: #f0932b;
					}

					&.low-match {
						background: #f5f5f5;
						color: #999;
					}
				}

				.campus-tag {
					font-size: 20rpx;
					color: $text-sub;
				}
			}
		}
	}

	.card-footer {
		.price-box {
			.price-symbol {
				color: $price-red;
				font-size: 22rpx;
				font-weight: bold;
			}

			.price-value {
				color: $price-red;
				font-size: 34rpx;
				font-weight: bold;
			}
		}

		.seller-info {
			display: flex;
			justify-content: space-between;
			align-items: center;

			.seller-name {
				font-size: 24rpx;
				color: #777;
			}

			.contact-btn {
				margin: 0;
				padding: 0 24rpx;
				height: 50rpx;
				line-height: 50rpx;
				background-color: $primary-yellow;
				font-size: 22rpx;
				font-weight: bold;
				border-radius: 25rpx;

				&::after {
					border: none;
				}
			}
		}
	}

	.load-status {
		padding: 30rpx;
		text-align: center;
		font-size: 24rpx;
		color: #ccc;
	}
</style>