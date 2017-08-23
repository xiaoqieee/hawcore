package com.banzhiyan.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class DelayThreadManager {

    private static final int CORE = Runtime.getRuntime().availableProcessors();


    public static final ScheduledExecutorService delayThread = Executors.newScheduledThreadPool(CORE);

    /**
     * 添加延迟任务
     *
     * @param task
     */
    public static void addTask(Runnable task, int delay, TimeUnit timeUnit) {
        delayThread.schedule(task, delay, timeUnit);
    }

    /**
     * 添加延迟任务(毫秒)
     *
     * @param task
     * @param delay
     */
    public static void addTask(Runnable task, int delay) {
        addTask(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 添加延迟任务(30秒)
     *
     * @param task
     */
    public static void addTask(Runnable task) {
        addTask(task, 30, TimeUnit.SECONDS);
    }
}
