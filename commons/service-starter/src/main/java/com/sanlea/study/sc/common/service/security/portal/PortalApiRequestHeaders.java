package com.sanlea.study.sc.common.service.security.portal;

import java.util.Collection;
import java.util.HashMap;

/**
 * Request headers
 *
 * @author kut
 */
public final class PortalApiRequestHeaders extends HashMap<String, Collection<String>> {

    /**
     * get header
     *
     * @param name header name
     * @return header
     */
    public String getHeader(String name) {
        var headers = get(name.toUpperCase());
        return headers == null || headers.isEmpty() ? null : headers.stream().toList().get(0);
    }

    /**
     * get headers
     *
     * @param name name
     * @return headers
     */
    public Collection<String> getHeaders(String name) {
        var headers = get(name.toUpperCase());
        return headers == null || headers.isEmpty() ? null : headers;
    }
}
