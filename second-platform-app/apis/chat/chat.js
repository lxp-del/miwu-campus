import request from '@/utils/request'

/**
 * 获取用户的会话列表
 * @param {number} currentUserId - 当前用户ID
 * @returns {Promise} 会话列表
 */
export const getThreadList = (currentUserId) => {
	return request({
		url: `/chat/threads/${currentUserId}`,
		method: 'GET'
	})
}


export function getHistoyByThreadId(threadId) {
	return request({
		url: `/chat/messages/${threadId}`,
		method: 'get'
	})
}

//清空已读
export function markAsRead(data) {
    return request({
        url: '/chat/read',
        method: 'post',
        data: data
    })
}
