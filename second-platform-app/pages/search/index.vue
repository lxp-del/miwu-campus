<template>
	<view class="search-page">
		<view class="search-header">
			<view class="back-icon" @click="goBack">
				<uni-icons type="left" size="24" color="#333"></uni-icons>
			</view>
			<view class="search-input-box">
				<uni-icons type="search" size="18" color="#999"></uni-icons>
				<input class="input" type="text" v-model="keyword" placeholder="艺术史论课堂笔记" @input="onSearchInput"
					confirm-type="search" @confirm="handleSearch(keyword)" />
				<uni-icons v-if="keyword" @click="clearSearch" type="clear" size="20" color="#ccc"></uni-icons>
			</view>
			<view class="search-btn" @click="handleSearch(keyword)">搜索</view>
		</view>

		<scroll-view scroll-y class="suggestion-list" v-if="keyword && suggestList.length > 0">
			<view class="suggestion-item" v-for="item in suggestList" :key="item.id" @click="goToDetail(item.id)">
				<rich-text :nodes="highlightText(item.title, keyword)"></rich-text>
				<uni-icons type="arrowright" size="14" color="#EEE"></uni-icons>
			</view>
		</scroll-view>

		<view class="default-view" v-else-if="!keyword">

			<view class="section-container" v-if="historyList.length > 0">
				<view class="section-head">
					<text class="title-text">最近搜索</text>
					<uni-icons @click="clearHistory" type="trash" size="18" color="#999"></uni-icons>
				</view>
				<view class="history-tags">
					<view class="tag" v-for="(tag, index) in historyList" :key="index" @click="handleSearch(tag)">
						{{tag}}
					</view>
				</view>
			</view>

			<view class="section-container">
				<view class="section-head">
					<text class="title-text bold">猜你想搜</text>
					<view class="refresh-box" @click="getList">
						<uni-icons type="refreshempty" size="14" color="#999"></uni-icons>
						<text class="refresh-text">换一批</text>
					</view>
				</view>
				<view class="guess-grid">
					<view class="guess-item" v-for="(item, index) in guessList" :key="item.id"
						@click="goToDetail(item.id)">
						<text class="guess-index" :class="{'top-three': index < 3}">{{index + 1}}</text>
						<text class="guess-title">{{ formatTitle(item.title) }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="empty-box" v-else>
			<text>没有找到相关结果</text>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue'
	import {
		onShow
	} from '@dcloudio/uni-app';
	import {
		getGoodsListAll
	} from '@/apis/goods/goodstopandtype.js';

	const keyword = ref('');
	const goodInfo = ref([]);
	const suggestList = ref([]);
	const historyList = ref([]);
	const guessList = ref([]);

	let timer = null;

	// 返回上一页
	const goBack = () => {
		uni.navigateBack();
	}

	// 获取商品数据并随机初始化“猜你想搜”
	const getList = () => {
		getGoodsListAll().then(res => {
			goodInfo.value = res.data || [];
			// 模拟“猜你想搜”：随机取8条
			guessList.value = [...goodInfo.value].sort(() => 0.5 - Math.random()).slice(0, 8);
		})
	}

	// 标题截断：前8个字+省略号
	const formatTitle = (title) => {
		if (!title) return '';
		return title.length > 8 ? title.substring(0, 8) + '...' : title;
	}

	// 从缓存加载历史记录
	const loadHistory = () => {
		const cache = uni.getStorageSync('search_history');
		historyList.value = cache ? JSON.parse(cache) : [];
	}

	onShow(() => {
		getList();
		loadHistory();
	})

	// 联想模糊匹配
	const onSearchInput = () => {
		if (timer) clearTimeout(timer);
		timer = setTimeout(() => {
			if (!keyword.value.trim()) {
				suggestList.value = [];
				return;
			}
			suggestList.value = goodInfo.value.filter(item =>
				item.title.toLowerCase().includes(keyword.value.toLowerCase())
			);
		}, 200);
	}

	// 搜索触发 (存缓存)
	const handleSearch = (val) => {
		if (!val.trim()) return;
		keyword.value = val;

		let history = [...historyList.value];
		history = history.filter(item => item !== val);
		history.unshift(val);
		history = history.slice(0, 10); // 存10条

		historyList.value = history;
		uni.setStorageSync('search_history', JSON.stringify(history));

		// 此处可根据业务需求跳转结果列表页或在当前页展示
		console.log('执行搜索逻辑:', val);
	}

	// 清空缓存
	const clearHistory = () => {
		uni.showModal({
			title: '提示',
			content: '确定清空所有搜索历史？',
			confirmColor: '#D4FB1B',
			success: (res) => {
				if (res.confirm) {
					historyList.value = [];
					uni.removeStorageSync('search_history');
				}
			}
		});
	}

	const clearSearch = () => {
		keyword.value = '';
		suggestList.value = [];
	}

	// 高亮关键词
	const highlightText = (text, key) => {
		const reg = new RegExp(key, 'gi');
		return text.replace(reg,
			`<span style="color:#000; background:#D4FB1B; font-weight:bold; border-radius:4rpx; padding:0 4rpx;">${key}</span>`
			);
	}

	const goToDetail = (id) => {
		uni.navigateTo({
			url: `/pages/gooddetail/index?id=${id}`
		})
	}
</script>

<style lang="scss" scoped>
	.search-page {
		min-height: 100vh;
		background-color: #fff;
	}

	/* 顶部搜索栏布局 */
	.search-header {
		display: flex;
		align-items: center;
		padding: 20rpx 20rpx 20rpx 10rpx;
		background-color: #fff;
		position: sticky;
		top: 0;
		z-index: 99;

		.back-icon {
			padding: 10rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.search-input-box {
			flex: 1;
			height: 72rpx;
			background-color: #F2F2F2;
			border-radius: 36rpx;
			display: flex;
			align-items: center;
			padding: 0 24rpx;
			margin-left: 10rpx;

			.input {
				flex: 1;
				margin-left: 14rpx;
				font-size: 28rpx;
			}
		}

		.search-btn {
			margin-left: 24rpx;
			font-size: 30rpx;
			font-weight: bold;
			color: #333;
		}
	}

	/* 联想列表样式 */
	.suggestion-list {
		background: #fff;

		.suggestion-item {
			padding: 30rpx 40rpx;
			border-bottom: 1rpx solid #F8F8F8;
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size: 28rpx;

			&:active {
				background-color: #fcfcfc;
			}
		}
	}

	/* 公共板块容器 */
	.section-container {
		padding: 40rpx 30rpx;

		.section-head {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 28rpx;

			.title-text {
				font-size: 26rpx;
				color: #999;

				&.bold {
					font-size: 32rpx;
					color: #333;
					font-weight: bold;
				}
			}

			.refresh-box {
				display: flex;
				align-items: center;

				.refresh-text {
					font-size: 24rpx;
					color: #999;
					margin-left: 6rpx;
				}
			}
		}
	}

	/* 历史搜索标签 */
	.history-tags {
		display: flex;
		flex-wrap: wrap;

		.tag {
			background-color: #F7F7F7;
			padding: 10rpx 30rpx;
			border-radius: 30rpx;
			font-size: 24rpx;
			color: #333;
			margin-right: 20rpx;
			margin-bottom: 20rpx;

			&:active {
				background-color: #D4FB1B;
			}
		}
	}

	/* 猜你想搜 - 网格布局 */
	.guess-grid {
		display: grid;
		grid-template-columns: 1fr 1fr; // 一行两排
		column-gap: 40rpx;

		.guess-item {
			display: flex;
			align-items: center;
			height: 84rpx;
			border-bottom: 1rpx solid #F9F9F9;

			.guess-index {
				font-size: 28rpx;
				color: #CCC;
				width: 36rpx;
				font-weight: 500;
				font-style: italic;

				&.top-three {
					color: #FF5500; // 前三名橙色加亮
					font-weight: bold;
				}
			}

			.guess-title {
				flex: 1;
				font-size: 28rpx;
				color: #333;
				/* 文本溢出处理 */
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}

			&:active {
				opacity: 0.7;
			}
		}
	}

	.empty-box {
		padding: 100rpx;
		text-align: center;
		color: #CCC;
		font-size: 26rpx;
	}
</style>