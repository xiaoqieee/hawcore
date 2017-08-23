package com.banzhiyan.logging.slf4j;

import com.banzhiyan.logging.Journal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class Slf4jLogger extends Journal {
    private final Logger logger;

    public Slf4jLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
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
