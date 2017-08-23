package com.banzhiyan.core.concurrent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
    public CustomRejectedExecutionHandler() {
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if(!executor.isShutdown()) {
            boolean offered = false;

            try {
                offered = executor.getQueue().offer(r, 30L, TimeUnit.SECONDS);
            } catch (InterruptedException var5) {
            }

            if(!offered) {
                r.run();
            }

        }
    }
}
