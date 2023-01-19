package com.sanlea.study.sc.common.starter.microservice_api_client.backend;

/**
 * CSP request exception
 *
 * @author kut
 */
public class BackendApiCspRequestException extends RuntimeException{
    public BackendApiCspRequestException(String message) {
        super(message);
    }
}
