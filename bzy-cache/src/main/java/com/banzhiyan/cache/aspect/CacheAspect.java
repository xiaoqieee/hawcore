package com.banzhiyan.cache.aspect;

import com.banzhiyan.cache.CacheFacade;
import com.banzhiyan.cache.annotation.CacheEvict;
import com.banzhiyan.cache.annotation.CacheFlush;
import com.banzhiyan.cache.annotation.Cacheable;
import com.banzhiyan.cache.constant.CacheType;
import com.banzhiyan.cache.exception.CacheException;
import com.banzhiyan.core.support.ParameterNameHelper;
import com.banzhiyan.security.crypto.Digester;
import com.banzhiyan.util.FstUtils;
import com.banzhiyan.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.Ordered;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by xn025665 on 2017/8/24.
 */

@Aspect
class CacheAspect implements Ordered {
    private CacheFacade ehcache;
    private CacheFacade memoryCache;
    private CacheFacade redisCache;
    private static final ThreadLocal<Digester> DIGESTER = new ThreadLocal<Digester>() {
        protected Digester initialValue() {
            return new Digester("SHA-1", 1);
        }
    };
    private final ExpressionParser parser = new SpelExpressionParser();

    CacheAspect() {
    }

    @Around("ooh.bravo.cache.pointcut.CachePointcuts.cacheable()")
    public Object cacheable(ProceedingJoinPoint jp) {
        Method currentMethod = this.currentMethod(jp);
        Cacheable cacheable = (Cacheable)currentMethod.getAnnotation(Cacheable.class);
        String key = this.handleCacheKey(jp, currentMethod, cacheable);
        CacheFacade cf = this.selectCache(cacheable.cacheType());

        try {
            return cf.get(cacheable.cacheName(), key, cacheable.timeout(), cacheable.timeUnit(), jp);
        } catch (Throwable var7) {
            throw new CacheException(var7);
        }
    }

    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature)jp.getSignature()).getMethod();
    }

    private String handleCacheKey(ProceedingJoinPoint jp, Method currentMethod, Cacheable cacheable) {
        String key = cacheable.key();
        if(StringUtils.isBlank(key)) {
            key = this.generateKey(jp.getTarget().getClass().getName(), currentMethod.getName(), jp.getArgs());
        } else {
            key = this.parseSpelKeyIfNeeded(key, currentMethod, jp.getArgs());
        }

        return key;
    }

    private CacheFacade selectCache(CacheType cacheType) {
        switch(cacheType.ordinal()) {
            case 1:
                return this.ehcache;
            case 2:
                return this.memoryCache;
            case 3:
            default:
                return this.redisCache;
        }
    }

    private String generateKey(String className, String methodName, Object... arguments) {
        byte[] bytes = ArrayUtils.EMPTY_BYTE_ARRAY;
        bytes = ArrayUtils.addAll(bytes, StringUtils.getBytesUtf8(className));
        bytes = ArrayUtils.addAll(bytes, StringUtils.getBytesUtf8(methodName));
        if(arguments != null && arguments.length > 0) {
            Object[] arr = arguments;
            int len = arguments.length;

            for(int i = 0; i < len; ++i) {
                Object argument = arr[i];
                if(argument != null) {
                    bytes = ArrayUtils.addAll(bytes, FstUtils.serialize(argument));
                }
            }
        }

        return ((Digester)DIGESTER.get()).reset().digestHex(bytes);
    }

    @After("ooh.bravo.cache.pointcut.CachePointcuts.cacheEvict()")
    public void cacheEvict(JoinPoint jp) {
        Method currentMethod = this.currentMethod(jp);
        CacheEvict cacheEvict = (CacheEvict)currentMethod.getAnnotation(CacheEvict.class);
        String key = this.parseSpelKeyIfNeeded(cacheEvict.key(), currentMethod, jp.getArgs());
        CacheFacade cf = this.selectCache(cacheEvict.cacheType());
        cf.remove(cacheEvict.cacheName(), key, cacheEvict.isPrefix());
    }

    @After("ooh.bravo.cache.pointcut.CachePointcuts.cacheFlush()")
    public void cacheFlush(JoinPoint jp) {
        Method currentMethod = this.currentMethod(jp);
        CacheFlush cacheFlush = (CacheFlush)currentMethod.getAnnotation(CacheFlush.class);
        CacheFacade cf = this.selectCache(cacheFlush.cacheType());
        cf.removeAll(cacheFlush.cacheName());
    }

    private String parseSpelKeyIfNeeded(String key, Method method, Object[] args) {
        String[] parameterNames = ParameterNameHelper.getDiscoverer().getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        int i = 0;

        for(int length = parameterNames.length; i < length; ++i) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return (String)this.parser.parseExpression(key).getValue(context, String.class);
    }

    public int getOrder() {
        return 80;
    }

    public void setMemoryCache(CacheFacade memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void setEhcache(CacheFacade ehcache) {
        this.ehcache = ehcache;
    }

    public void setRedisCache(CacheFacade redisCache) {
        this.redisCache = redisCache;
    }
}

