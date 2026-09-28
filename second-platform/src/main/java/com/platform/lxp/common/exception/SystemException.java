package com.platform.lxp.common.exception;

import com.platform.lxp.common.ResultCode;
import lombok.Getter;

/**
 * @author 来晓璞
 * @date 2026/3/30 19:24
 * @Description: 系统异常
 */
@Getter
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Integer code;
    private final String message;

    public SystemException(String message) {
        super(message);
        this.code = ResultCode.INTERNAL_SERVER_ERROR_CODE;
        this.message = message;
    }

    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.INTERNAL_SERVER_ERROR_CODE;
        this.message = message;
    }

    public SystemException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
