package com.sanlea.study.sc.common.starter.microservice.protocol.exception;

/**
 * Error codes
 *
 * @author kut
 */
public interface ErrorCodes {
    // remote service error
    String REMOTE_SERVICE_ERROR = "ERROR.REMOTE_SERVICE";

    // system error
    String SYSTEM_ERROR = "ERROR.SYSTEM";

    // parameters validation error
    String PARAMETERS_VALIDATION_ERROR = "ERROR.PARAMETERS_VALIDATION";
}
