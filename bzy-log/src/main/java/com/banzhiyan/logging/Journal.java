package com.banzhiyan.logging;

import com.banzhiyan.logging.util.DateFormatUtils;

import java.util.Date;

/**
 * Created by xn025665 on 2017/8/23.
 */
public abstract class Journal {
    private static final ThreadLocal<LoggingEvent> EVENT_LOCAL = new ThreadLocal();

    public Journal() {
    }

    protected final String buildMessage() {
        LoggingEvent event = (LoggingEvent)EVENT_LOCAL.get();
        if(event == null) {
            return "";
        } else {
            StringBuilder buf = new StringBuilder(64);
            buf.append(event.getMethod()).append(event.getMessage());
            buf.append(" [");
            buf.append(event.getIdentifier());
            buf.append("]~");
            buf.append(DateFormatUtils.format(new Date(event.getTimestamp())));
            return buf.toString();
        }
    }

    void error0(LoggingEvent event) {
        EVENT_LOCAL.set(event);
        if(event.getThrowable() == null) {
            this.error();
        } else {
            this.error(event.getThrowable());
        }

    }

    void warn0(LoggingEvent event) {
        EVENT_LOCAL.set(event);
        if(event.getThrowable() == null) {
            this.warn();
        } else {
            this.warn(event.getThrowable());
        }

    }

    void info0(LoggingEvent event) {
        EVENT_LOCAL.set(event);
        if(event.getThrowable() == null) {
            this.info();
        } else {
            this.info(event.getThrowable());
        }

    }

    void debug0(LoggingEvent event) {
        EVENT_LOCAL.set(event);
        if(event.getThrowable() == null) {
            this.debug();
        } else {
            this.debug(event.getThrowable());
        }

    }

    protected abstract void error();

    protected abstract void warn();

    protected abstract void info();

    protected abstract void debug();

    protected abstract void error(Throwable var1);

    protected abstract void warn(Throwable var1);

    protected abstract void info(Throwable var1);

    protected abstract void debug(Throwable var1);

    protected abstract boolean isDebugEnabled();

    protected abstract boolean isInfoEnabled();

    protected abstract boolean isWarnEnabled();

    protected abstract boolean isErrorEnabled();
}
