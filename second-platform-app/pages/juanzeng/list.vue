<template>
	<view class="donation-list-page">
		<!-- 新增：返回首页按钮 -->
		<view class="back-home" @click="goToHome">
			<uni-icons type="home" size="20" color="#333"></uni-icons>
		</view>

		<view class="search-bar">
			<view class="search-input">
				<uni-icons type="search" size="18" color="#666"></uni-icons>
				<input type="text" placeholder="搜索你感兴趣的宝贝" v-model="searchKeyword" @confirm="handleSearch" />
			</view>
			<view class="filter-btn" @click="showFilter = true">
				<text class="filter-text">筛选</text>
				<uni-icons type="tune" size="18" color="#333"></uni-icons>
			</view>
		</view>

		<scroll-view class="category-scroll" scroll-x="true" :show-scrollbar="false">
			<view v-for="(category, index) in categories" :key="index" class="category-item"
				:class="{ active: selectedCategory === category.value }" @click="selectCategory(category.value)">
				<text class="category-label">{{ category.label }}</text>
				<view class="active-line" v-if="selectedCategory === category.value"></view>
			</view>
		</scroll-view>

		<view class="waterfall-container">
			<view class="waterfall-column" v-for="(column, index) in 2" :key="index">
				<view v-for="(item, itemIndex) in getColumnItems(index)" :key="item.id" class="donation-card"
					@click="goToDetail(item.id)">
					<view class="card-image-wrap">
						<image :src="getFirstImage(item.images)" mode="aspectFill" class="card-image"></image>
						<view class="status-badge" v-if="item.status !== 'available'">
							{{ getStatusLabel(item.status) }}
						</view>
					</view>

					<view class="card-content">
						<text class="card-title">{{ item.title }}</text>

						<view class="price-section">
							 
							<text class="price-value">自愿捐赠</text>
							<text class="price-free" v-if="item.price <= 0">送TA</text>
						</view>

						<view class="card-footer">
							<view class="user-info">
								<image :src="item.userAvatar || '/static/logo.png'" mode="aspectFill"
									class="user-avatar"></image>
								<text class="user-name">{{ item.userName }}</text>
							</view>
							<text class="card-time">{{ formatTime(item.createdAt) }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="load-more">
			<uni-load-more :status="loading ? 'loading' : (noMore ? 'noMore' : 'more')" color="#999"></uni-load-more>
		</view>

		<uni-popup ref="popup" type="bottom">
			<view class="filter-popup">
				<view class="filter-header">
					<text class="filter-title">精细筛选</text>
					<uni-icons type="closeempty" size="20" color="#999" @click="showFilter = false"></uni-icons>
				</view>

				<view class="filter-body">
					<view class="filter-section">
						<text class="section-title">选择校区</text>
						<view class="option-grid">
							<view v-for="(campus, index) in campusList" :key="index" class="option-item"
								:class="{ active: selectedCampus === campus }" @click="selectedCampus = campus">
								{{ campus }}
							</view>
						</view>
					</view>

					<view class="filter-section">
						<text class="section-title">宝贝状态</text>
						<view class="option-grid">
							<view v-for="(status, index) in statusList" :key="index" class="option-item"
								:class="{ active: selectedStatus === status.value }"
								@click="selectedStatus = status.value">
								{{ status.label }}
							</view>
						</view>
					</view>
				</view>

				<view class="filter-footer">
					<view class="reset-btn" @click="resetFilter">重置</view>
					<view class="confirm-btn" @click="confirmFilter">确定</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		computed,
		onMounted,
		watch
	} from 'vue';
	import {
		getDonationList
	} from '@/apis/donation/donation.js';

	const searchKeyword = ref('');
	const selectedCategory = ref('');
	const selectedCampus = ref('');
	const selectedStatus = ref('');
	const showFilter = ref(false);
	const loading = ref(false);
	const noMore = ref(false);
	const page = ref(1);
	const pageSize = ref(10);
	const donationItems = ref([]);
	const popup = ref(null);

	// 监听弹窗显示
	watch(showFilter, (val) => {
		if (val) popup.value.open();
		else popup.value.close();
	});

	const categories = [{
			label: '全部',
			value: ''
		},
		{
			label: '书籍',
			value: '书籍'
		},
		{
			label: '电子产品',
			value: '电子产品'
		},
		{
			label: '生活用品',
			value: '生活用品'
		},
		{
			label: '其他',
			value: '其他'
		}
	];

	const campusList = ['全部', '花津', '赭山', '天门山'];
	const statusList = [{
			label: '全部',
			value: ''
		},
		{
			label: '可捐赠',
			value: 'available'
		},
		{
			label: '已完成',
			value: 'traded'
		}
	];

	// 新增：跳转到首页
	const goToHome = () => {
		uni.switchTab({
			url: '/pages/index/index'
		});
	};

	const getColumnItems = (columnIndex) => {
		return donationItems.value.filter((_, index) => index % 2 === columnIndex);
	};

	const getFirstImage = (images) => {
		if (!images) return '/static/logo.png';
		const imageList = images.split(',');
		return imageList[0] || '/static/logo.png';
	};

	const getStatusLabel = (val) => {
		const status = statusList.find(s => s.value === val);
		return status ? status.label : '';
	};

	const formatTime = (timeStr) => {
		if (!timeStr) return '';
		const date = new Date(timeStr);
		const now = new Date();
		const diff = now - date;
		const days = Math.floor(diff / (1000 * 60 * 60 * 24));
		if (days === 0) return '刚刚';
		if (days < 7) return days + '天前';
		return (date.getMonth() + 1) + '-' + date.getDate();
	};

	const selectCategory = (category) => {
		selectedCategory.value = category;
		resetPage();
		loadDonationList();
	};

	const resetFilter = () => {
		selectedCampus.value = '全部';
		selectedStatus.value = '';
	};

	const confirmFilter = () => {
		showFilter.value = false;
		resetPage();
		loadDonationList();
	};

	const handleSearch = () => {
		resetPage();
		loadDonationList();
	};

	const resetPage = () => {
		page.value = 1;
		donationItems.value = [];
		noMore.value = false;
	};

	const loadDonationList = async () => {
		if (loading.value || noMore.value) return;
		loading.value = true;
		try {
			const params = {
				page: page.value,
				pageSize: pageSize.value,
				category: selectedCategory.value,
				campus: selectedCampus.value === '全部' ? '' : selectedCampus.value,
				keyword: searchKeyword.value,
				status: selectedStatus.value
			};
			const res = await getDonationList(params);
			if (res.data) {
				if (page.value === 1) donationItems.value = res.data.items;
				else donationItems.value = [...donationItems.value, ...res.data.items];
				if (res.data.items.length < pageSize.value) noMore.value = true;
				else page.value++;
			}
		} catch (error) {
			uni.showToast({
				title: '加载失败',
				icon: 'none'
			});
		} finally {
			loading.value = false;
		}
	};

	const goToDetail = (id) => {
		uni.navigateTo({
			url: `/pages/juanzeng/detail?id=${id}`
		});
	};

	onMounted(() => {
		loadDonationList();
		uni.onReachBottom(() => loadDonationList());
	});
</script>

<style scoped>
	.donation-list-page {
		min-height: 100vh;
		background-color: #F7F8FA;
		position: relative;
	}

	/* 新增：返回首页按钮样式 */
	.back-home {
		position: absolute;
		top: 20rpx;
		left: 20rpx;
		z-index: 101;
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: rgba(255, 255, 255, 0.9);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}

	/* 搜索栏：闲鱼扁圆风格 */
	.search-bar {
		display: flex;
		align-items: center;
		padding: 15rpx 25rpx 15rpx 100rpx; /* 增加左侧内边距，为返回按钮留出空间 */
		background-color: #FFFFFF;
		position: sticky;
		top: 0;
		z-index: 100;
	}

	.search-input {
		flex: 1;
		display: flex;
		align-items: center;
		background-color: #F2F2F2;
		border-radius: 100rpx;
		padding: 12rpx 30rpx;
		margin-right: 20rpx;
	}

	.search-input input {
		flex: 1;
		margin-left: 15rpx;
		font-size: 26rpx;
		color: #333;
	}

	.filter-btn {
		display: flex;
		align-items: center;
	}

	.filter-text {
		font-size: 28rpx;
		margin-right: 4rpx;
		color: #333;
	}

	/* 分类标签：极简线型 */
	.category-scroll {
		white-space: nowrap;
		background-color: #FFFFFF;
		padding: 10rpx 0 20rpx 0;
	}

	.category-item {
		display: inline-flex;
		flex-direction: column;
		align-items: center;
		padding: 0 30rpx;
		position: relative;
	}

	.category-label {
		font-size: 28rpx;
		color: #666;
		transition: all 0.2s;
	}

	.category-item.active .category-label {
		color: #333;
		font-weight: bold;
		font-size: 30rpx;
	}

	.active-line {
		width: 40rpx;
		height: 6rpx;
		background-color: #D4FB1B;
		border-radius: 3rpx;
		margin-top: 8rpx;
	}

	/* 瀑布流容器 */
	.waterfall-container {
		display: flex;
		padding: 15rpx;
		gap: 15rpx;
	}

	.waterfall-column {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 15rpx;
	}

	/* 捐赠卡片：圆角与投影 */
	.donation-card {
		background-color: #FFFFFF;
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.03);
	}

	.card-image-wrap {
		position: relative;
		width: 100%;
		height: 0;
		padding-bottom: 100%;
	}

	.card-image {
		position: absolute;
		width: 100%;
		height: 100%;
	}

	.status-badge {
		position: absolute;
		bottom: 0;
		right: 0;
		background-color: rgba(0, 0, 0, 0.6);
		color: #fff;
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		border-top-left-radius: 12rpx;
	}

	.card-content {
		padding: 16rpx;
	}

	.card-title {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		line-height: 1.4;
		margin-bottom: 12rpx;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}

	.price-section {
		display: flex;
		align-items: baseline;
		margin-bottom: 16rpx;
	}

	.price-symbol {
		color: #FF4D4F;
		font-size: 22rpx;
		font-weight: bold;
	}

	.price-value {
		color: #FF4D4F;
		font-size: 36rpx;
		font-weight: bold;
		margin-right: 8rpx;
	}

	.price-free {
		font-size: 20rpx;
		color: #FF4D4F;
		border: 1rpx solid #FF4D4F;
		padding: 0 6rpx;
		border-radius: 4rpx;
	}

	.card-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-top: 1rpx solid #F5F5F5;
		padding-top: 12rpx;
	}

	.user-info {
		display: flex;
		align-items: center;
	}

	.user-avatar {
		width: 32rpx;
		height: 32rpx;
		border-radius: 50%;
		margin-right: 8rpx;
	}

	.user-name {
		font-size: 22rpx;
		color: #999;
	}

	.card-time {
		font-size: 20rpx;
		color: #CCC;
	}

	/* 筛选弹窗 */
	.filter-popup {
		background-color: #fff;
		border-radius: 30rpx 30rpx 0 0;
		padding: 40rpx 30rpx;
	}

	.filter-header {
		display: flex;
		justify-content: space-between;
		margin-bottom: 40rpx;
	}

	.filter-title {
		font-size: 32rpx;
		font-weight: bold;
	}

	.filter-section {
		margin-bottom: 40rpx;
	}

	.section-title {
		font-size: 28rpx;
		font-weight: bold;
		margin-bottom: 20rpx;
		display: block;
	}

	.option-grid {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}

	.option-item {
		padding: 12rpx 30rpx;
		background-color: #F5F5F5;
		border-radius: 8rpx;
		font-size: 26rpx;
		color: #666;
	}

	.option-item.active {
		background-color: #FFFBE6;
		color: #FFC300;
		border: 1rpx solid #D4FB1B;
	}

	.filter-footer {
		display: flex;
		gap: 20rpx;
		margin-top: 60rpx;
		padding-bottom: env(safe-area-inset-bottom);
	}

	.reset-btn {
		flex: 1;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		background-color: #F5F5F5;
		border-radius: 40rpx;
		font-size: 28rpx;
	}

	.confirm-btn {
		flex: 2;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		background-color: #D4FB1B;
		border-radius: 40rpx;
		font-size: 28rpx;
		font-weight: bold;
	}
</style>