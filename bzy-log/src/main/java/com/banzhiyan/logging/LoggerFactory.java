package com.banzhiyan.logging;

/**
 * Created by xn025665 on 2017/8/23.
 */
public final class LoggerFactory {
    public LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return new LoggerProxy(name);
    }
}
