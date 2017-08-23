package com.banzhiyan.logging.log4j;

import com.banzhiyan.logging.Journal;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Created by xn025665 on 2017/8/23.
 */

public class Log4JLogger extends Journal {
    private final Logger logger;

    public Log4JLogger(String name) {
        this.logger = Logger.getLogger(name);
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
        return this.logger.isEnabledFor(Level.WARN);
    }

    public boolean isErrorEnabled() {
        return this.logger.isEnabledFor(Level.ERROR);
    }
}

