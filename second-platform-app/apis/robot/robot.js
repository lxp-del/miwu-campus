import request from '@/utils/request'


export function chatDeepSeek(data) {
	return request({
		url: '/api/deepseek/chat',  
		method: 'post',
		data: data  
	})
}
export function chatDeepSeekcpy(data) {
	return request({
		url: '/api/deepseek/chatcy',  
		method: 'post',
		data: data  
	})
}