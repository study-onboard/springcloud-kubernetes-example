package com.sanlea.study.sc.common.starter.microservice.exception;

import lombok.*;

/**
 * API error
 *
 * @author kut
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MicroserviceApiError {
    // code
    private String code;

    // error
    private String error;
}
