package com.banzhiyan.tolerance.aspect;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.banzhiyan.tolerance.annotation.RequestThrottle;
import com.banzhiyan.tolerance.exception.RequestThrottleException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/**
 * Created by xn025665 on 2017/8/24.
 */

@Aspect
public class RequestThrottleAspect implements Ordered {
    private static final HTreeMap<String, Semaphore> THROTTLES = throttles();

    public RequestThrottleAspect() {
    }

    private static HTreeMap<String, Semaphore> throttles() {
        DB db = DBMaker.newMemoryDB().cacheLRUEnable().cacheSize(256).transactionDisable().make();
        return db.createHashMap("Request-Throttle-Aspect").expireMaxSize(256L).make();
    }

    @Around("execution(@ooh.bravo.tolerance.annotation.RequestThrottle * *(..))")
    public Object throttle(ProceedingJoinPoint jp) throws Throwable {
        Method currentMethod = this.currentMethod(jp);
        RequestThrottle requestThrottle = (RequestThrottle)currentMethod.getAnnotation(RequestThrottle.class);
        Semaphore semaphore = this.getOrCreateQueue(jp, requestThrottle);
        if(!requestThrottle.failfast() && !requestThrottle.failsafe()) {
            semaphore.acquire();
        } else {
            boolean acquired = semaphore.tryAcquire((long)requestThrottle.timeout(), TimeUnit.SECONDS);
            if(!acquired) {
                if(requestThrottle.failsafe()) {
                    return null;
                }

                throw new RequestThrottleException("[" + requestThrottle.subscription() + "] failfast to concurrent request limit " + requestThrottle.concurrency());
            }
        }

        Object var9;
        try {
            var9 = jp.proceed();
        } finally {
            semaphore.release();
        }

        return var9;
    }

    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature)jp.getSignature()).getMethod();
    }

    private Semaphore getOrCreateQueue(ProceedingJoinPoint jp, RequestThrottle requestThrottle) {
        int concurrency = requestThrottle.concurrency();
        Assert.isTrue(concurrency > 0);
        MethodSignature signature = (MethodSignature)jp.getSignature();
        String identifier = signature.toLongString() + "[" + requestThrottle.subscription() + "]";
        Semaphore semaphore = (Semaphore)THROTTLES.get(identifier);
        if(semaphore == null) {
            semaphore = new Semaphore(concurrency, true);
            Semaphore previous = (Semaphore)THROTTLES.putIfAbsent(identifier, semaphore);
            if(previous != null) {
                semaphore = previous;
            }
        }

        return semaphore;
    }

    public int getOrder() {
        return 50;
    }
}

