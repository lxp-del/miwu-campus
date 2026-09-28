import service from '@/utils/request'

/**
 * 上传文件接口
 * @param {File} file 文件对象
 */
// api/file.js
export function uploadFile(formData) {
	return service({
		url: '/file/upload',
		method: 'post',
		data: formData,
	});
}