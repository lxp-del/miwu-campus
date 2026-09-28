package com.platform.lxp.common;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:37
 * @Description: 响应结果常量类
 */
public class ResultCode {

    /**
     * 成功状态码
     */
    public static final Integer SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "操作成功";

    /**
     * 失败状态码
     */
    public static final Integer FAILURE_CODE = 500;
    public static final String FAILURE_MSG = "操作失败";

    /**
     * 错误状态码
     */
    public static final Integer ERROR_CODE = 400;
    public static final String ERROR_MSG = "系统错误";

    /**
     * 客户端错误状态码 (4xx)
     */
    public static final Integer BAD_REQUEST_CODE = 400;
    public static final String BAD_REQUEST_MSG = "请求参数错误";

    public static final Integer UNAUTHORIZED_CODE = 401;
    public static final String UNAUTHORIZED_MSG = "未授权，请登录";

    public static final Integer FORBIDDEN_CODE = 403;
    public static final String FORBIDDEN_MSG = "拒绝访问";

    public static final Integer NOT_FOUND_CODE = 404;
    public static final String NOT_FOUND_MSG = "请求资源不存在";

    public static final Integer METHOD_NOT_ALLOWED_CODE = 405;
    public static final String METHOD_NOT_ALLOWED_MSG = "请求方法不支持";

    public static final Integer VALIDATE_FAILED_CODE = 422;
    public static final String VALIDATE_FAILED_MSG = "参数验证失败";

    /**
     * 服务器错误状态码 (5xx)
     */
    public static final Integer INTERNAL_SERVER_ERROR_CODE = 500;
    public static final String INTERNAL_SERVER_ERROR_MSG = "服务器内部错误";

    public static final Integer SERVICE_UNAVAILABLE_CODE = 503;
    public static final String SERVICE_UNAVAILABLE_MSG = "服务不可用";

    /**
     * 业务错误状态码 (1xxx - 业务相关)
     */
    public static final Integer BUSINESS_ERROR_CODE = 1001;
    public static final String BUSINESS_ERROR_MSG = "业务处理失败";

    public static final Integer DATA_NOT_FOUND_CODE = 1002;
    public static final String DATA_NOT_FOUND_MSG = "数据不存在";

    public static final Integer DATA_EXIST_CODE = 1003;
    public static final String DATA_EXIST_MSG = "数据已存在";

    public static final Integer PARAM_ERROR_CODE = 1004;
    public static final String PARAM_ERROR_MSG = "参数错误";
}
