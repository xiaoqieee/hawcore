package com.banzhiyan.util;

import com.banzhiyan.core.concurrent.ExecutorUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class SystemTimer {
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final long tickUnit = Long.parseLong(System.getProperty("systemTimer.tick", "50"));
    private static volatile long timeMillis = System.currentTimeMillis();

    public SystemTimer() {
    }

    public static long currentTimeMillis() {
        return timeMillis;
    }

    static {
        EXECUTOR.scheduleAtFixedRate(new SystemTimer.TimerTicker(), tickUnit, tickUnit, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ExecutorUtil.gracefulShutdown(SystemTimer.EXECUTOR, 100);
            }
        });
    }

    private static class TimerTicker implements Runnable {
        private TimerTicker() {
        }

        public void run() {
            SystemTimer.timeMillis = System.currentTimeMillis();
        }
    }
}
