package com.platform.lxp.common.exception;

import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 来晓璞
 * @date 2026/3/30 19:25
 * @Description: 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: code={}, message={}, URI: {}", e.getCode(), e.getMessage(), request.getRequestURI());

        ResponseResult result = ResponseResult.error(e.getCode(), e.getMessage());

        // 如果有错误数据，添加到扩展字段中
        if (e.getData() != null) {
            result.putExtra("data", e.getData());
        }

        return result;
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("系统异常: code={}, message={}, URI: {}", e.getCode(), e.getMessage(), request.getRequestURI(), e);
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理 Spring Validation 参数验证异常 - @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.error("参数验证失败: {}, URI: {}", errorMsg, request.getRequestURI());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseResult.error(ResultCode.VALIDATE_FAILED_CODE, errorMsg)
                .putExtra("errors", errors);
    }

    /**
     * 处理参数绑定异常 - @Validated
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult handleBindException(BindException e, HttpServletRequest request) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.error("参数绑定失败: {}, URI: {}", errorMsg, request.getRequestURI());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseResult.error(ResultCode.PARAM_ERROR_CODE, errorMsg)
                .putExtra("errors", errors);
    }

    /**
     * 处理约束违反异常 - @Validated 校验单个参数
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.error("参数约束验证失败: {}, URI: {}", errorMsg, request.getRequestURI());
        return ResponseResult.error(ResultCode.PARAM_ERROR_CODE, errorMsg);
    }

    /**
     * 处理请求体格式错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("请求体格式错误: URI: {}", request.getRequestURI(), e);
        return ResponseResult.error(ResultCode.BAD_REQUEST_CODE, "请求参数格式错误");
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseResult handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("请求资源不存在: {}, URI: {}", e.getMessage(), request.getRequestURI());
        return ResponseResult.error(ResultCode.NOT_FOUND_CODE, ResultCode.NOT_FOUND_MSG);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常: URI: {}", request.getRequestURI(), e);
        return ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR_CODE, ResultCode.INTERNAL_SERVER_ERROR_MSG);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("非法参数异常: {}, URI: {}", e.getMessage(), request.getRequestURI());
        return ResponseResult.error(ResultCode.PARAM_ERROR_CODE, e.getMessage());
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: URI: {}", request.getRequestURI(), e);
        return ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR_CODE, ResultCode.INTERNAL_SERVER_ERROR_MSG);
    }
}
