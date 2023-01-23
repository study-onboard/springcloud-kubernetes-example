package com.sanlea.study.sc.common.starter.microservice.protocol.exception;

/**
 * Remote service exception
 *
 * @author kut
 */
public class RemoteServiceException extends RuntimeException{
    public RemoteServiceException(String message) {
        super(message);
    }
}
