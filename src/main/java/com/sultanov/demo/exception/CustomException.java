package com.sultanov.demo.exception;

import com.sultanov.demo.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final int code;
    private final String message;

    public CustomException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }
}
