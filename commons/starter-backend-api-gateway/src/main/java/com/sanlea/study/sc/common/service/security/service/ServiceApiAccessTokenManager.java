package com.sanlea.study.sc.common.service.security.service;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Access token manager
 *
 * @author kut
 */
public class ServiceApiAccessTokenManager {

    // current application id - inject from OS ENV
    @Getter
    @Value("${SERVICE_APPLICATION_ID}")
    private String applicationId;

    // current application secret - inject from OS ENV
    @Value("${SERVICE_APPLICATION_SECRET}")
    private String applicationSecret;

    // CSP server address
    @Value("${CSP_SERVER_ADDRESS}")
    private String cspServerAddress;

    // cached remote application access tokens
    private final Map<String, Map<String, LocalDateTime>> cachedRemoteApplicationAccessTokens;

    // REST template
    private final RestTemplate restTemplate;

    /**
     * constructor
     *
     * @param restTemplate rest template
     */
    public ServiceApiAccessTokenManager(RestTemplate restTemplate) {
        this.accessTokenMap = new HashMap<>();
        this.cachedRemoteApplicationAccessTokens = new HashMap<>();
        this.restTemplate = restTemplate;
    }

    /**
     * get access token
     *
     * @param remoteApplicationId      remote application id
     * @param remoteApplicationService remote application service
     * @return access token
     */
    public String getAccessToken(String remoteApplicationId, String remoteApplicationService) {
        var accessTokenMapKey = this.buildAccessTokenMapKey(
                remoteApplicationId,
                remoteApplicationService
        );
        var accessToken = this.accessTokenMap.get(accessTokenMapKey);
        if (accessToken == null || accessToken.isExpired()) {
            accessToken = this.retrieveAccessTokenByCsp(remoteApplicationId, remoteApplicationService);
            this.accessTokenMap.put(accessTokenMapKey, accessToken);
        }

        return accessToken.getToken();
    }


    /**
     * validate remote access token
     *
     * @param applicationId    application id
     * @param applicationToken application token
     * @return result
     */
    public boolean validateRemoteAccessToken(String applicationId, String applicationToken) {
        var tokenMap = this.cachedRemoteApplicationAccessTokens.get(applicationId);
        if (tokenMap != null) {
            var expireIn = tokenMap.get(applicationToken);
            if (expireIn != null && expireIn.isAfter(LocalDateTime.now())) {
                return true;
            }
        } else {
            tokenMap = new HashMap<>();
            this.cachedRemoteApplicationAccessTokens.put(applicationId, tokenMap);
        }

        var result = validateRemoteAccessTokenByCsp(
                applicationId,
                applicationToken
        );
        if (result.valid) {
            tokenMap.put(applicationToken, result.expireIn);
        }
        return result.valid;
    }


    /**
     * build access token map key
     *
     * @param applicationId      application id
     * @param applicationService application service
     * @return access token content
     */
    private String buildAccessTokenMapKey(String applicationId,
                                          String applicationService) {
        return "%s-%s".formatted(applicationId, applicationService);
    }


    /**
     * request to CSP server
     *
     * @param path          request path
     * @param responseClass response class
     * @param request       request
     * @param <Resp>        response type
     * @param <Req>         request type
     * @return response
     */
    private <Resp extends BaseCspResponse, Req> Resp requestCsp(String path, Class<Resp> responseClass, Req request) {
        // build request url
        var requestUrl = "%s%s".formatted(this.cspServerAddress, path);

        var requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.set("X-APPLICATION-ID", this.applicationId);
        requestHeaders.set("X-APPLICATION-SECRET", this.applicationSecret);

        var requestEntity = new HttpEntity<>(request, requestHeaders);

        try {
            var httpResponse = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    requestEntity,
                    responseClass
            );

            var cspResponse = httpResponse.getBody();
            if (cspResponse == null) {
                throw new ServiceApiCspRequestException(
                        "Unknown CSP error: %s".formatted(httpResponse.getStatusCode())
                );
            }

            if (!Objects.equals(cspResponse.getCode(), "SUCCESS")) {
                throw new ServiceApiCspRequestException("%s: %s".formatted(
                        cspResponse.getCode(),
                        cspResponse.getError()
                ));
            }

            return cspResponse;
        } catch (RestClientException ex) {
            throw new ServiceApiCspRequestException(ex.getMessage());
        }
    }


    /**
     * validate remote access token by CSP
     *
     * @param remoteApplicationId    remote application id
     * @param remoteApplicationToken remote application token
     * @return result
     */
    private ValidateAccessTokenResult validateRemoteAccessTokenByCsp(String remoteApplicationId,
                                                                     String remoteApplicationToken) {
        var cspRequest = new CspValidateAccessTokenRequest(remoteApplicationId, remoteApplicationToken);
        var cspResponse = this.requestCsp(
                "/token/validate",
                CspValidateAccessTokenResponse.class,
                cspRequest
        );
        return new ValidateAccessTokenResult(cspResponse.valid, cspResponse.expireIn);
    }


    /**
     * retrieve access token by CSP server
     *
     * @param remoteApplicationId      remote application id
     * @param remoteApplicationService remote application service
     */
    private ServiceApiAccessToken retrieveAccessTokenByCsp(String remoteApplicationId,
                                                           String remoteApplicationService) {
        var cspRequest = new CspAccessTokenRequest(remoteApplicationId, remoteApplicationService);
        var cspResponse = this.requestCsp(
                "/token",
                CspAccessTokenResponse.class,
                cspRequest
        );
        return new ServiceApiAccessToken(cspResponse.accessToken, cspResponse.expireIn);
    }


    /**
     * CSP access token response
     */
    @NoArgsConstructor
    @Getter
    @Setter
    private static class CspAccessTokenResponse extends BaseCspResponse {
        // access token
        private String accessToken;

        // expire in
        private LocalDateTime expireIn;
    }

    /**
     * CSP access token request
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class CspAccessTokenRequest {
        // application id
        private String applicationId;

        // application service
        private String applicationService;
    }

    /**
     * CSP validate access token request
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class CspValidateAccessTokenRequest {
        // remote application id
        private String remoteApplicationId;

        // remote application token
        private String remoteApplicationToken;
    }

    /**
     * CSP validate access token response
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    private static class CspValidateAccessTokenResponse extends BaseCspResponse {
        // valid
        private boolean valid;

        // access token expire in
        private LocalDateTime expireIn;
    }

    /**
     * Base response
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private abstract static class BaseCspResponse {
        // code
        private String code;

        // error
        private String error;
    }

    /**
     * validate access token result
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class ValidateAccessTokenResult {
        // valid
        private boolean valid;

        // access token expire
        private LocalDateTime expireIn;
    }
}
