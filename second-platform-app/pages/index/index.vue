<template>
	<view class="home-container">
		<view class="color-bg"></view>
		<view class="texture-mask"></view>

		<scroll-view scroll-y="true" class="content-scroll">
			<view class="header-section">
				<view class="hero-area">
					<text class="title-text text-left">同学</text>
					<image class="character-img" src="/static/indexro.png" mode="aspectFit"></image>
					<text class="title-text text-right">~你好呀！</text>
				</view>

				<view class="search-bar">
					<view class="search-input-box" @click="goSearchPage">
						<icon type="search" size="16" color="#999" />
						<input class="input" type="text" placeholder="艺术史论课堂笔记" placeholder-style="color:#999" />
					</view>
					<view class="search-btn">搜索</view>
				</view>
			</view>

			<view class="grid-nav">
				<view class="nav-item" v-for="(item, index) in navList" :key="index" @click="tiaotiao(item)">
					<image :src="item.icon" class="nav-icon" mode="aspectFill"></image>
					<text class="nav-text">{{ item.name }}</text>
				</view>
			</view>

			<view class="tile-section">
				<view class="tile-card alumni-circle" @click="goToRecognitiontiezi">
					<view class="card-decor-gradient"></view>
					<image src="/static/indextwo.png" class="tile-bg" mode="aspectFill"></image>
					<view class="tile-mask">
						<text class="tile-title">校友圈交流</text>
						<view class="tile-desc">Connect with Alumni</view>
					</view>
				</view>

				<view class="tile-card wiki-circle" @click="goToRecognition">
					<view class="card-decor-gradient"></view>
					<image src="/static/indexthree.png" class="tile-bg-two" mode="aspectFill"></image>
					<view class="tile-mask">
						<text class="tile-title">识物上传</text>
						<view class="tile-desc">Campus Wiki</view>
					</view>
				</view>
			</view>

			<view class="ad-banner" v-if="adData">
				<view class="ad-left">
					<image :src="adData.icon" class="ad-icon" mode="aspectFit"></image>
					<text class="ad-text">{{ adData.title }}</text>
				</view>
				<view class="ad-btn">去逛逛</view>
			</view>

			<scroll-view scroll-x class="label-scroll" :show-scrollbar="false">
				<view class="label-list">
					<view v-for="(item, index) in labelList" :key="item.id"
						:class="['label-item', activeLabel === index ? 'active' : '']"
						@tap="handleLabelClick(item.id, index)">
						{{ item.label }}
					</view>
				</view>
			</scroll-view>

			<view class="list-container">
				<view v-if="loading || !infoList || infoList.length === 0" class="loading-state">
					<view class="loading-spinner"></view>
					<text class="loading-text">数据加载中~</text>
				</view>

				<view v-else class="data-list">
					<view class="list-card" v-for="(item, index) in infoList" :key="index" @click="toDetail(item)">
						<view class="card-left">
							<image :src="item.image" mode="aspectFill" class="cover-img"></image>
						</view>
						<view class="card-right">
							<view class="tag-row">
								<text class="card-tag">{{ item.tag }}</text>
							</view>
							<view class="card-title">{{ item.title }}</view>
							<view class="card-price">￥{{ item.price }}</view>
							<view class="card-user-info">
								<view class="user-main">
									<image :src="item.sellerAvatar" class="avatar" mode="aspectFill"></image>
									<text class="username">{{ item.sellerNickname }}</text>
								</view>
								<text class="post-time">{{ item.publishTimeAgo }}</text>
							</view>
						</view>
					</view>
				</view>
			</view>

			<view class="footer-gap"></view>
		</scroll-view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive
	} from 'vue'

	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app';

	import {
		getGoodsTypeList,
		getGoodsList
	} from '@/apis/goods/goodstopandtype.js';

	// 加载状态控制
	const loading = ref(false)

	// 导航列表
	const navList = ref([{
			name: '爱心捐助',
			icon: '../../static/indexicon/three.png',
			page: '/pages/trip/trip'
		},
		{
			name: '衣旧有爱',
			icon: '../../static/indexicon/eight.png',
			page: '/pages/lost/lost'
		},
		{
			name: '消息探探',
			icon: '../../static/indexicon/seven.png',
			page: '/pages/message/message'
		},

		{
			name: '二手市场集',
			icon: '../../static/indexicon/six.png',
			page: '/pages/market/market'
		},
		{
			name: '二手出售险',
			icon: '../../static/indexicon/five.png',
			page: '/pages/insurance/insurance'
		}
	])

	const adData = reactive({
		title: '今日推送：暑期社会实践开启报名！',
		icon: 'https://web-springboot-tali.oss-cn-beijing.aliyuncs.com/upload/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20260330222106_325_7.png'
	})

	const labelList = ref([])
	const activeLabel = ref(0)
	const infoList = ref([])


	const goSearchPage = () => {
		uni.navigateTo({
			url: '/pages/search/index'
		})
	}

	const goToRecognition = () => {
		uni.navigateTo({
			url: '/pages/add/index'
		})
	}

	const tiaotiao = (item) => {
		console.log(item);
		if (item.name === '爱心捐助') {
			uni.navigateTo({
				url: '/pages/juanzeng/index'
			})
		} else if (item.name === '消息探探') {
			uni.navigateTo({
				url: '/pages/xiaoyuan/index'
			})
		} else if (item.name === '衣旧有爱') {
			uni.navigateTo({
				url: '/pages/juanzeng/list'
			})
		}
	}

	const goToRecognitiontiezi = () => {
		uni.navigateTo({
			url: '/pages/tiezi/index'
		})
	}

	const handleLabelClick = (id, index) => {
		activeLabel.value = index
		loadDataByLabelId(id)
	}

	const loadDataByLabelId = async (labelId) => {
		console.log('加载分类ID:', labelId)
		await getGoodsListEven(labelId)
	}

	const toDetail = (item) => {
		uni.navigateTo({
			url: `/pages/gooddetail/index?id=${item.id}`
		})
	}

	const toUserProfile = (username) => {
		uni.navigateTo({
			url: `/pages/user/profile?username=${username}`
		})
	}

	// 获取商品列表
	const getGoodsListEven = async (typeId = null) => {
		loading.value = true // 开始加载
		try {
			const currentTypeId = typeId !== null ? typeId : (labelList.value[activeLabel.value]?.id || 1)
			const params = {
				pageNum: 1,
				pageSize: 10,
				typeId: currentTypeId
			}

			const res = await getGoodsList(params)

			// 模拟请求延迟，便于观察 Loading 效果（实际开发可移除）
			// await new Promise(resolve => setTimeout(resolve, 800));

			if (res?.data.data && Array.isArray(res.data.data)) {
				infoList.value = res.data.data.list
			} else if (res?.data && typeof res.data === 'object') {
				infoList.value = res.data.data.list
			} else {
				infoList.value = []
			}
		} catch (error) {
			console.error('获取商品列表失败:', error)
			infoList.value = []
		} finally {
			loading.value = false // 结束加载
		}
	}

	const getList = async () => {
		loading.value = true
		try {
			const res = await getGoodsTypeList()
			if (res?.data && Array.isArray(res.data)) {
				labelList.value = res.data
				if (labelList.value.length > 0) {
					activeLabel.value = 0
					const firstTypeId = labelList.value[0].id
					await getGoodsListEven(firstTypeId)
				} else {
					await getGoodsListEven(1)
				}
			} else {
				await getGoodsListEven(1)
			}
		} catch (error) {
			await getGoodsListEven(1)
		} finally {
			// getGoodsListEven 内部已经处理了 loading.value = false
		}
	}

	onShow(() => {
		getList()
	})
</script>

<style lang="scss" scoped>
	$status-bar-height: 25px;
	$theme-color: #D4FB1B;

	/* --- 新增加载组件样式 --- */
	.list-container {
		min-height: 400rpx;
		display: flex;
		flex-direction: column;
	}

	.loading-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 0;
		width: 100%;

		.loading-spinner {
			width: 60rpx;
			height: 60rpx;
			border: 6rpx solid rgba(212, 251, 27, 0.2);
			border-top: 6rpx solid $theme-color;
			border-radius: 50%;
			animation: spin 1s linear infinite;
		}

		.loading-text {
			margin-top: 24rpx;
			font-size: 24rpx;
			color: $theme-color;
			font-weight: 500;
			letter-spacing: 2rpx;
		}
	}

	@keyframes spin {
		0% {
			transform: rotate(0deg);
		}

		100% {
			transform: rotate(360deg);
		}
	}

	/* --- 原有样式保持不变 --- */
	.home-container {
		position: relative;
		width: 100%;
		height: 100vh;
		background-color: #f8f9fb;
		overflow: hidden;
	}

	.color-bg {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 40vh;
		background: linear-gradient(180deg, #D4FB1B 0%, rgba(212, 251, 27, 0) 100%);
		z-index: 0;
	}

	.texture-mask {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 350rpx;
		background-image: radial-gradient(#c6e916 2px, transparent 2px);
		background-size: 20px 20px;
		opacity: 0.2;
		z-index: 1;
	}

	.content-scroll {
		position: relative;
		width: 100%;
		height: 100%;
		z-index: 2;
	}

	.header-section {
		padding: calc(#{$status-bar-height} + 30rpx) 30rpx 20rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.hero-area {
		width: 100%;
		height: 200rpx;
		display: flex;
		justify-content: center;
		align-items: flex-end;
		position: relative;

		.title-text {
			position: absolute;
			font-size: 44rpx;
			font-weight: 900;
			color: #333;
			font-style: italic;
			z-index: 3;
			text-shadow: 2rpx 2rpx 0px #fff;
		}

		.text-left {
			top: 10rpx;
			left: 150rpx;
		}

		.text-right {
			top: 10rpx;
			right: 40rpx;
		}

		.character-img {
			width: 360rpx;
			height: 200rpx;
			position: relative;
			z-index: 2;
		}
	}

	.search-bar {
		width: 100%;
		height: 88rpx;
		background: #fff;
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		padding: 0 8rpx 0 30rpx;
		box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.08);
		margin-top: -30rpx;
		z-index: 10;
		box-sizing: border-box;

		.search-input-box {
			flex: 1;
			display: flex;
			align-items: center;

			.input {
				flex: 1;
				margin-left: 10rpx;
				font-size: 26rpx;
			}
		}

		.search-btn {
			width: 130rpx;
			height: 74rpx;
			background: #FEE01D;
			border-radius: 37rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 26rpx;
			font-weight: bold;
			box-shadow: 0 4rpx 10rpx rgba(254, 224, 29, 0.4);
		}
	}

	.grid-nav {
		display: flex;
		justify-content: space-between;
		padding: 40rpx 20rpx;

		.nav-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 20%;

			.nav-icon {
				width: 90rpx;
				height: 90rpx;
				border-radius: 24rpx;
				margin-bottom: 12rpx;
			}

			.nav-text {
				font-size: 22rpx;
				color: #333;
			}
		}
	}

	.tile-section {
		display: flex;
		justify-content: space-between;
		padding: 0 30rpx;

		.tile-card {
			width: 48%;
			height: 240rpx;
			border-radius: 32rpx;
			position: relative;
			overflow: hidden;
			box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.1), inset 0 -8rpx 0 rgba(0, 0, 0, 0.1);
			transition: transform 0.2s;

			&:active {
				transform: scale(0.97);
			}

			.tile-bg {
				width: 100%;
				height: 100%;
				position: absolute;
				top: 0;
				left: 0;
				z-index: 10;
				pointer-events: none;
			}

			.tile-bg-two {
				width: 60%;
				height: 60%;
				position: absolute;
				top: 80rpx;
				left: 130rpx;
				z-index: 10;
				pointer-events: none;
			}

			.card-decor-gradient {
				position: absolute;
				top: -50%;
				left: -50%;
				width: 200%;
				height: 200%;
				background: linear-gradient(45deg, rgba(255, 255, 255, 0) 45%, rgba(255, 255, 255, 0.15) 50%, rgba(255, 255, 255, 0) 55%);
				z-index: 2;
				animation: shine 4s infinite;
			}

			.tile-mask {
				position: absolute;
				left: 0;
				top: 0;
				width: 100%;
				height: 100%;
				display: flex;
				flex-direction: column;
				padding: 30rpx 24rpx;
				box-sizing: border-box;
				z-index: 5;
				background: rgba(255, 255, 255, 0.2);
				backdrop-filter: blur(4rpx);

				.tile-title {
					font-size: 34rpx;
					font-weight: 800;
					color: #333;
					letter-spacing: 2rpx;
				}

				.tile-desc {
					font-size: 18rpx;
					color: rgba(0, 0, 0, 0.5);
					font-weight: bold;
					text-transform: uppercase;
					margin-top: 4rpx;
				}
			}

			&.alumni-circle {
				background: linear-gradient(135deg, #d4ff12 0%, #b8e200 100%);
				border: 4rpx solid #e2ff5e;

				.tile-mask {
					align-items: flex-start;
				}
			}

			&.wiki-circle {
				background: linear-gradient(135deg, #12d4ff 0%, #00b8e2 100%);
				border: 4rpx solid #5effe2;

				.tile-mask {
					align-items: flex-start;
				}
			}
		}
	}

	@keyframes shine {
		0% {
			transform: translateX(-100%) translateY(-100%);
		}

		20% {
			transform: translateX(100%) translateY(100%);
		}

		100% {
			transform: translateX(100%) translateY(100%);
		}
	}

	.ad-banner {
		margin: 40rpx 30rpx;
		padding: 20rpx 24rpx;
		background: #fff;
		border-radius: 24rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 10rpx 20rpx rgba(0, 0, 0, 0.03);

		.ad-left {
			display: flex;
			align-items: center;
			flex: 1;

			.ad-icon {
				width: 36rpx;
				height: 36rpx;
			}

			.ad-text {
				font-size: 24rpx;
				color: #333;
				margin-left: 14rpx;
				font-weight: 500;
			}
		}

		.ad-btn {
			font-size: 22rpx;
			color: #000;
			padding: 12rpx 24rpx;
			background: #FEE01D;
			border-radius: 30rpx;
			font-weight: bold;
		}
	}

	.label-scroll {
		width: 100%;
		white-space: nowrap;
		margin-bottom: 20rpx;

		.label-list {
			padding: 10rpx 30rpx;

			.label-item {
				display: inline-block;
				padding: 14rpx 38rpx;
				background: #fff;
				border-radius: 40rpx;
				font-size: 26rpx;
				color: #999;
				margin-right: 20rpx;
				box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.02);

				&.active {
					background: #333;
					color: $theme-color;
					font-weight: bold;
					box-shadow: 0 10rpx 20rpx rgba(0, 0, 0, 0.15);
				}
			}
		}
	}

	.data-list {
		padding: 0 30rpx;

		.list-card {
			display: flex;
			background: #fff;
			border-radius: 28rpx;
			padding: 24rpx;
			margin-bottom: 28rpx;
			box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.04);

			.card-left {
				width: 200rpx;
				height: 200rpx;
				flex-shrink: 0;

				.cover-img {
					width: 100%;
					height: 100%;
					border-radius: 20rpx;
				}
			}

			.card-right {
				flex: 1;
				margin-left: 24rpx;
				display: flex;
				flex-direction: column;

				.card-tag {
					font-size: 20rpx;
					color: #8cc63f;
					background: rgba(140, 198, 63, 0.1);
					padding: 4rpx 12rpx;
					border-radius: 8rpx;
					width: fit-content;
				}

				.card-title {
					width: 350rpx;
					font-size: 30rpx;
					font-weight: bold;
					color: #333;
					margin-top: 12rpx;
					line-height: 1.4;
					overflow: hidden;
					text-overflow: ellipsis;
					display: -webkit-box;
					-webkit-line-clamp: 2;
					/* 控制行数，1表示一行，2表示两行 */
					-webkit-box-orient: vertical;
					word-break: break-all;

				}

				.card-price {
					font-size: 34rpx;
					color: #ff4d4f;
					font-weight: bold;
					margin-top: 10rpx;
				}

				.card-user-info {
					margin-top: auto;
					display: flex;
					justify-content: space-between;
					align-items: center;

					.user-main {
						display: flex;
						align-items: center;

						.avatar {
							width: 36rpx;
							height: 36rpx;
							border-radius: 50%;
							margin-right: 10rpx;
						}

						.username {
							font-size: 24rpx;
							color: #666;
						}
					}

					.post-time {
						font-size: 22rpx;
						color: #bbb;
					}
				}
			}
		}
	}

	.footer-gap {
		height: 80rpx;
	}
</style>