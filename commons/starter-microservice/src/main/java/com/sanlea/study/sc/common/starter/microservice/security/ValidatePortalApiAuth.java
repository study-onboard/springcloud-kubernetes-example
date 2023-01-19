package com.sanlea.study.sc.common.starter.microservice.security;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Validate portal API auth
 *
 * @author kut
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ValidatePortalApiAuth {
}
