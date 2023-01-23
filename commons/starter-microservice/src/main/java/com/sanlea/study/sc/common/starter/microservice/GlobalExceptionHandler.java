package com.sanlea.study.sc.common.starter.microservice;

import com.sanlea.study.sc.common.starter.microservice.exception.BusinessException;
import com.sanlea.study.sc.common.starter.microservice.exception.MicroserviceApiError;
import com.sanlea.study.sc.common.starter.microservice.protocol.exception.ErrorCodes;
import com.sanlea.study.sc.common.starter.microservice.protocol.exception.RemoteServiceException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

/**
 * Global exception handler
 *
 * @author kut
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * handle validation exception
     *
     * @param ex exception
     * @return entity
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<MicroserviceApiError> handleValidationException(BindException ex) {
        var errorList = new ArrayList<String>();
        ex.getAllErrors().forEach(err -> {
            if (err instanceof FieldError error) {
                errorList.add("%s:%s".formatted(error.getField(), error.getDefaultMessage()));
            } else {
                errorList.add(err.getDefaultMessage());
            }
        });
        var errorMessage = Strings.join(errorList.iterator(), ';');

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = new MicroserviceApiError(ErrorCodes.PARAMETERS_VALIDATION_ERROR, errorMessage);
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.BAD_REQUEST);
    }


    /**
     * handle business exception
     *
     * @param ex business exception
     * @return entity of response
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MicroserviceApiError> handleBusinessException(BusinessException ex) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = new MicroserviceApiError(ex.getCode(), ex.getError());
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.PRECONDITION_FAILED);
    }


    /**
     * Handle remote service exception
     *
     * @param ex remote service exception
     * @return entity
     */
    @ExceptionHandler(RemoteServiceException.class)
    @ConditionalOnMissingBean
    public ResponseEntity<MicroserviceApiError> handleRemoteServiceException(RemoteServiceException ex) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = new MicroserviceApiError(ErrorCodes.REMOTE_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.GATEWAY_TIMEOUT);
    }

    /**
     * default exception handler
     *
     * @param ex exception
     * @return entity of response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MicroserviceApiError> handleDefaultException(Exception ex) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = new MicroserviceApiError(ErrorCodes.SYSTEM_ERROR, ex.getMessage());
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.SERVICE_UNAVAILABLE);
    }
}