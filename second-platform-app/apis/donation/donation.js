import request from '@/utils/request';

// 获取捐赠物品列表
export function getDonationList(params) {
  return request({
    url: '/api/donations',
    method: 'get',
    params
  });
}

// 获取捐赠物品详情
export function getDonationDetail(id) {
  return request({
    url: `/api/donations/${id}`,
    method: 'get'
  });
}

// 新增捐赠物品
export function createDonation(data) {
  return request({
    url: '/api/donations',
    method: 'post',
    data
  });
}

// 更新捐赠物品
export function updateDonation(id, data) {
  return request({
    url: `/api/donations/${id}`,
    method: 'put',
    data
  });
}

// 删除捐赠物品
export function deleteDonation(id) {
  return request({
    url: `/api/donations/${id}`,
    method: 'delete'
  });
}