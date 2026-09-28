<template>
  <view class="donation-detail-page">
    <!-- 返回按钮 -->
    <view class="back-btn" @click="goBack">
      <text class="back-icon">‹</text>
    </view>

    <!-- 图片轮播 -->
    <swiper class="image-swiper" :indicator-dots="true" :autoplay="false">
      <swiper-item v-for="(image, index) in imageList" :key="index">
        <image :src="image" mode="aspectFill" class="swiper-image"></image>
      </swiper-item>
    </swiper>

    <!-- 物品信息 -->
    <view class="info-section">
      <view class="price-section">
        <text class="price" v-if="donationItem.price > 0">¥{{ donationItem.price }}</text>
        <text class="price free" v-else>免费</text>
        <text class="status" :class="statusClass">{{ statusText }}</text>
      </view>
      
      <text class="title">{{ donationItem.title }}</text>
      <text class="description">{{ donationItem.description }}</text>
      
      <view class="meta-info">
        <view class="meta-item">
          <uni-icons type="location" size="20" color="#999"></uni-icons>
          <text class="meta-text">{{ donationItem.campus }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="time" size="20" color="#999"></uni-icons>
          <text class="meta-text">{{ formatTime(donationItem.createdAt) }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="tags" size="20" color="#999"></uni-icons>
          <text class="meta-text">{{ donationItem.category }}</text>
        </view>
      </view>
    </view>

    <!-- 发布者信息 -->
    <view class="user-section">
      <view class="user-info" @click="goToUser">
        <image :src="donationItem.userAvatar || '/static/logo.png'" mode="aspectFill" class="user-avatar"></image>
        <view class="user-details">
          <text class="user-name">{{ donationItem.userName }}</text>
          <text class="user-label">发布者</text>
        </view>
        <uni-icons type="right" size="24" color="#999"></uni-icons>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <view class="action-btn" @click="shareItem">
        <uni-icons type="share" size="28" color="#666"></uni-icons>
        <text class="action-text">分享</text>
      </view>
      <button class="contact-btn" @click="contactUser">
        联系发布者
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getDonationDetail } from '@/apis/donation/donation.js';

// 响应式数据
const donationItem = ref({
  id: '',
  title: '',
  description: '',
  price: 0,
  category: '',
  images: '',
  userName: '',
  userAvatar: '',
  campus: '',
  status: '',
  createdAt: ''
});

// 计算属性：图片列表
const imageList = computed(() => {
  if (!donationItem.value.images) return ['/static/logo.png'];
  return donationItem.value.images.split(',').filter(img => img);
});

// 计算属性：状态文本
const statusText = computed(() => {
  const statusMap = {
    'available': '可捐赠',
    'traded': '已交易',
    'donated': '已捐赠'
  };
  return statusMap[donationItem.value.status] || '未知';
});

// 计算属性：状态样式
const statusClass = computed(() => {
  return donationItem.value.status;
});

// 方法：格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '';
  const date = new Date(timeStr);
  return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + 
         date.getHours() + ':' + (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
};

// 方法：返回上一页
const goBack = () => {
  uni.navigateBack();
};

// 方法：跳转到用户页面
const goToUser = () => {
  // 这里可以跳转到用户详情页
  uni.showToast({ title: '跳转到用户页面', icon: 'none' });
};

// 方法：联系发布者
const contactUser = () => {
  // 这里可以跳转到聊天页面
  uni.showToast({ title: '联系发布者', icon: 'none' });
};

// 方法：分享物品
const shareItem = () => {
  uni.share({
    title: donationItem.value.title,
    path: `/pages/juanzeng/detail?id=${donationItem.value.id}`,
    success: function () {
      console.log('分享成功');
    },
    fail: function (err) {
      console.log('分享失败', err);
    }
  });
};

// 生命周期：页面加载
onMounted(() => {
  // 获取页面参数
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const id = currentPage.options.id;
  
  if (id) {
    loadDonationDetail(id);
  }
});

// 方法：加载捐赠物品详情
const loadDonationDetail = async (id) => {
  try {
    const res = await getDonationDetail(id);
    if (res.data) {
      donationItem.value = res.data;
    }
  } catch (error) {
    console.error('加载捐赠物品详情失败', error);
    uni.showToast({ title: '加载失败，请重试', icon: 'none' });
  }
};
</script>

<style scoped>
/* 全局页面背景 */
.donation-detail-page {
  min-height: 100vh;
  background-color: #F5F5F5;
  position: relative;
}

/* 返回按钮 */
.back-btn {
  position: fixed;
  top: 40rpx;
  left: 30rpx;
  width: 60rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.back-icon {
  font-size: 40rpx;
  color: #333;
  font-weight: bold;
  margin-left: -4rpx;
  margin-top: -2rpx;
}

/* 图片轮播 */
.image-swiper {
  width: 100%;
  height: 750rpx;
  background-color: #F0F0F0;
}

.swiper-image {
  width: 100%;
  height: 100%;
}

/* 物品信息 */
.info-section {
  background-color: #FFFFFF;
  padding: 20rpx;
  margin-bottom: 10rpx;
}

.price-section {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.price {
  font-size: 36rpx;
  font-weight: bold;
  color: #FF6A6A;
  margin-right: 15rpx;
}

.price.free {
  color: #00BFA5;
}

.status {
  font-size: 24rpx;
  padding: 5rpx 10rpx;
  border-radius: 8rpx;
}

.status.available {
  background-color: rgba(0, 191, 165, 0.1);
  color: #00BFA5;
}

.status.traded {
  background-color: rgba(153, 153, 153, 0.1);
  color: #999;
}

.status.donated {
  background-color: rgba(255, 106, 106, 0.1);
  color: #FF6A6A;
}

.title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 15rpx;
  line-height: 1.4;
}

.description {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 1.5;
}

.meta-info {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #F0F0F0;
}

.meta-item {
  display: flex;
  align-items: center;
  font-size: 24rpx;
  color: #999;
}

.meta-text {
  margin-left: 5rpx;
}

/* 发布者信息 */
.user-section {
  background-color: #FFFFFF;
  padding: 20rpx;
  margin-bottom: 100rpx;
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background-color: #F0F0F0;
  margin-right: 15rpx;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 5rpx;
}

.user-label {
  font-size: 24rpx;
  color: #999;
}

/* 底部操作栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #FFFFFF;
  padding: 20rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  z-index: 99;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 30rpx;
}

.action-text {
  font-size: 20rpx;
  color: #666;
  margin-top: 5rpx;
}

.contact-btn {
  flex: 1;
  background-color: #FF6A6A;
  color: #FFFFFF;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 40rpx;
  height: 80rpx;
  line-height: 80rpx;
  margin: 0;
}

.contact-btn::after {
  border: none;
}
</style>