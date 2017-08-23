package com.banzhiyan.core.concurrent;

import com.banzhiyan.core.config.ProcessorHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ThreadPoolExecutorFactoryBean implements FactoryBean<ExecutorService>, InitializingBean, DisposableBean {
    private int corePoolSize = 1;
    private int maxPoolSize = ProcessorHelper.number();
    private int keepAliveSeconds = 15;
    private boolean allowCoreThreadTimeOut = false;
    private int queueCapacity = 10000;
    private ExecutorService exposedExecutor;
    private String threadNamePrefix = "Pool-";

    public ThreadPoolExecutorFactoryBean() {
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    private void initializeExecutor() {
        BlockingQueue<Runnable> queue = this.createQueue(this.queueCapacity);
        ThreadPoolExecutor executor = this.createExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, queue, new NamedThreadFactory(this.threadNamePrefix, true), new CustomRejectedExecutionHandler());
        if(this.allowCoreThreadTimeOut) {
            executor.allowCoreThreadTimeOut(true);
        }

        this.exposedExecutor = executor;
    }

    private ThreadPoolExecutor createExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, (long)keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
    }

    private BlockingQueue<Runnable> createQueue(int queueCapacity) {
        return (BlockingQueue)(queueCapacity > 0?new LinkedBlockingQueue(queueCapacity):new SynchronousQueue());
    }

    public ExecutorService getObject() {
        return this.exposedExecutor;
    }

    public Class<? extends ExecutorService> getObjectType() {
        return ExecutorService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        this.initializeExecutor();
    }

    public void destroy() throws Exception {
        ExecutorUtil.gracefulShutdown(this.exposedExecutor, 500);
    }
}

