package com.sanlea.study.sc.common.starter.microservice.protocol.exception;

import lombok.Getter;

/**
 * Business exception
 *
 * @author kut
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    private final String error;

    public BusinessException(String code, String error) {
        super("%s:%s".formatted(code, error));

        this.code = code;
        this.error = error;
    }
}
