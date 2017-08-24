package com.banzhiyan.tolerance.exception;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class RequestThrottleException extends RuntimeException {
    private static final long serialVersionUID = -961123567566229538L;

    public RequestThrottleException(String message) {
        super(message);
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}
