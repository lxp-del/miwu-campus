package com.platform.lxp.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 来晓璞
 * @date 2026/3/30 19:31
 * @Description: 统一响应结果
 */
@Data
@Accessors(chain = true)
public class ResponseResult {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    /**
     * 扩展字段
     */
    private Map<String, Object> extra;

    public ResponseResult() {
        this.extra = new HashMap<>();
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.extra = new HashMap<>();
    }

    public ResponseResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.extra = new HashMap<>();
    }

    /**
     * 成功响应
     */
    public static ResponseResult success() {
        return new ResponseResult(ResultCode.SUCCESS_CODE, ResultCode.SUCCESS_MSG);
    }

    public static ResponseResult success(Object data) {
        return new ResponseResult(ResultCode.SUCCESS_CODE, ResultCode.SUCCESS_MSG, data);
    }

    public static ResponseResult success(String message, Object data) {
        return new ResponseResult(ResultCode.SUCCESS_CODE, message, data);
    }

    /**
     * 失败响应
     */
    public static ResponseResult error(String message) {
        return new ResponseResult(ResultCode.BUSINESS_ERROR_CODE, message);
    }

    public static ResponseResult error(Integer code, String message) {
        return new ResponseResult(code, message);
    }

    /**
     * 添加扩展字段
     */
    public ResponseResult putExtra(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.put(key, value);
        return this;
    }
}
