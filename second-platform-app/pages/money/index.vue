<template>
  <view class="wallet-container">
    <view class="custom-nav">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="nav-title">我的余额</text>
      <view class="placeholder"></view> </view>

    <view class="balance-card">
      <view class="card-header">
        <text class="title">我的余额币</text>
        <text class="detail-btn">明细 ></text>
      </view>
      <view class="balance-amount">
        <text class="symbol">￥</text>
        <text class="amount">{{ balance }}</text>
      </view>
      <view class="card-footer">
        <text>今日已产生收益 0.00</text>
      </view>
    </view>

    <view class="section-box recharge-section">
      <view class="section-title">充值金额</view>
      <view class="amount-grid">
        <view 
          class="amount-item" 
          :class="{ 'active': selectedAmount === item.value && !isCustomAmount }"
          v-for="(item, index) in amountList" 
          :key="index"
          @click="selectAmount(item.value)"
        >
          <text class="val">{{ item.label }}</text>
          <text class="desc" v-if="item.desc">{{ item.desc }}</text>
        </view>
        
        <view 
          class="amount-item custom-item" 
          :class="{ 'active': isCustomAmount }"
          @click="activateCustom"
        >
          <text class="val" v-if="!isCustomAmount">自定义</text>
          <input 
            v-else
            class="custom-input" 
            type="number" 
            v-model="customValue" 
            @input="handleCustomInput"
            placeholder="输入金额" 
            :focus="isCustomAmount"
          />
        </view>
      </view>
    </view>

    <view class="section-box payment-section">
      <view class="section-title">支付方式</view>
      <radio-group @change="handlePaymentChange">
        <label class="payment-item" v-for="(pay, index) in paymentMethods" :key="index">
          <view class="pay-info">
            <view class="pay-icon" :class="pay.iconClass">{{ pay.iconText }}</view>
            <text class="pay-name">{{ pay.name }}</text>
          </view>
          <radio :value="pay.value" :checked="selectedPayment === pay.value" color="#D4FB1B" />
        </label>
      </radio-group>
    </view>

    <view class="section-box task-section">
      <view class="task-info">
        <view class="task-title">每日签到领余额币</view>
        <view class="task-desc">已连续签到 <text class="highlight">{{ checkInDays }}</text> 天，今日可领 5 币</view>
      </view>
      <button class="checkin-btn" :class="{ 'disabled': hasCheckedIn }" @click="handleCheckIn">
        {{ hasCheckedIn ? '已签到' : '立即签到' }}
      </button>
    </view>

    <view class="footer-action">
      <button class="recharge-btn" @click="submitRecharge" :disabled="selectedAmount <= 0">
        立即充值 ￥{{ selectedAmount || '0' }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// --- 状态数据 ---
const balance = ref('2,450.00') 
const checkInDays = ref(3)      
const hasCheckedIn = ref(false) 
const selectedPayment = ref('alipay') 

// 充值相关状态
const selectedAmount = ref(50)  // 最终要充值的金额
const isCustomAmount = ref(false) // 是否处于“自定义金额”模式
const customValue = ref('')       // 自定义输入框绑定的值

const amountList = ref([
  { label: '10元', value: 10, desc: '' },
  { label: '50元', value: 50, desc: '送5币' },
  { label: '100元', value: 100, desc: '送12币' }
  // 删掉了一个500元，给自定义按钮腾出位置，保持2x2的完美网格
])

const paymentMethods = ref([
  { name: '支付宝支付', value: 'alipay', iconClass: 'bg-blue', iconText: '支' },
  { name: '微信支付', value: 'wechat', iconClass: 'bg-green', iconText: '微' }
])

// --- 交互逻辑 ---

// 新增：返回上一页逻辑
const goBack = () => {
  uni.navigateBack({
    delta: 1,
    fail: () => {
      // 如果没有上一页（比如直接分享进来的），就回退到首页
      uni.switchTab({ url: '/pages/index/index' })
    }
  })
}

// 选择固定套餐
const selectAmount = (val) => {
  isCustomAmount.value = false // 关闭自定义模式
  selectedAmount.value = val   // 更新选中金额
  customValue.value = ''       // 清空输入框的内容
}

// 激活自定义模式
const activateCustom = () => {
  isCustomAmount.value = true
  selectedAmount.value = customValue.value ? Number(customValue.value) : 0
}

// 监听自定义输入框的输入
const handleCustomInput = (e) => {
  const val = Number(e.detail.value)
  selectedAmount.value = val > 0 ? val : 0
}

const handlePaymentChange = (e) => {
  selectedPayment.value = e.detail.value
}

const handleCheckIn = () => {
  if (hasCheckedIn.value) return
  uni.showLoading({ title: '签到中...' })
  setTimeout(() => {
    uni.hideLoading()
    hasCheckedIn.value = true
    checkInDays.value += 1
    uni.showToast({ title: '签到成功，+5币', icon: 'success' })
  }, 600)
}

const submitRecharge = () => {
  if (selectedAmount.value <= 0) {
    return uni.showToast({ title: '请输入正确的金额', icon: 'none' })
  }
  uni.showModal({
    title: '确认充值',
    content: `将使用${selectedPayment.value === 'alipay' ? '支付宝' : '微信'}支付 ￥${selectedAmount.value}`,
    success: (res) => {
      if (res.confirm) {
        uni.showLoading({ title: '拉起支付中...' })
        setTimeout(() => {
          uni.hideLoading()
          uni.showToast({ title: '模拟支付成功', icon: 'success' })
        }, 1000)
      }
    }
  })
}
</script>

<style scoped lang="scss">
$theme-color: #D4FB1B;
$text-dark: #333333;
$bg-color: #F5F5F9;

.wallet-container {
  min-height: 100vh;
  background-color: $bg-color;
  padding: 0 24rpx 160rpx; // 顶部padding去掉，由nav撑开
}

/* 新增：自定义导航栏样式 */
.custom-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: calc(var(--status-bar-height) + 20rpx); // 适配手机顶部状态栏
  padding-bottom: 20rpx;
  margin-bottom: 20rpx;
  
  .back-btn {
    width: 60rpx;
    height: 60rpx;
    display: flex;
    align-items: center;
    .back-icon {
      font-size: 60rpx;
      color: $text-dark;
      line-height: 1;
      margin-top: -10rpx; // 微调箭头居中
    }
  }

  .nav-title {
    font-size: 34rpx;
    font-weight: bold;
    color: $text-dark;
  }

  .placeholder {
    width: 60rpx; // 和返回按钮等宽，利用flex布局把标题挤到正中间
  }
}

.balance-card {
  background: linear-gradient(135deg, $theme-color 0%, #e2fc4c 100%);
  border-radius: 24rpx;
  padding: 40rpx;
  color: $text-dark;
  box-shadow: 0 8rpx 24rpx rgba(212, 251, 27, 0.3);
  margin-bottom: 30rpx;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 28rpx;
    opacity: 0.8;
  }

  .balance-amount {
    margin: 30rpx 0;
    font-weight: bold;
    .symbol { font-size: 40rpx; }
    .amount { font-size: 80rpx; }
  }

  .card-footer {
    font-size: 24rpx;
    opacity: 0.8;
  }
}

.section-box {
  background-color: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
  }
}

.amount-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  .amount-item {
    width: 48%;
    height: 120rpx;
    background-color: #F8F8F8;
    border-radius: 16rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-bottom: 20rpx;
    border: 2rpx solid transparent;
    transition: all 0.2s;
    box-sizing: border-box;

    .val {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }

    .desc {
      font-size: 22rpx;
      color: #999;
      margin-top: 8rpx;
    }

    &.active {
      background-color: rgba(212, 251, 27, 0.1);
      border-color: $theme-color;
      .val { color: #000; }
      .desc { color: #b5d617; }
    }
  }

  /* 自定义输入框特殊样式 */
  .custom-item {
    .custom-input {
      text-align: center;
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      width: 80%;
      height: 100%;
    }
  }
}

.payment-section {
  .payment-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #F0F0F0;

    &:last-child { border-bottom: none; }

    .pay-info {
      display: flex;
      align-items: center;

      .pay-icon {
        width: 48rpx;
        height: 48rpx;
        border-radius: 50%;
        color: #fff;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 24rpx;
        margin-right: 20rpx;

        &.bg-blue { background-color: #1677FF; }
        &.bg-green { background-color: #09B83E; }
      }

      .pay-name {
        font-size: 28rpx;
        color: #333;
      }
    }
  }
}

.task-section {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .task-info {
    .task-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 8rpx;
    }
    .task-desc {
      font-size: 24rpx;
      color: #999;
      .highlight {
        color: #FF5000;
        font-weight: bold;
        margin: 0 4rpx;
      }
    }
  }

  .checkin-btn {
    margin: 0;
    padding: 0 30rpx;
    height: 60rpx;
    line-height: 60rpx;
    background-color: $theme-color;
    color: #333;
    font-size: 26rpx;
    border-radius: 30rpx;
    font-weight: bold;

    &::after { border: none; }
    &.disabled {
      background-color: #E5E5E5;
      color: #999;
    }
  }
}

.footer-action {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 20rpx 30rpx 60rpx; 
  background-color: #FFFFFF;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
  z-index: 99;

  .recharge-btn {
    background-color: $theme-color;
    color: #333;
    height: 88rpx;
    line-height: 88rpx;
    border-radius: 44rpx;
    font-size: 32rpx;
    font-weight: bold;

    &::after { border: none; }
    
    &:active { opacity: 0.8; }
    
    &[disabled] {
      background-color: #f0f0f0 !important;
      color: #ccc !important;
    }
  }
}
</style>