package com.sanlea.study.sc.common.starter.microservice_api_client.backend;

import java.lang.annotation.*;

/**
 * Service API
 *
 * @author kut
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BackendAPI {

    // application id
    String applicationId();

    // application service name
    String service();
}
