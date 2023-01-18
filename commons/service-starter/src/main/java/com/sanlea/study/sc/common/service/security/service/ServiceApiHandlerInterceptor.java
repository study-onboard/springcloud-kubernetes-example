package com.sanlea.study.sc.common.service.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Service Api handler interceptor
 *
 * @author kut
 */
public class ServiceApiHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private ServiceApiAccessTokenManager accessTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (isServiceApiEndpoint(request)) {
            var applicationId = request.getHeader("X-APPLICATION-ID");
            var sapiToken = request.getHeader("X-SAPI-ACCESS-TOKEN");
            return accessTokenManager.validateRemoteAccessToken(applicationId, sapiToken);
        }
        return true;
    }

    /**
     * check if is service api endpoint
     *
     * @param request request
     * @return result
     */
    private boolean isServiceApiEndpoint(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/service/");
    }
}
