package com.banzhiyan.cache.provider;

import com.banzhiyan.cache.CacheFacade;
import com.banzhiyan.cache.exception.CacheException;
import com.banzhiyan.core.concurrent.TaskManager;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/24.
 */

public abstract class AbstractBaseCache implements CacheFacade {
    public AbstractBaseCache() {
    }

    public Object get(String cacheName, String key, long timeout, TimeUnit timeUnit, ProceedingJoinPoint jp) {
        Object result = this.fromCache(cacheName, key);
        if(result == null) {
            result = this.fromMethod(cacheName, key, timeout, timeUnit, jp);
        }

        return result;
    }

    private Object fromMethod(String cache, String key, long timeout, TimeUnit timeUnit, ProceedingJoinPoint jp) {
        Future ft = TaskManager.get("Redis-Data-Caching").submit(this.getSet(cache, key, timeout, timeUnit, jp));

        try {
            return ft.get();
        } catch (Throwable var9) {
            throw new CacheException(var9);
        }
    }

    protected abstract Object fromCache(String var1, String var2);

    protected abstract Callable<Object> getSet(String var1, String var2, long var3, TimeUnit var5, ProceedingJoinPoint var6);
}

