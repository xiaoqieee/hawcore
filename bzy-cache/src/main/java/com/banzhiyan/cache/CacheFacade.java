package com.banzhiyan.cache;

import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by xn025665 on 2017/8/24.
 */
public interface CacheFacade {
    Object get(String cacheName, String cacheKey, long expire, TimeUnit timeUnit, ProceedingJoinPoint joinPoint);

    void remove(String expire, String cacheKey, boolean isPrefix);

    void removeAll(String cacheName);
}
