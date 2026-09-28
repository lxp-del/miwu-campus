<template>
	<view class="privacy-container">
		<view class="custom-nav" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-left" @tap="goBack">
				<text class="back-icon">←</text>
			</view>
			<view class="nav-title">隐私政策</view>
			<view class="nav-right"></view>
		</view>

		<scroll-view scroll-y class="content-scroll">
			<view class="header-card">
				<text class="main-title">隐私政策摘要</text>
				<text class="sub-tip">我们非常重视您的个人信息保护，本摘要将帮助您快速了解核心内容。</text>
				<view class="tag-box">
					<view class="tag">身份认证</view>
					<view class="tag">位置共享</view>
					<view class="tag">交易安全</view>
				</view>
			</view>

			<view class="article-body">
				<view class="section">
					<view class="h1">一、 我们收集的信息</view>
					<view class="p">1.1 **账号信息**：当您注册校园账号时，我们会收集您的手机号码、学生证号/学校邮箱，用于验证您的学生身份。</view>
					<view class="p">1.2 **位置信息**：为了向您展示同校或附近校区的二手物品，在您授权后，我们会收集您的地理位置信息。</view>
					<view class="p">1.3 **交易详情**：我们会记录您的订单详情、支付状态及沟通记录，以保障交易的追溯性。 </view>
				</view>

				<view class="section">
					<view class="h1">二、 信息的使用方式</view>
					<view class="p">2.1 提供、维护及改进我们的校园二手交易服务。</view>
					<view class="p">2.2 用于身份核验，确保平台环境的纯净（仅限在校生使用）。</view>
					<view class="p">2.3 发送服务通知、系统更新及必要的安全警示。</view>
				</view>

				<view class="section">
					<view class="h1">三、 信息的共享与披露</view>
					<view class="p">我们不会将您的个人信息出售给任何第三方。仅在以下情况共享：</view>
					<view class="li">• 获得您的明确授权；</view>
					<view class="li">• 与交易另一方共享必要的联系方式（如即时通讯聊天）；</view>
					<view class="li">• 法律法规要求或配合学校保卫处等部门的安全调查。</view>
				</view>

				<view class="section">
					<view class="h1">四、 您的权利</view>
					<view class="p">您可以通过个人中心随时访问、更正或删除您的个人信息。若您决定不再使用本平台，可在设置中申请注销账号。</view>
				</view>

				<view class="section">
					<view class="h1">五、 Cookie 及同类技术</view>
					<view class="p">我们使用 Cookie 来提升登录体验并记住您的偏好设置（如深色模式、校区选择）。</view>
				</view>
			</view>

			<view class="footer-info">
				<text>如有疑问，请联系校内管理员：support@campus.edu</text>
				<view class="version">版本号：V1.0.4 | 2026.04</view>
			</view>
		</scroll-view>
	</view>
</template>

<script setup>
import { ref } from 'vue';

const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;

const goBack = () => {
	uni.navigateBack();
};
</script>

<style lang="scss" scoped>
$theme: #D4FB1B;

.privacy-container {
	min-height: 100vh;
	background-color: #fcfcfc;
	display: flex;
	flex-direction: column;
}

/* 导航 */
.custom-nav {
	background: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx 40rpx;
	.back-icon { font-size: 40rpx; color: #333; font-weight: bold; }
	.nav-title { font-size: 30rpx; font-weight: bold; }
	.nav-right { width: 40rpx; }
}

.content-scroll {
	flex: 1;
}

/* 头部卡片 */
.header-card {
	background: #1A1A1A;
	margin: 30rpx;
	padding: 50rpx 40rpx;
	border-radius: 40rpx;
	color: #fff;
	.main-title { font-size: 40rpx; font-weight: bold; display: block; margin-bottom: 16rpx; }
	.sub-tip { font-size: 24rpx; color: rgba(255,255,255,0.6); line-height: 1.5; }
	.tag-box {
		display: flex;
		gap: 16rpx;
		margin-top: 30rpx;
		.tag {
			background: rgba($theme, 0.2);
			color: $theme;
			padding: 6rpx 20rpx;
			border-radius: 10rpx;
			font-size: 22rpx;
			border: 1rpx solid rgba($theme, 0.3);
		}
	}
}

/* 文章主体 */
.article-body {
	padding: 10rpx 40rpx;
	.section {
		margin-bottom: 60rpx;
		.h1 {
			font-size: 32rpx;
			font-weight: bold;
			color: #1a1a1a;
			margin-bottom: 24rpx;
			display: flex;
			align-items: center;
			&::before {
				content: '';
				width: 10rpx;
				height: 10rpx;
				background: $theme;
				margin-right: 16rpx;
				border-radius: 50%;
			}
		}
		.p {
			font-size: 28rpx;
			color: #4a4a4a;
			line-height: 1.8;
			margin-bottom: 16rpx;
			text-align: justify;
		}
		.li {
			font-size: 26rpx;
			color: #666;
			line-height: 1.8;
			padding-left: 20rpx;
		}
	}
}

.footer-info {
	padding: 60rpx 40rpx 100rpx;
	text-align: center;
	font-size: 24rpx;
	color: #999;
	.version {
		margin-top: 10rpx;
		font-size: 20rpx;
		color: #ccc;
	}
}
</style>