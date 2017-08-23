package com.banzhiyan.core.concurrent;

import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class NamedThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);
    private static final AtomicLong POOL_SEQ = new AtomicLong(1L);
    private final AtomicLong threadNumber;
    private final ThreadGroup threadGroup;
    private final String namePrefix;
    private final boolean isDaemon;

    public NamedThreadFactory() {
        this("Thread-Pool-" + POOL_SEQ.getAndIncrement());
    }

    public NamedThreadFactory(String name) {
        this(name, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this.threadNumber = new AtomicLong(1L);
        this.namePrefix = prefix + "-thread-";
        this.isDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        this.threadGroup = s != null?s.getThreadGroup():Thread.currentThread().getThreadGroup();
    }

    public Thread newThread(Runnable runnable) {
        String name = this.namePrefix + this.threadNumber.getAndIncrement();
        Thread thread = new Thread(this.threadGroup, runnable, name, 0L);
        thread.setDaemon(this.isDaemon);
        thread.setUncaughtExceptionHandler(this);
        return thread;
    }

    public void uncaughtException(Thread t, Throwable e) {
        logger.error("Uncaught Exception in thread " + t.getName(), e);
    }
}
