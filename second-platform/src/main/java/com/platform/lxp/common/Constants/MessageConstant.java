package com.platform.lxp.common.Constants;

/**
 * 提示信息常量类
 * 所有前端返回的提示文字统一放这里
 */
public class MessageConstant {

    // ==================== 通用 ====================
    public static final String SUCCESS = "操作成功";
    public static final String FAIL = "操作失败";
    public static final String SYSTEM_ERROR = "系统异常，请稍后重试";

    // ==================== 登录相关 ====================
    public static final String ACCOUNT_NOT_EXIST = "账号不存在";
    public static final String USERNAME_IS_EMPTY = "用户名不能为空";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String PHONE_IS_EMPTY = "手机号不能为空";
    public static final String CODE_IS_EMPTY = "验证码不能为空";
    public static final String CODE_ERROR = "验证码错误";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGOUT_SUCCESS = "退出成功";
    public static final String PASSWORD_IS_EMPTY = "密码不能为空";
    public static final String ID_IS_EMPTY = "ID不能为空";
    public static final String PRICE_IS_EMPTY = "价格不能为空";
    public static final String ADDRESS_IS_EMPTY = "地址不能为空";

    // ==================== 学生相关 ====================
    public static final String STUDENT_NOT_FOUND = "未找到该学生信息";
    public static final String STUDENT_LOGIN_SUCCESS = "学生登录成功";
    public static final String STUDENT_AUTO_REGISTER_SUCCESS = "新学生自动注册并登录成功";

    // ==================== OCR 图片识别 ====================
    public static final String OCR_RECOGNIZE_FAIL = "图片识别失败，请重新上传";
    public static final String OCR_NOT_FOUND_NAME_STUDENTID = "未识别到姓名或学号";

    // ==================== 文件上传 ====================
    public static final String FILE_IS_EMPTY = "上传文件不能为空";
    public static final String FILE_SIZE_LIMIT = "文件大小超出限制";

    // ==================== 商品上传相关 ====================
    public static final String GOODS_NOT_FOUND = "商品不存在";
    public static final String GOODS_buy_fail = "商品购买失败";
    public static final String PARENT_COMMENT_NOT_FOUND = "父评论不存在";
}
