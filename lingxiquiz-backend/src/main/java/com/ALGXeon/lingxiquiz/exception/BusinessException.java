package com.ALGXeon.lingxiquiz.exception;

import com.ALGXeon.lingxiquiz.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/ALGXeon">ALGXeon</a>
 * 
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
