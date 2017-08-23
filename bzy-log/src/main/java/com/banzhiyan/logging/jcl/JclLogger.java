package com.banzhiyan.logging.jcl;

import com.banzhiyan.logging.Journal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class JclLogger extends Journal {
    private final Log logger;

    public JclLogger(String name) {
        this.logger = LogFactory.getLog(name);
    }

    public void error() {
        this.logger.error(this.buildMessage());
    }

    public void warn() {
        this.logger.warn(this.buildMessage());
    }

    public void info() {
        this.logger.info(this.buildMessage());
    }

    public void debug() {
        this.logger.debug(this.buildMessage());
    }

    public void error(Throwable error) {
        this.logger.error(this.buildMessage(), error);
    }

    public void warn(Throwable error) {
        this.logger.warn(this.buildMessage(), error);
    }

    public void info(Throwable error) {
        this.logger.info(this.buildMessage(), error);
    }

    public void debug(Throwable error) {
        this.logger.debug(this.buildMessage(), error);
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }
}
