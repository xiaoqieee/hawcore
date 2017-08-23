package com.banzhiyan.logging.jdk;

import com.banzhiyan.logging.Journal;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class JulLogger extends Journal {
    private final Logger logger;

    public JulLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    public void error() {
        this.logger.log(Level.SEVERE, this.buildMessage());
    }

    public void warn() {
        this.logger.log(Level.WARNING, this.buildMessage());
    }

    public void info() {
        this.logger.log(Level.INFO, this.buildMessage());
    }

    public void debug() {
        this.logger.log(Level.FINE, this.buildMessage());
    }

    public void error(Throwable error) {
        this.logger.log(Level.SEVERE, this.buildMessage(), error);
    }

    public void warn(Throwable error) {
        this.logger.log(Level.WARNING, this.buildMessage(), error);
    }

    public void info(Throwable error) {
        this.logger.log(Level.INFO, this.buildMessage(), error);
    }

    public void debug(Throwable error) {
        this.logger.log(Level.FINE, this.buildMessage(), error);
    }

    public boolean isDebugEnabled() {
        return this.logger.isLoggable(Level.FINE);
    }

    public boolean isInfoEnabled() {
        return this.logger.isLoggable(Level.INFO);
    }

    public boolean isWarnEnabled() {
        return this.logger.isLoggable(Level.WARNING);
    }

    public boolean isErrorEnabled() {
        return this.logger.isLoggable(Level.SEVERE);
    }
}
