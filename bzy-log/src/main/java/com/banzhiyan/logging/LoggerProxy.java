package com.banzhiyan.logging;

import com.banzhiyan.log.annotation.Level;
import com.banzhiyan.logging.util.FormattingTuple;
import com.banzhiyan.logging.util.MessageFormatter;

/**
 * Created by xn025665 on 2017/8/23.
 */

class LoggerProxy implements Logger {
    private static final Journal logger = JournalHolder.getLogger(LoggerProxy.class.getName());
    private final String name;

    public LoggerProxy(String name) {
        this.name = name;
    }

    private void logging(LoggingEvent event) {
        Journal journal = JournalHolder.getLogger(event.getName());
        switch (event.getLevel().ordinal()) {
            case 1:
                journal.info0(event);
                break;
            case 2:
                journal.error0(event);
                break;
            case 3:
                journal.warn0(event);
                break;
            case 4:
            default:
                journal.debug0(event);
        }

    }

    public void error(String format, Object... arguments) {
        this.logging(this.buildEvent(Level.ERROR, format, arguments));
    }

    private LoggingEvent buildEvent(Level level, String format, Object[] arguments) {
        FormattingTuple fmt = MessageFormatter.arrayFormat(format, arguments);
        return new LoggingEvent(this.name, this.method(), level, fmt.getMessage(), fmt.getThrowable(), arguments);
    }

    public void warn(String format, Object... arguments) {
        this.logging(this.buildEvent(Level.WARN, format, arguments));
    }

    public void info(String format, Object... arguments) {
        this.logging(this.buildEvent(Level.INFO, format, arguments));
    }

    public void debug(String format, Object... arguments) {
        this.logging(this.buildEvent(Level.DEBUG, format, arguments));
    }

    public void catching(Throwable throwable) {
        this.error("catching", throwable);
    }

    public <T extends Throwable> T throwing(T throwable) {
        this.error("throwing", throwable);
        return throwable;
    }

    public void error(String message) {
        this.logging(this.buildEvent(Level.ERROR, message));
    }

    private LoggingEvent buildEvent(Level level, String message) {
        return new LoggingEvent(this.name, this.method(), level, message);
    }

    public void warn(String message) {
        this.logging(this.buildEvent(Level.WARN, message));
    }

    public void info(String message) {
        this.logging(this.buildEvent(Level.INFO, message));
    }

    public void debug(String message) {
        this.logging(this.buildEvent(Level.DEBUG, message));
    }

    public void error(String message, Throwable error) {
        this.logging(this.buildEvent(Level.ERROR, message, error));
    }

    private LoggingEvent buildEvent(Level level, String message, Throwable error) {
        return new LoggingEvent(this.name, this.method(), level, message, error);
    }

    public void warn(String message, Throwable error) {
        this.logging(this.buildEvent(Level.WARN, message, error));
    }

    public void info(String message, Throwable error) {
        this.logging(this.buildEvent(Level.INFO, message, error));
    }

    public void debug(String message, Throwable error) {
        this.logging(this.buildEvent(Level.DEBUG, message, error));
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    private String method() {
        return "";
    }
}

