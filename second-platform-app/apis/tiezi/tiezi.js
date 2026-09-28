import request from '@/utils/request'

/**
 * 发布新帖子
 * 对应后端：@PostMapping("/add")
 * @param {Object} data - 帖子数据对象 (包含 title, content, imageUrl 等字段)
 * @returns {Promise}
 */
export function addPost(data) {
    return request({
        url: '/forum/post/add',
        method: 'post',
        data: data
    })
}

/**
 * 获取帖子详情
 * 对应后端：@GetMapping("/detail/{id}")
 * @param {Long} id - 帖子ID
 * @returns {Promise}
 */
export function getPostDetail(id) {
    return request({
        url: `/forum/post/detail/${id}`,
        method: 'get'
    })
}
/**
 * 获取帖子列表
 */
export function getPostList() {
  return request({
    url: '/forum/post/list', // 对应后端的 @GetMapping("/list")
    method: 'get'
  })
}