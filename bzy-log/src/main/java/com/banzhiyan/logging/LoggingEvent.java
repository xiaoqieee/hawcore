package com.banzhiyan.logging;

import com.banzhiyan.log.annotation.Level;

/**
 * Created by xn025665 on 2017/8/23.
 */

class LoggingEvent {
    private final String identifier = RequestIdentifier.get();
    private final Level level;
    private final String name;
    private final String method;
    private final String message;
    private final Throwable throwable;
    private final Object[] arguments;
    private final long timestamp = System.currentTimeMillis();

    public LoggingEvent(String name, String method, Level level, String message) {
        this.name = name;
        this.method = method;
        this.level = level;
        this.message = this.nullToEmpty(message);
        this.throwable = null;
        this.arguments = null;
    }

    public LoggingEvent(String name, String method, Level level, String message, Throwable throwable) {
        this.name = name;
        this.method = method;
        this.level = level;
        this.message = this.nullToEmpty(message);
        this.throwable = throwable;
        this.arguments = null;
    }

    public LoggingEvent(String name, String method, Level level, String message, Throwable throwable, Object... arguments) {
        this.name = name;
        this.method = method;
        this.level = level;
        this.message = this.nullToEmpty(message);
        this.throwable = throwable;
        this.arguments = arguments;
    }

    private String nullToEmpty(String message) {
        return message == null?"":message;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Level getLevel() {
        return this.level;
    }

    public String getMessage() {
        return this.message;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public String getName() {
        return this.name;
    }

    public String getMethod() {
        return this.method;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}

