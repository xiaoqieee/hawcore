package com.banzhiyan.logging.jdk;

import com.banzhiyan.logging.Journal;
import com.banzhiyan.logging.LoggerFactoryDelegate;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class JulLoggerFactoryDelegate implements LoggerFactoryDelegate {
    public JulLoggerFactoryDelegate() {
    }

    public Journal getLogger(String name) {
        return new JulLogger(name);
    }

    public String toString() {
        return "Java Util Logging";
    }
}

