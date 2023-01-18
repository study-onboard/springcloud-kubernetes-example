package com.sanlea.study.sc.common.service.security.portal;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

/**
 * Request headers holder
 *
 * @author kut
 */
public final class PortalApiRequestHeadersHolder {
    // request headers
    private static final ThreadLocal<PortalApiRequestHeaders> requestHeaders = new ThreadLocal<>();

    private PortalApiRequestHeadersHolder() {}

    /**
     * Get headers
     *
     * @return request headers
     */
    public static PortalApiRequestHeaders getHeaders() {
        var result = requestHeaders.get();
        return result == null ? new PortalApiRequestHeaders() : result;
    }

    /**
     * save headers
     *
     * @param request http request
     */
    public static void saveHeaders(HttpServletRequest request) {
        var headers = new PortalApiRequestHeaders();
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var headerName = headerNames.nextElement();
            var headerValues = request.getHeaders(headerName);

            var values = new ArrayList<String>();
            while (headerValues.hasMoreElements()) {
                var value = headerValues.nextElement();
                values.add(value);
            }
            headers.put(headerName.toUpperCase(), values);
        }
        requestHeaders.set(headers);
    }

    /**
     * clear headers
     */
    public static void clearHeaders() {
        requestHeaders.remove();
    }
}
