package com.sanlea.study.sc.common.starter.microservice.protocol.exception;

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
public class MicroserviceError {
    // code
    private String code;

    // error
    private String error;
}
