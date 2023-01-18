package com.sanlea.study.sc.common.service.security.service;

import java.lang.annotation.*;

/**
 * Service API
 *
 * @author kut
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceAPI {

    // application id
    String applicationId();

    // application service name
    String service();
}
