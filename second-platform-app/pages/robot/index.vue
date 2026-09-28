<template>
	<view class="chat-container">
		<view class="back-btn" @tap="goHome">
			<text class="back-icon">‹</text>
		</view>

		<view class="ai-header">
			<view class="greeting-section">
				<text class="greeting-small">Hi，你好！</text>
				<text class="greeting-large">需要我为你做些什么？</text>
			</view>

			<view class="nav-cards-container">
				<view class="nav-card" @tap="handleMemoryMigration">
					<text class="nav-icon">✨</text>
					<text class="nav-text">聊荐好物</text>
				</view>
				<view class="nav-card" @tap="toggleRolePanel">
					<text class="nav-icon">🤖</text>
					<text class="nav-text">虚拟角色</text>
				</view>
				<view class="nav-card" @tap="tuijian">
					<text class="nav-icon">🛍</text>
					<text class="nav-text">商品推荐</text>
				</view>
			</view>
		</view>

		<scroll-view scroll-y class="chat-body" :scroll-top="scrollTop" scroll-with-animation>
			<view class="chat-list" id="chatList">
				<view v-for="(msg, index) in messageList" :key="index" :class="['msg-row', msg.role]">
					<image v-if="msg.role === 'ai'" class="avatar" src="/static/indexro.png" mode="aspectFill"></image>
					<view class="bubble-container">
						<view :class="msg.role === 'ai' ? 'plain-text' : 'bubble'" v-if="msg.content">
							<text>{{ msg.content }}</text>
							<text class="cursor" v-if="msg.typing">|</text>
							<view class="order-detail-btn" v-if="msg.hasOrderAction && !msg.typing" @tap="goToCart">
								查看详情
							</view>
						</view>

						<scroll-view scroll-x class="ai-goods-scroll" :show-scrollbar="false" :enhanced="true"
							v-if="msg.role === 'ai' && msg.goods && msg.goods.length > 0">
							<view class="goods-flex-container">
								<view class="custom-goods-card" v-for="(item, gidx) in msg.goods" :key="gidx"
									@tap="goToDetail(item.id)">
									<view class="img-wrapper">
										<image class="g-img" :src="item.img" mode="aspectFill"></image>
										<view class="title-mask">
											<text class="mask-title">{{ item.title }}</text>
										</view>
									</view>
									<view class="price-row">
										
									</view>
									<view class="detail-btn">查看详情</view>
								</view>
							</view>
						</scroll-view>
					</view>
				</view>

				<view class="msg-row ai" v-if="isWaiting">
					<view class="gemini-icon">
						<view class="star-sparkle"></view>
					</view>
					<view class="loading-text-wrapper">
						<text class="shimmer-text">正在调用系统内部服务...</text>
					</view>
				</view>

				<view style="height: 100rpx;"></view>
			</view>
		</scroll-view>

		<view class="role-popover" :class="{ 'show': showRolePanel }">
			<view class="role-grid">
				<view class="role-card" v-for="(role, index) in roles" :key="index" @tap="selectRole(role)">
					<text class="role-avatar">{{ role.icon }}</text>
					<text class="role-name">{{ role.name }}</text>
				</view>
			</view>
			<view class="popover-arrow"></view>
		</view>

		<view class="fixed-bottom-input">
			<view class="input-container">
				<view class="plus-btn" @tap="toggleActionSheet">
					<text class="plus-icon">+</text>
				</view>

				<view class="input-box-wrapper">
					<view class="role-chip" v-if="selectedRole">
						<text class="chip-text">{{ selectedRole.name }}</text>
						<text class="chip-close" @tap.stop="clearRole">×</text>
					</view>

					<input class="input-box" type="text" v-model="inputText" :focus="inputFocus" placeholder="输入您的问题..."
						placeholder-style="color:#ccc" @confirm="sendUserMsg" :disabled="isTyping || isWaiting" />

					<view class="send-icon-btn" v-if="inputText.trim().length > 0" @tap="sendUserMsg">
						<text class="send-arrow">↑</text>
					</view>
				</view>
			</view>
		</view>

		<view class="action-mask" v-if="showActionSheet" @tap="toggleActionSheet"></view>
		<view class="action-sheet" :class="{ 'show': showActionSheet }">
			<view class="action-item" @tap="handleCamera">
				<text class="action-icon">📷</text>
				<text>相机</text>
			</view>
			<view class="action-item" @tap="handleGallery">
				<text class="action-icon">🖼️</text>
				<text>图库</text>
			</view>
			<view class="action-item" @tap="handleFile">
				<text class="action-icon">📁</text>
				<text>文件</text>
			</view>
			<view class="action-cancel" @tap="toggleActionSheet">取消</view>
		</view>

		<view class="role-mask" v-if="showRolePanel" @tap="showRolePanel = false"></view>

		<view class="modal-overlay" v-if="showOrderModal" @tap.stop>
			<view class="qw-modal">
				<view class="qw-modal-header">
					<text class="qw-modal-title">订单确认</text>
				</view>
				<view class="qw-modal-body">
					<view class="qw-success-icon">
						<text class="qw-check">✓</text>
					</view>
					<text class="qw-subtitle">下单成功</text>
					<text class="qw-desc">订单号：{{ currentOrderNo }}</text>
				</view>
				<view class="qw-modal-footer">
					<view class="qw-btn qw-btn-default" @tap="closeOrderModal">继续选购</view>
					<view class="qw-btn qw-btn-primary" @tap="handleViewDetail">查看详情</view>
				</view>
			</view>
		</view>

		<view class="modal-overlay" v-if="showUploadModal" @tap.stop>
			<view class="upload-modal">
				<text class="upload-title">记忆迁移</text>

				<view class="upload-progress-container" v-if="isUploading">
					<view class="upload-spinner"></view>
					<text class="upload-text">正在上传及解析 Excel 数据...</text>
				</view>

				<view class="upload-success" v-else-if="uploadSuccess">
					<text class="check-icon">✓</text>
					<text class="upload-text">数据迁移完成！</text>
					<text class="recommend-text" @tap="startNewChat">是否开启新的对话根据记忆进行推荐商品</text>
				</view>

				<view class="upload-actions" v-else>
					<text class="upload-desc">请上传包含记忆数据的 Excel 文件进行解析导入</text>
					<view class="upload-btn" @tap="chooseExcelFile">选择 Excel 文件</view>
					<view class="cancel-btn" @tap="showUploadModal = false">取消</view>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		chatDeepSeek,
		chatDeepSeekcpy,
	} from "@/apis/robot/robot.js"

	export default {
		data() {
			return {
				inputText: '',
				messageList: [],
				scrollTop: 0,
				isTyping: false,
				isWaiting: false,
				showActionSheet: false,
				showOrderModal: false,
				currentOrderNo: '',
				showRolePanel: false,
				selectedRole: null,
				inputFocus: false,
				roles: [{
						name: '销售专家',
						icon: '💼',
						id: 1
					},
					{
						name: '二手达人',
						icon: '♻️',
						id: 2
					},
					{
						name: '穿搭助手',
						icon: '👗',
						id: 3
					},
					{
						name: '数码极客',
						icon: '💻',
						id: 4
					}
				],
				showUploadModal: false,
				isUploading: false,
				uploadSuccess: false
			};
		},
		methods: {
			goHome() {
				uni.reLaunch({
					url: '/pages/index/index'
				});
			},
			toggleActionSheet() {
				this.showActionSheet = !this.showActionSheet;
			},
			handleCamera() {
				this.toggleActionSheet();
				uni.chooseImage({
					count: 1,
					sourceType: ['camera'],
					success: (res) => {}
				});
			},
			handleGallery() {
				this.toggleActionSheet();
				uni.chooseImage({
					count: 1,
					sourceType: ['album'],
					success: (res) => {}
				});
			},
			handleFile() {
				this.toggleActionSheet();
				// #ifdef MP-WEIXIN
				uni.chooseMessageFile({
					count: 1,
					type: 'file',
					extension: ['.xls', '.xlsx'],
					success: (res) => {
						this.startUploadTransition();
					}
				});
				// #endif
				// #ifndef MP-WEIXIN
				uni.chooseFile({
					count: 1,
					extension: ['.xls', '.xlsx'],
					success: (res) => {
						this.startUploadTransition();
					}
				});
				// #endif
			},
			toggleRolePanel() {
				this.showRolePanel = !this.showRolePanel;
			},
			selectRole(role) {
				this.selectedRole = role;
				this.showRolePanel = false;
				this.$nextTick(() => {
					this.inputFocus = true;
				});
			},
			clearRole() {
				this.selectedRole = null;
			},
			handleMemoryMigration() {
				this.showUploadModal = true;
				this.isUploading = false;
				this.uploadSuccess = false;
			},
			chooseExcelFile() {
				// #ifdef MP-WEIXIN
				uni.chooseMessageFile({
					count: 1,
					type: 'file',
					extension: ['.xls', '.xlsx'],
					success: (res) => {
						this.startUploadTransition();
					}
				});
				// #endif
				// #ifndef MP-WEIXIN
				uni.chooseFile({
					count: 1,
					extension: ['.xls', '.xlsx'],
					success: (res) => {
						this.startUploadTransition();
					}
				});
				// #endif
			},
			startUploadTransition() {
				this.isUploading = true;
				setTimeout(() => {
					this.isUploading = false;
					this.uploadSuccess = true;
				}, 2000);
			},
			startNewChat() {
				this.showUploadModal = false;
				uni.reLaunch({
					url: '/pages/jiyi/index'
				});
			},
			tuijian(){
				uni.reLaunch({
					url: '/pages/tuijian/index'
				});
			},
			sendUserMsg() {
				const text = this.inputText.trim();
				if (!text || this.isTyping || this.isWaiting) return;
				this.inputText = '';
				this.executeChat(text);
			},
			// --- 核心修改部分 ---
			async executeChat(text) {
				this.messageList.push({
					role: 'user',
					content: text
				});
				this.scrollToBottom();
				this.isWaiting = true;

				try {
					let res;
					// 判断是否选择了虚拟角色
					if (this.selectedRole) {
						// 选择了角色，调用带有cpy后缀的接口
						res = await chatDeepSeekcpy({
							message: text,
							roleName: this.selectedRole.name // 可选：传递角色名给后端
						});
					} else {
						// 未选择角色，调用默认接口
						res = await chatDeepSeek({
							message: text
						});
					}

					this.isWaiting = false;
					const rawData = (typeof res === 'object' && res.data) ? res.data : res;
					this.parseAiResponse(String(rawData));
				} catch (err) {
					this.isWaiting = false;
					this.simulateTypewriter("抱歉，内部系统响应较慢，请稍后再试。", []);
				}
			},
			// --- 修改结束 ---
			parseAiResponse(rawText) {
				const goods = [];
				let textContent = rawText;
				let hasOrderAction = false;
				if (textContent.includes('[ACTION]VIEW_CART[/ACTION]')) {
					hasOrderAction = true;
					textContent = textContent.replace('[ACTION]VIEW_CART[/ACTION]', '').trim();
					this.triggerOrderSuccess();
				}
				const reg = /([^\n]+)\n\[ID\](.*?)\[\/ID\]\n\[IMG\](.*?)\[\/IMG\]/g;
				let match;
				while ((match = reg.exec(textContent)) !== null) {
					goods.push({
						title: match[1].replace('--- 相关商品推荐 ---', '').trim(),
						id: match[2].trim(),
						img: match[3].trim(),
						price: (Math.random() * 100 + 50).toFixed(2)
					});
					textContent = textContent.replace(match[0], '');
				}
				textContent = textContent.replace('--- 相关商品推荐 ---', '').trim();
				this.simulateTypewriter(textContent || (hasOrderAction ? "下单成功" : "为您找到以下相关商品："), goods, hasOrderAction);
			},
			triggerOrderSuccess() {
				this.currentOrderNo = '20260409' + Math.random().toString().slice(2, 10);
				this.showOrderModal = true;
			},
			closeOrderModal() {
				this.showOrderModal = false;
			},
			handleViewDetail() {
				this.showOrderModal = false;
				this.goToCart();
			},
			simulateTypewriter(fullText, goods, hasOrderAction = false) {
				const newMsgIndex = this.messageList.length;
				this.messageList.push({
					role: 'ai',
					content: '',
					goods: [],
					hasOrderAction: false,
					typing: true
				});
				this.isTyping = true;
				let currentIdx = 0;
				const timer = setInterval(() => {
					if (currentIdx < fullText.length) {
						this.messageList[newMsgIndex].content += fullText[currentIdx];
						currentIdx++;
						if (currentIdx % 5 === 0) this.scrollToBottom();
					} else {
						this.messageList[newMsgIndex].typing = false;
						this.messageList[newMsgIndex].goods = goods;
						this.messageList[newMsgIndex].hasOrderAction = hasOrderAction;
						this.isTyping = false;
						clearInterval(timer);
						setTimeout(() => {
							this.scrollToBottom();
						}, 100);
					}
				}, 20);
			},
			scrollToBottom() {
				this.$nextTick(() => {
					const query = uni.createSelectorQuery().in(this);
					query.select('#chatList').boundingClientRect(data => {
						if (data) this.scrollTop = data.height + 800;
					}).exec();
				});
			},
			goToDetail(id) {
				uni.navigateTo({
					url: `/pages/gooddetail/index?id=${id}`
				});
			},
			goToCart() {
				uni.navigateTo({
					url: '/pages/mycart/index'
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	/* 样式部分完全保留，未做任何修改以确保原始视觉效果一致 */
	.role-popover {
		position: fixed;
		bottom: 160rpx;
		left: 40rpx;
		width: 440rpx;
		background: rgba(255, 255, 255, 0.98);
		backdrop-filter: blur(20px);
		border-radius: 40rpx;
		box-shadow: 0 12rpx 48rpx rgba(0, 0, 0, 0.12);
		z-index: 2001;
		padding: 24rpx;
		transform: translateY(30rpx) scale(0.9);
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
		border: 1rpx solid rgba(0, 0, 0, 0.05);

		&.show {
			transform: translateY(0) scale(1);
			opacity: 1;
			visibility: visible;
		}

		.role-grid {
			display: grid;
			grid-template-columns: 1fr 1fr;
			gap: 20rpx;
		}

		.role-card {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 24rpx 10rpx;
			background: #f7f8f9;
			border-radius: 28rpx;

			.role-avatar {
				font-size: 44rpx;
				margin-bottom: 8rpx;
			}

			.role-name {
				font-size: 24rpx;
				color: #333;
				font-weight: 500;
			}

			&:active {
				background: #eeeeee;
			}
		}

		.popover-arrow {
			position: absolute;
			bottom: -12rpx;
			left: 60rpx;
			width: 24rpx;
			height: 24rpx;
			background: white;
			transform: rotate(45deg);
			border-right: 1rpx solid rgba(0, 0, 0, 0.05);
			border-bottom: 1rpx solid rgba(0, 0, 0, 0.05);
		}
	}

	.role-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		z-index: 2000;
		background: transparent;
	}

	.role-chip {
		display: flex;
		align-items: center;
		background: #333;
		border-radius: 30rpx;
		padding: 8rpx 18rpx;
		margin-right: 12rpx;
		flex-shrink: 0;

		.chip-text {
			color: #D4FB1B;
			font-size: 24rpx;
			font-weight: bold;
		}

		.chip-close {
			color: #fff;
			font-size: 32rpx;
			margin-left: 10rpx;
			line-height: 1;
		}
	}

	.action-mask {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.4);
		z-index: 1000;
	}

	.action-sheet {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: #fff;
		border-radius: 40rpx 40rpx 0 0;
		z-index: 1001;
		transform: translateY(100%);
		transition: transform 0.3s ease;
		padding-bottom: env(safe-area-inset-bottom);

		&.show {
			transform: translateY(0);
		}

		.action-item {
			display: flex;
			align-items: center;
			padding: 35rpx 0;
			margin-left: 50px;
			font-size: 32rpx;
			color: #333;
			border-bottom: 1rpx solid #f9f9f9;

			.action-icon {
				margin-right: 15rpx;
				font-size: 36rpx;
			}
		}

		.action-cancel {
			padding: 35rpx 0;
			text-align: center;
			font-size: 32rpx;
			color: #999;
			border-top: 12rpx solid #f5f5f5;
		}
	}

	.chat-container {
		width: 100%;
		height: 100vh;
		display: flex;
		flex-direction: column;
		background-color: #f8f8f8;
		overflow: hidden;
		position: relative;
	}

	.back-btn {
		position: absolute;
		left: 30rpx;
		top: 80rpx;
		width: 70rpx;
		height: 70rpx;
		background-color: rgba(255, 255, 255, 0.4);
		backdrop-filter: blur(4rpx);
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 100;

		.back-icon {
			font-size: 56rpx;
			color: #333;
			margin-top: -6rpx;
			margin-left: -4rpx;
		}
	}

	.ai-header {
		width: 100%;
		padding: 180rpx 0 40rpx;
		background: linear-gradient(180deg, #D4FB1B 0%, rgba(212, 251, 27, 0) 100%);
		display: flex;
		flex-direction: column;
		flex-shrink: 0;
	}

	.greeting-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 20rpx;

		.greeting-small {
			margin-right: 160px;
			font-size: 28rpx;
			color: #555;
		}

		.greeting-large {
			font-size: 44rpx;
			font-weight: bold;
			color: #333;
		}
	}

	.nav-cards-container {
		display: flex;
		flex-direction: column;
		padding: 0 40rpx;
		margin-top: 30rpx;
		width: 200px;

		.nav-card {
			display: flex;
			align-items: center;
			background: rgba(255, 255, 255, 0.9);
			border-radius: 50rpx;
			padding: 16rpx 36rpx;
			margin-bottom: 20rpx;
			box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.03);

			.nav-icon {
				font-size: 32rpx;
				margin-right: 16rpx;
			}

			.nav-text {
				font-size: 28rpx;
				color: #333;
				font-weight: 500;
			}
		}
	}

	.chat-body {
		flex: 1;
		overflow-y: scroll;

		.chat-list {
			padding: 20rpx 30rpx;
		}
	}

	.msg-row {
		display: flex;
		margin-bottom: 40rpx;

		.avatar {
			width: 70rpx;
			height: 70rpx;
			border-radius: 50%;
			flex-shrink: 0;
		}

		.bubble-container {
			max-width: 85%;
		}

		.bubble {
			padding: 22rpx 30rpx;
			font-size: 28rpx;
			border-radius: 30rpx;
		}

		&.ai {
			.plain-text {
				padding: 10rpx 20rpx;
				font-size: 28rpx;
				color: #333;
			}
		}

		&.user {
			flex-direction: row-reverse;

			.bubble {
				background: #D4FB1B;
				margin-right: 20rpx;
				border-top-right-radius: 4rpx;
			}
		}
	}

	.fixed-bottom-input {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 25px;
		z-index: 900;
		padding: 20rpx 30rpx 60rpx;
		background: #fff;
		border-top: 1rpx solid #eee;

		.input-container {
			display: flex;
			align-items: center;
		}

		.plus-btn {
			width: 70rpx;
			height: 70rpx;
			border-radius: 50%;
			background: #f5f5f5;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-right: 20rpx;

			.plus-icon {
				font-size: 48rpx;
				color: #555;
				margin-top: -6rpx;
			}
		}

		.input-box-wrapper {
			flex: 1;
			display: flex;
			align-items: center;
			background: #f5f5f5;
			border-radius: 45rpx;
			padding: 0 10rpx 0 25rpx;
			height: 90rpx;
		}

		.input-box {
			flex: 1;
			font-size: 28rpx;
		}

		.send-icon-btn {
			width: 60rpx;
			height: 60rpx;
			background: #333;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;

			.send-arrow {
				color: #D4FB1B;
				font-size: 34rpx;
			}
		}
	}

	.ai-goods-scroll {
		width: 100%;
		margin-top: 16rpx;
	}

	.goods-flex-container {
		display: flex;
		gap: 20rpx;
		padding-bottom: 10rpx;
	}

	.custom-goods-card {
		width: 400rpx;
		flex-shrink: 0;
		background: #fff;
		border-radius: 12rpx;
		overflow: hidden;
		border: 1rpx solid #eee;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.img-wrapper {
		width: 400rpx;
		height: 300rpx;
		position: relative;
	}

	.g-img {
		width: 100%;
		height: 100%;
		background: #f5f5f5;
	}

	.title-mask {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		background: rgba(0, 0, 0, 0.6);
		padding: 8rpx 10rpx;
	}

	.mask-title {
		color: #fff;
		font-size: 22rpx;
		line-height: 1.2;
		display: block;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.price-row {
		padding: 12rpx 0;
		text-align: center;
	}

	.detail-btn {
		width: 100%;
		text-align: center;
		padding: 14rpx 0;
		background: #f8f8f8;
		font-size: 24rpx;
		color: #333;
		border-top: 1rpx solid #eee;
	}

	.order-detail-btn {
		display: inline-block;
		margin-top: 16rpx;
		padding: 8rpx 32rpx;
		color: black;
		font-size: 24rpx;
		border-radius: 30rpx;
		font-weight: bold;
	}

	.gemini-icon {
		width: 70rpx;
		height: 70rpx;
		background: #fff;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-right: 20rpx;

		.star-sparkle {
			width: 30rpx;
			height: 30rpx;
			background: #D4FB1B;
			clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
			animation: gemini-spin 2s infinite linear;
		}
	}

	.loading-text-wrapper {
		display: flex;
		align-items: center;

		.shimmer-text {
			font-size: 26rpx;
			font-weight: 500;
			background: linear-gradient(90deg, #999 0%, #333 50%, #999 100%);
			background-size: 200% 100%;
			-webkit-background-clip: text;
			color: transparent;
			animation: shimmer 1.5s infinite linear;
		}
	}

	@keyframes shimmer {
		0% {
			background-position: 200% 0;
		}

		100% {
			background-position: -200% 0;
		}
	}

	@keyframes gemini-spin {
		0% {
			transform: rotate(0deg);
		}

		100% {
			transform: rotate(360deg);
		}
	}

	.cursor {
		animation: blink 0.8s infinite;
		color: #D4FB1B;
	}

	@keyframes blink {

		0%,
		100% {
			opacity: 1;
		}

		50% {
			opacity: 0;
		}
	}

	.modal-overlay {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.4);
		backdrop-filter: blur(4px);
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 3000;
	}

	.qw-modal {
		width: 600rpx;
		background: #fff;
		border-radius: 24rpx;
		overflow: hidden;
		display: flex;
		flex-direction: column;
	}

	.qw-modal-header {
		padding: 30rpx;
		text-align: center;
		border-bottom: 1rpx solid #f0f0f0;

		.qw-modal-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
	}

	.qw-modal-body {
		padding: 50rpx 40rpx;
		display: flex;
		flex-direction: column;
		align-items: center;

		.qw-success-icon {
			width: 90rpx;
			height: 90rpx;
			background: #e8f5e9;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-bottom: 24rpx;

			.qw-check {
				color: #52C41A;
				font-size: 48rpx;
				font-weight: bold;
			}
		}

		.qw-subtitle {
			font-size: 36rpx;
			font-weight: bold;
			color: #333;
			margin-bottom: 12rpx;
		}

		.qw-desc {
			font-size: 28rpx;
			color: #999;
		}
	}

	.qw-modal-footer {
		display: flex;
		border-top: 1rpx solid #f0f0f0;

		.qw-btn {
			flex: 1;
			height: 100rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 30rpx;
		}

		.qw-btn-default {
			color: #666;
			border-right: 1rpx solid #f0f0f0;
		}

		.qw-btn-primary {
			color: #0066FF;
			font-weight: bold;
		}
	}

	.upload-modal {
		width: 560rpx;
		background: #fff;
		border-radius: 24rpx;
		padding: 50rpx 40rpx;
		text-align: center;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.upload-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 40rpx;
	}

	.upload-actions {
		display: flex;
		flex-direction: column;
		width: 100%;

		.upload-desc {
			font-size: 26rpx;
			color: #999;
			margin-bottom: 30rpx;
			line-height: 1.5;
		}

		.upload-btn {
			background: #333;
			color: #D4FB1B;
			padding: 24rpx;
			border-radius: 16rpx;
			margin-bottom: 20rpx;
			font-size: 30rpx;
			font-weight: bold;
		}

		.cancel-btn {
			background: #f5f5f5;
			color: #666;
			padding: 24rpx;
			border-radius: 16rpx;
			font-size: 30rpx;
		}
	}

	.upload-progress-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 20rpx 0;
	}

	.upload-spinner {
		width: 64rpx;
		height: 64rpx;
		border: 6rpx solid #f3f3f3;
		border-top: 6rpx solid #D4FB1B;
		border-right: 6rpx solid #333;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 30rpx;
	}

	@keyframes spin {
		0% {
			transform: rotate(0deg);
		}

		100% {
			transform: rotate(360deg);
		}
	}

	.upload-text {
		font-size: 28rpx;
		color: #666;
	}

	.upload-success {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 20rpx 0;

		.check-icon {
			font-size: 70rpx;
			color: #52C41A;
			margin-bottom: 20rpx;
		}
		
		.recommend-text {
			margin-top: 30rpx;
			font-size: 28rpx;
			color: #000;
			font-weight: bold;
			text-decoration: underline;
			line-height: 1.4;
			padding: 10rpx;
			
			&:active {
				opacity: 0.7;
			}
		}
	}
</style>