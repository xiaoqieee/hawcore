package com.banzhiyan.logging.jcl;

import com.banzhiyan.logging.Journal;
import com.banzhiyan.logging.LoggerFactoryDelegate;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class JclLoggerFactoryDelegate implements LoggerFactoryDelegate {
    public JclLoggerFactoryDelegate() {
    }

    public Journal getLogger(String name) {
        return new JclLogger(name);
    }

    public String toString() {
        return "Apache Commons Logging";
    }
}
