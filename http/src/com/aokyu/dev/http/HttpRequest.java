/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.dev.http;

import com.aokyu.dev.http.content.MessageBody;

import java.net.URL;

/**
 * This class holds data for a HTTP request.
 */
public class HttpRequest {

    private final HttpMethod mMethod;
    private final URL mUrl;
    private final HttpHeaders mHeaders;
    private final MessageBody mMessageBody;

    /**
     * Creates a new HTTP request for the parameters.
     *
     * @param method The HTTP method.
     * @param url The URL to request.
     * @param requestHeaders The headers for the request.
     */
    public HttpRequest(HttpMethod method, URL url, HttpHeaders requestHeaders) {
        this(method, url, requestHeaders, null);
    }

    /**
     * Creates a new HTTP request for the parameters.
     *
     * @param method The HTTP method.
     * @param url The URL to request.
     * @param requestHeaders The headers for the request.
     * @param messageBody The message body.
     */
    public HttpRequest(HttpMethod method, URL url,
            HttpHeaders requestHeaders, MessageBody messageBody) {
        mMethod = method;
        mUrl = url;
        mHeaders = requestHeaders;
        mMessageBody = messageBody;
    }

    public HttpMethod getMethod() {
        return mMethod;
    }

    public String getMethodAsString() {
        return mMethod.name();
    }

    public URL getUrl() {
        return mUrl;
    }

    public String getHost() {
        return mUrl.getHost();
    }

    public HttpHeaders getHeaders() {
        return mHeaders;
    }

    public MessageBody getBody() {
        return mMessageBody;
    }
}
