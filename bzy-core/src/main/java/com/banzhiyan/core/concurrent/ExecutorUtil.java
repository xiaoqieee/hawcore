package com.banzhiyan.core.concurrent;

import java.util.concurrent.*;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ExecutorUtil {
    private static final ThreadPoolExecutor shutdownExecutor;

    public ExecutorUtil() {
    }

    public static boolean isShutdown(Executor executor) {
        return executor instanceof ExecutorService && ((ExecutorService)executor).isShutdown();
    }

    public static void gracefulShutdown(Executor executor, int timeout) {
        if(executor instanceof ExecutorService && !isShutdown(executor)) {
            ExecutorService es = (ExecutorService)executor;

            try {
                es.shutdown();
            } catch (SecurityException var5) {
                return;
            } catch (NullPointerException var6) {
                return;
            }

            try {
                if(!es.awaitTermination((long)timeout, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException var4) {
                es.shutdownNow();
                Thread.currentThread().interrupt();
            }

            if(!isShutdown(es)) {
                newThreadToCloseExecutor(es);
            }

        }
    }

    public static void shutdownNow(Executor executor, int timeout) {
        if(executor instanceof ExecutorService && !isShutdown(executor)) {
            ExecutorService es = (ExecutorService)executor;

            try {
                es.shutdownNow();
            } catch (SecurityException var5) {
                return;
            } catch (NullPointerException var6) {
                return;
            }

            try {
                es.awaitTermination((long)timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var4) {
                Thread.currentThread().interrupt();
            }

            if(!isShutdown(es)) {
                newThreadToCloseExecutor(es);
            }

        }
    }

    private static void newThreadToCloseExecutor(final ExecutorService es) {
        if(!isShutdown(es)) {
            shutdownExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        for(int i = 0; i < 1000; ++i) {
                            es.shutdownNow();
                            if(es.awaitTermination(10L, TimeUnit.MILLISECONDS)) {
                                break;
                            }
                        }
                    } catch (InterruptedException var2) {
                        Thread.currentThread().interrupt();
                    } catch (Throwable var3) {
                        ;
                    }

                }
            });
        }

    }

    static {
        shutdownExecutor = new ThreadPoolExecutor(0, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(100), new NamedThreadFactory("Close-ExecutorService-Timer", true));
    }
}