package com.banzhiyan.util.http;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class HttpRequest {
    private final int DEFAULT_CONNECTION_TIMEOUT;
    private final int DEFAULT_SO_TIMEOUT;
    public static String DEFAULT_CHARSET = "UTF-8";
    private final String targetUrl;
    private HttpRequest.METHOD method;
    private int timeout;
    private int connectionTimeout;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String charset;

    public HttpRequest(String targetUrl) {
        this(targetUrl, DEFAULT_CHARSET);
    }

    public HttpRequest(String targetUrl, String charset) {
        this.DEFAULT_CONNECTION_TIMEOUT = 10000;
        this.DEFAULT_SO_TIMEOUT = 10000;
        this.method = HttpRequest.METHOD.POST;
        this.timeout = 10000;
        this.connectionTimeout = 10000;
        this.charset = DEFAULT_CHARSET;
        this.targetUrl = targetUrl;
        charset = charset == null?DEFAULT_CHARSET:charset;
        Charset.forName(charset);
        this.charset = charset;
    }

    public Map<String, String> getParameters() {
        return (Map)(this.parameters == null?new HashMap(0):this.parameters);
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders() {
        return (Map)(this.headers == null?new HashMap(0):this.headers);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getTargetUrl() {
        return this.targetUrl;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Charset getCharset() {
        return Charset.forName(this.charset);
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public HttpRequest.METHOD getMethod() {
        return this.method;
    }

    public void setMethod(HttpRequest.METHOD method) {
        this.method = method;
    }

    public static enum METHOD {
        POST,
        GET;

        private METHOD() {
        }
    }
}

