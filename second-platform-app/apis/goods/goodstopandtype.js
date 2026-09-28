import request from '@/utils/request'

/**
 * 查询商品类型列表
 * @returns {Promise} 
 */
export function getGoodsTypeList() {
	return request({
		url: '/goods/type/list',
		method: 'get'
	})
}

/**
 * 查询商品话题列表
 * @description  
 * @returns {Promise} 
 */
export function getGoodsTopicList() {
	return request({
		url: '/goods/topic/list',
		method: 'get'
	})
}

/**
 * 新增订单商品
 * @param {Object} data 
 * @returns {Promise}  
 */
export function addOrderGoods(data) {
	return request({
		url: '/second/goods/add', // 拼接路径：类上的 @RequestMapping + 方法上的 @PostMapping
		method: 'post',
		data: data // POST 请求，数据放在 body 中
	})
}

/**
 * 商品列表信息
 */
export function getGoodsList(data) {
	return request({
		url: '/second/goods/list/type',
		method: 'post',
		data: data
	})
}

/**
 * 查询详情数据
 * @param {number} id - 商品ID
 */
export function getGoodsById(id) {
	return request({
		url: `/second/goods/getById/${id}`, // 使用模板字符串拼接
		method: 'get'
	})
}

/**
 * 商品列表全部信息
 */
export function getGoodsListAll() {
	return request({
		url: '/second/goods/listAll',
		method: 'get'
	})
}