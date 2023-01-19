package com.sanlea.study.sc.common.starter.microservice_api_client.backend;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Access token
 *
 * @author kut
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "token")
public class BackendApiAccessToken {
    // access token
    private String token;

    // expire in
    private LocalDateTime expireIn;

    /**
     * check access token expired
     *
     * @return result
     */
    public boolean isExpired() {
        return expireIn.isBefore(LocalDateTime.now());
    }
}
