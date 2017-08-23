package com.banzhiyan.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolManager {

    private static final int CORE = Runtime.getRuntime().availableProcessors();
    
    // 线程池维护线程的最少数量,工作线程
    public static final int CORE_POOL_SIZE = CORE * 3;

    // 线程池维护线程的最大数量
    public static final int MAX_POOL_SIZE = CORE * 5;

    // 线程池维护线程所允许的空闲时间,单位由TimeUnit定义
    public static final int KEEP_ALIVE_TIME = 3;

    // 线程池所使用的缓冲队列大小
    public static final int WORK_QUEUE_SIZE = 2048;

    /** 
     * Param:  
     * 当池子大小小于corePoolSize就新建线程，并处理请求;当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去从workQueue中取任务并处理;
     * 当workQueue放不下新入的任务时，新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize就用RejectedExecutionHandler来做拒绝处理
     * 
     * corePoolSize - 池中所保存的线程数，包括空闲线程。 有点像工作线程 
     * maximumPoolSize - 池中允许的最大线程数(采用LinkedBlockingQueue时没有作用)。  
     * keepAliveTime -当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，线程池维护线程所允许的空闲时间。  
     * unit - keepAliveTime参数的时间单位，线程池维护线程所允许的空闲时间的单位:秒 。  
     * workQueue - 执行前用于保持任务的队列（缓冲队列）。此队列仅保持由execute 方法提交的 Runnable 任务。  ArrayBlockingQueue有界队列
     *             工作线程50个全在工作的时候，不会先加大工作线程到51，而是放到队列里面，队列满了再到51
     * RejectedExecutionHandler:AbortPolicy超过最大线程数，直接拒绝并且抛出异常
     *                          CallerRunsPolicy不想放弃执行任务。但是由于池中已经没有任何资源了，那么就直接使用调用改线程的线程run方法开启新线程
     */
    public static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 添加任务
     * @param task
     */
    public static void addTask(Runnable task) {
        threadPool.execute(task);
    }
}
