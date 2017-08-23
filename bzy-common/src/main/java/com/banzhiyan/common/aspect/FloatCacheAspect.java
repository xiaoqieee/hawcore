package com.banzhiyan.common.aspect;

import com.neo.xnol.pcts.common.annotation.FloatCache;
import com.neo.xnol.pcts.common.utils.AopUtils;
import ooh.bravo.cache.CacheException;
import ooh.bravo.core.concurrent.TaskManager;
import ooh.bravo.logging.Logger;
import ooh.bravo.logging.LoggerFactory;
import ooh.bravo.redis.spring.ObjectRedisTemplate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/6/17.
 */
@Aspect
@Component
public class FloatCacheAspect implements Ordered {

    Logger logger = LoggerFactory.getLogger(FloatCacheAspect.class);


    @Autowired
    @Qualifier("objectRedisTemplate")
    public ObjectRedisTemplate<Object> redisTemplate;

    @Pointcut("@annotation(com.neo.xnol.pcts.common.annotation.FloatCache)")
    public void cache() {
    }

    @Around(value = "cache()")
    public void around(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Method currentMethod = AopUtils.currentMethod(joinPoint);
        FloatCache floatCache = currentMethod.getAnnotation(FloatCache.class);
        long period = floatCache.period();
        int floatTime = floatCache.floatTime();

        Thread t = new Thread();
        t.run();
//        t.isAlive()
    }


    private Object getResultFromCacheSafe(ProceedingJoinPoint jp, Method currentMethod, String cacheName, String cacheKey) throws Exception {
        Object result = getResultFromCache(jp, currentMethod, cacheName, cacheKey);
        if (null != result) {
            return result;
        }
        return null;
    }

    private Object getResultFromCache(ProceedingJoinPoint jp, Method currentMethod, String cacheName, String cacheKey) throws Exception {
        cacheName = AopUtils.handleCacheName(jp, currentMethod, cacheName);
        String hashKey = AopUtils.handleHashKey(jp, currentMethod, cacheKey);
        try {
            return redisTemplate.opsForHash().get(cacheName, hashKey);
        } catch (Throwable var7) {
            throw new CacheException(var7);
        }
    }

    private void setResultIntoCache(final ProceedingJoinPoint jp, final Method currentMethod, final String cachName, final String cacheKey, final long timeOut, final TimeUnit timeUnit, final Object result) {
        TaskManager taskManager = TaskManager.get("Request-Exec-Limiter-Redis-Cache");
        taskManager.execute(new Runnable() {
            @Override
            public void run() {
                String cacheName = AopUtils.handleCacheName(jp, currentMethod, cachName);
                String hashKey = AopUtils.handleHashKey(jp, currentMethod, cacheKey);
                try {
                    redisTemplate.opsForHash().put(cacheName, hashKey, result);
                    redisTemplate.expire(cacheName, timeOut, timeUnit);

                } catch (Throwable t) {
                    throw new CacheException(t);
                }
            }
        });
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
