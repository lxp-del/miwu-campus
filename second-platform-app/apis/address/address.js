import request from '@/utils/request'

/**
 * 新增地址
 * @param {Object} data
 * @returns {Promise}
 */
export function addAddress(data) {
	return request({
		url: '/api/address/add',
		method: 'post',
		data: data
	})
}

/**
 * 修改地址
 * @param {Object} data
 * @returns {Promise}
 */
export function updateAddress(data) {
	return request({
		url: '/api/address/update',
		method: 'post',
		data: data
	})
}

/**
 * 删除地址
 * @param {Number} id
 * @returns {Promise}
 */
export function deleteAddresss(id) {
	return request({
		url: '/api/address/delete',
		method: 'get',
		params: { id: id }
	})
}

export function morenAddresss(id) {
    return request({
        url: `/api/address/moren/${id}`,
        method: 'get'
    })
}

/**
 * 获取地址详情
 * @param {Number} id
 * @returns {Promise}
 */
export function getAddressInfo(id) {
	return request({
		url: '/api/address/info',
		method: 'get',
		params: { id: id }
	})
}

/**
 * 获取地址列表
 * @param {Object} query
 * @returns {Promise}
 */
export function getAddressList(query) {
	return request({
		url: '/api/address/list',
		method: 'get',
		params: query
	})
}