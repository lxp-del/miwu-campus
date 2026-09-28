import request from '@/utils/request'

/**
 * 获取评论列表
 * 对应后端：@GetMapping("/list")
 * @returns {Promise}
 */
export function getCommentList(goodId) {
    return request({
        url: `/commentss/list/${goodId}`,  
        method: 'get'
         
    })
}
/**
 * 新增评论
 * 对应后端：@PostMapping
 * @param {Object} data - 评论数据对象 (包含 orderId, content, star 等字段)
 * @returns {Promise}
 */
export function addComment(data) {
    return request({
        url: '/commentss/add',
        method: 'post',
        data: data
    })
}

 