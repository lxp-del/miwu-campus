import axios from 'axios'

// 创建 axios 实例
const service = axios.create({
	baseURL: process.env.NODE_ENV === 'production' ?
		'https://your-production-domain.com' :
		'http://localhost:9090', // 改为后端服务的实际地址
	timeout: 80000
});


// 打印环境信息
console.log('环境变量:', process.env.NODE_ENV)
console.log('基础URL:', service.defaults.baseURL)

// 请求拦截器
service.interceptors.request.use(config => {
  // 登录接口不要加 token
  if (!config.url.includes('/login')) {
    const token = uni.getStorageSync('token') || ''
    if (token) config.headers['Authorization'] = `Bearer ${token}`
  }
  return config
})

// 响应拦截器
service.interceptors.response.use(
	response => {
		return response.data
	},
	error => {
		console.error('响应错误:', error)
		if (error.response) {
			switch (error.response.status) {
				case 401:
					// 清除本地token
					if (typeof window !== 'undefined') {
						localStorage.removeItem('token')
					} else {
						uni.removeStorageSync('token')
					}
					uni.navigateTo({
						url: '/pages/login/index'
					})
					break
				case 403:
					uni.showToast({
						title: '没有权限访问',
						icon: 'none'
					})
					break
				case 500:
					uni.showToast({
						title: '服务器内部错误',
						icon: 'none'
					})
					break
				default:
					uni.showToast({
						title: error.response.data?.message || '请求失败',
						icon: 'none'
					})
			}
		} else if (error.message.includes('timeout')) {
			uni.showToast({
				title: '请求超时，请稍后重试',
				icon: 'none'
			})
		} else {
			uni.showToast({
				title: '网络错误，请检查网络连接',
				icon: 'none'
			})
		}
		return Promise.reject(error)
	}
)

export default service