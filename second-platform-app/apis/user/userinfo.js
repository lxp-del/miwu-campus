import request from '@/utils/request'

/**
 * 查询用户个人信息
 * @returns {Promise} 
 */
export function getUserInfo() {
	return request({
		url: '/sys/user/userinfo',
		method: 'get'
	})
}

 
export function getUserById(id) {
	return request({
		url: `/sys/user/getById/${id}`,
		method: 'get'
	})
}

/**
 * 修改用户信息
 * @param {Object} data - 包含用户信息的对象 (如: { id: 1, nickname: 'NewName', phone: '...' })
 */
export function updateUserInfo(data) {
    return request({
        url: '/sys/user/update',
        method: 'put',
        data: data // PUT 请求通常将参数放在 data 中
    })
}