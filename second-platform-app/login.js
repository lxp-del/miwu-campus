 import request from '@/utils/request'

// 登录接口 - 注意路径
export function login(data) {
  return request({
    url: '/sys/user/login',  // 去掉 /api 前缀
    method: 'post',
    data
  })
}

// 登出接口
export function logout() {
  return request({
    url: '/logout',  // 去掉 /api 前缀
    method: 'post'
  })
}

// 获取用户信息接口
export function getUserInfo() {
  return request({
    url: '/user/info',  // 去掉 /api 前缀
    method: 'get'
  })
}