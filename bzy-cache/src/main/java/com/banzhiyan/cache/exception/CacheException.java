package com.banzhiyan.cache.exception;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class CacheException extends RuntimeException {
    private static final long serialVersionUID = 358692444730213098L;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable e) {
        super(e);
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}
