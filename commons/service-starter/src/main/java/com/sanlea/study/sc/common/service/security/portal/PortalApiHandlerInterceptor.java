package com.sanlea.study.sc.common.service.security.portal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Portal API handler interceptor
 *
 * @author kut
 */
public class PortalApiHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // save headers
        if (isPortalApiEndpoint(request)) {
            PortalApiRequestHeadersHolder.saveHeaders(request);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // clear headers
        if (isPortalApiEndpoint(request)) {
            PortalApiRequestHeadersHolder.clearHeaders();
        }
    }

    /**
     * check if is portal API endpoint
     *
     * @param request request
     * @return result
     */
    private boolean isPortalApiEndpoint(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/portal/");
    }
}
