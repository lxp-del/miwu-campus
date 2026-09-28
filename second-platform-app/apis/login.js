import service from '@/utils/request'

// 登录接口
export function login(formData) {
  return service({
    url: '/sys/user/login',
    method: 'post',
    data: formData,
    headers: {}   
  });
}

// 登出接口
export function logout() {
  return service({  // 统一用 service
    url: '/logout',
    method: 'post'
  });
}

// 获取用户信息接口
export function getUserInfo() {
  return service({
    url: '/user/info',
    method: 'get'
  });
}