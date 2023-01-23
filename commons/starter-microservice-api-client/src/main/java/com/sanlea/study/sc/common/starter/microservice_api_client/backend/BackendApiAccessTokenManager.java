package com.sanlea.study.sc.common.starter.microservice_api_client.backend;

import com.sanlea.study.sc.common.starter.microservice.protocol.exception.MicroserviceError;
import com.sanlea.study.sc.common.starter.microservice.protocol.exception.RemoteServiceException;
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
public class BackendApiAccessTokenManager {
    // features
    // 1. request token used application id and secret by CSP service
    //    - token
    //    - expire in
    // 2. cache token

    // current access token
    private final Map<String, BackendApiAccessToken> accessTokenMap;

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

    // REST template
    private final RestTemplate restTemplate;

    /**
     * constructor
     *
     * @param restTemplate rest template
     */
    public BackendApiAccessTokenManager(RestTemplate restTemplate) {
        this.accessTokenMap = new HashMap<>();
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
    private <Resp extends MicroserviceError, Req> Resp requestCsp(String path, Class<Resp> responseClass, Req request) {
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
                throw new RemoteServiceException(
                        "Unknown CSP error: %s".formatted(httpResponse.getStatusCode())
                );
            }

            if (!Objects.equals(cspResponse.getCode(), "SUCCESS")) {
                throw new RemoteServiceException("%s: %s".formatted(
                        cspResponse.getCode(),
                        cspResponse.getError()
                ));
            }

            return cspResponse;
        } catch (RestClientException ex) {
            throw new RemoteServiceException("Access CSP service failed: %s".formatted(ex.getMessage()));
        }
    }


    /**
     * retrieve access token by CSP server
     *
     * @param remoteApplicationId      remote application id
     * @param remoteApplicationService remote application service
     */
    private BackendApiAccessToken retrieveAccessTokenByCsp(String remoteApplicationId,
                                                           String remoteApplicationService) {
        var cspRequest = new CspAccessTokenRequest(remoteApplicationId, remoteApplicationService);
        var cspResponse = this.requestCsp(
                "/token",
                CspAccessTokenResponse.class,
                cspRequest
        );
        return new BackendApiAccessToken(cspResponse.accessToken, cspResponse.expireIn);
    }


    /**
     * CSP access token response
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    private static class CspAccessTokenResponse extends MicroserviceError {
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
}
