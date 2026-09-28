<template>
	<view class="terms-container">
		<view class="custom-nav" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-left" @tap="goBack">
				<text class="back-icon">✕</text>
			</view>
			<view class="nav-title">用户协议与隐私政策</view>
			<view class="nav-right"></view>
		</view>

		<scroll-view scroll-y class="content-scroll">
			<view class="article">
				<view class="update-time">最近更新日期：2026年4月6日</view>
				
				<view class="section">
					<text class="h1">1. 特别提示</text>
					<text class="p">欢迎使用校园二手交易平台（以下简称“本平台”）。在注册、登录或使用本服务前，请您务必审慎阅读并充分理解本协议各条款。当您点击“同意”或实际使用服务时，即表示您已自愿接受本协议的所有内容。</text>
				</view>

				<view class="section">
					<text class="h1">2. 用户身份与认证</text>
					<text class="p">2.1 本平台主要面向在校大学生提供服务。用户须通过实名认证或学校邮箱/学生证件认证。</text>
					<text class="p">2.2 您应妥善保管您的账号及密码，任何通过您的账号进行的操作均视为您本人行为。</text>
				</view>

				<view class="section">
					<text class="h1">3. 交易规则</text>
					<text class="p">3.1 **信息发布**：用户不得发布违法、虚假、淫秽或侵犯他人知识产权的物品信息。</text>
					<text class="p">3.2 **校内交易**：为保障安全，建议用户选择校园内公共监控区域进行当面交易，并在确认物品无误后再进行线上或线下支付。</text>
					<text class="p">3.3 **禁止违禁品**：严禁交易烟草、酒精、处方药、易燃易爆品及任何法律法规禁止流通的物品。</text>
				</view>

				<view class="section">
					<text class="h1">4. 用户行为规范</text>
					<text class="p">您承诺在使用本平台过程中，遵守诚实信用原则。严禁从事刷单、虚假交易、恶意评价或骚扰其他用户的行为。</text>
				</view>

				<view class="section">
					<text class="h1">5. 隐私保护</text>
					<text class="p">本平台高度重视您的个人隐私，我们将严格按照相关法律规定保护您的个人信息。未经您的许可，我们不会向第三方提供您的手机号、学生信息等敏感数据。</text>
				</view>

				<view class="section">
					<text class="h1">6. 免责声明</text>
					<text class="p">本平台仅作为二手物品信息的发布与交流平台，不直接参与买卖双方的线下交易过程。用户因线下交易产生的纠纷或经济损失，本平台将提供必要的证据协助，但不承担连带赔偿责任。</text>
				</view>
			</view>
			
			<view class="bottom-placeholder"></view>
		</scroll-view>

		<view class="footer-action">
			<view class="btn-agree" @tap="handleConfirm">我已阅读并同意</view>
		</view>
	</view>
</template>

<script setup>
import { ref } from 'vue';

const statusBarHeight = uni.getSystemInfoSync().statusBarHeight;

const goBack = () => {
	uni.navigateBack();
};

const handleConfirm = () => {
	uni.showToast({
		title: '协议已确认',
		icon: 'success'
	});
	setTimeout(() => {
		uni.navigateBack();
	}, 1000);
};
</script>

<style lang="scss" scoped>
$theme-color: #D4FB1B;

.terms-container {
	min-height: 100vh;
	background-color: #FFFFFF;
	display: flex;
	flex-direction: column;
}

/* 导航栏 */
.custom-nav {
	background: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx 30rpx;
	border-bottom: 1rpx solid #f5f5f5;
	
	.back-icon { font-size: 36rpx; color: #333; padding: 10rpx; }
	.nav-title { font-size: 30rpx; font-weight: bold; }
	.nav-right { width: 60rpx; }
}

.content-scroll {
	flex: 1;
	padding: 40rpx;
	box-sizing: border-box;
}

.article {
	.update-time {
		font-size: 24rpx;
		color: #999;
		margin-bottom: 40rpx;
	}
	
	.section {
		margin-bottom: 50rpx;
		
		.h1 {
			display: block;
			font-size: 32rpx;
			font-weight: bold;
			color: #1a1a1a;
			margin-bottom: 20rpx;
			position: relative;
			padding-left: 20rpx;
			
			&::before {
				content: '';
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				width: 8rpx;
				height: 30rpx;
				 
				border-radius: 4rpx;
			}
		}
		
		.p {
			display: block;
			font-size: 28rpx;
			color: #666;
			line-height: 1.8;
			text-align: justify;
		}
	}
}

.bottom-placeholder {
	height: 160rpx;
}

/* 底部按钮 */
.footer-action {
	position: fixed;
	bottom: 0;
	left: 0;
	width: 100%;
	padding: 30rpx 40rpx 60rpx;
	background: linear-gradient(to top, #fff 80%, rgba(255,255,255,0));
	box-sizing: border-box;
	
	.btn-agree {
		background-color: $theme-color;
		height: 90rpx;
		line-height: 90rpx;
		text-align: center;
		border-radius: 45rpx;
		font-size: 30rpx;
		font-weight: bold;
		color: #000;
		box-shadow: 0 10rpx 20rpx rgba(212, 251, 27, 0.3);
		
		&:active {
			opacity: 0.8;
			transform: scale(0.98);
		}
	}
}
</style>