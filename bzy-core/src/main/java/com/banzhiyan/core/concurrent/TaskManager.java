package com.banzhiyan.core.concurrent;

import org.springframework.util.Assert;

import java.util.concurrent.*;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class TaskManager {
    private static final ConcurrentMap<String, TaskManager> HOLDER = new ConcurrentHashMap();
    private final ExecutorService ES;

    public static TaskManager get(String name) {
        Assert.notNull(name);
        TaskManager taskManager = (TaskManager)HOLDER.get(name);
        if(taskManager == null) {
            TaskManager tm = new TaskManager(name);
            taskManager = (TaskManager)HOLDER.putIfAbsent(name, tm);
            if(taskManager == null) {
                taskManager = tm;
            }
        }

        return taskManager;
    }

    private TaskManager(String name) {
        SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue();
        this.ES = new ThreadPoolExecutor(0, 2048, 15L, TimeUnit.SECONDS, synchronousQueue, new NamedThreadFactory(name));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ExecutorUtil.gracefulShutdown(TaskManager.this.ES, 500);
            }
        });
    }

    public void execute(Runnable command) {
        this.ES.execute(command);
    }

    public Future<?> submit(Runnable task) {
        return this.ES.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return this.ES.submit(task, result);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.ES.submit(task);
    }
}

