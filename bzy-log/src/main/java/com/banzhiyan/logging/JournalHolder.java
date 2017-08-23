package com.banzhiyan.logging;

import com.banzhiyan.logging.jdk.JulLoggerFactoryDelegate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xn025665 on 2017/8/23.
 */

class JournalHolder {
    private static final ConcurrentMap<String, Journal> JOURNALS = new ConcurrentHashMap();
    private static LoggerFactoryDelegate loggerFactoryDelegate;

    JournalHolder() {
    }

    public static Journal getLogger(String name) {
        Journal journal = (Journal)JOURNALS.get(name);
        if(journal == null) {
            journal = loggerFactoryDelegate.getLogger(name);
            Journal prev = (Journal)JOURNALS.putIfAbsent(name, journal);
            if(prev != null) {
                journal = prev;
            }
        }

        return journal;
    }

    private static void fallbackToDefault() {
        loggerFactoryDelegate = new JulLoggerFactoryDelegate();
    }

    static {
        String cname = null;

        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            cname = "ooh.bravo.logging.slf4j.Slf4JLoggerFactoryDelegate";
        } catch (Throwable var5) {
            ;
        }

        if(cname == null) {
            try {
                Class.forName("org.apache.log4j.LogManager");
                cname = "ooh.bravo.logging.log4j.Log4JLoggerFactoryDelegate";
            } catch (Throwable var4) {
                ;
            }
        }

        if(cname == null) {
            try {
                Class.forName("org.apache.commons.logging.LogFactory");
                cname = "ooh.bravo.logging.jcl.JclLoggerFactoryDelegate";
            } catch (Throwable var3) {
                ;
            }
        }

        try {
            if(cname != null) {
                Class<?> clazz = Class.forName(cname, true, Thread.currentThread().getContextClassLoader());
                loggerFactoryDelegate = (LoggerFactoryDelegate)clazz.newInstance();
            } else {
                fallbackToDefault();
            }
        } catch (Throwable var2) {
            fallbackToDefault();
        }

    }
}
