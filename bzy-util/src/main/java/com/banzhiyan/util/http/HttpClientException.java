package com.banzhiyan.util.http;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class HttpClientException extends Exception {
    private static final long serialVersionUID = 3105630126342123965L;

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(Throwable cause) {
        super(cause);
    }
}

