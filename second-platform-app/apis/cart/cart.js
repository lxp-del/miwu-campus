import request from '@/utils/request'

/**
 * 加入购物车
 * @param {Object} data 
 * @returns {Promise}  
 */
export function addCartGoods(data) {
	return request({
		url: '/api/cart/add',  
		method: 'post',
		data: data  
	})
}

/**
 * 获取我的购物车列表
 */
export function getCartList() {
  return request({
    url: '/api/cart/list',
    method: 'get'
  })
}