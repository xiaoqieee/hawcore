package com.banzhiyan.common.aspect;

import com.neo.xnol.pcts.common.annotation.RequestExecLimiter;
import com.neo.xnol.pcts.common.constant.SystemConstants;
import com.neo.xnol.pcts.common.memcache.LocalMemCache;
import ooh.bravo.cache.CacheException;
import ooh.bravo.cache.CacheType;
import ooh.bravo.core.concurrent.TaskManager;
import ooh.bravo.core.support.ParameterNameHelper;
import ooh.bravo.logging.Logger;
import ooh.bravo.logging.LoggerFactory;
import ooh.bravo.redis.spring.ObjectRedisTemplate;
import ooh.bravo.security.crypto.Digester;
import ooh.bravo.util.FstUtils;
import ooh.bravo.util.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by xn025665 on 2017/6/19.
 */
@Aspect
@Component
public class RequestExecLimiterAspect implements Ordered {

    private static String FIELD_LOGGER_NAME = "logger";

    private static final HTreeMap<String, Semaphore> THROTTLES = throttles();

    private static final Logger localLogger = LoggerFactory.getLogger(RequestExecLimiterAspect.class);

    @Autowired
    @Qualifier("objectRedisTemplate")
    public ObjectRedisTemplate<Object> redisTemplate;

    private static final ThreadLocal<Digester> DIGESTER = new ThreadLocal<Digester>() {
        protected Digester initialValue() {
            return new Digester("SHA-1", 1);
        }
    };
    private final ExpressionParser parser = new SpelExpressionParser();

    public RequestExecLimiterAspect() {
    }

    private static HTreeMap<String, Semaphore> throttles() {
        DB db = DBMaker.newMemoryDB().cacheLRUEnable().cacheSize(256).transactionDisable().make();
        return db.createHashMap("Request-Exec-Limiter").expireMaxSize(256L).make();
    }

    @Pointcut("@annotation(com.neo.xnol.pcts.common.annotation.RequestExecLimiter)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object handle(ProceedingJoinPoint jp) throws Throwable {
        Method currentMethod = this.currentMethod(jp);
        RequestExecLimiter requestExecLimiter = (RequestExecLimiter)currentMethod.getAnnotation(RequestExecLimiter.class);
        Semaphore semaphore = this.getOrCreateQueue(jp, requestExecLimiter);
        Logger logger = getLogger(jp);
        if(!requestExecLimiter.failfast() && !requestExecLimiter.failsafe()) {
            semaphore.acquire();
        } else {
            boolean acquired = semaphore.tryAcquire((long)requestExecLimiter.concurrentTimeout(), TimeUnit.SECONDS);
            if(!acquired) {
                if(requestExecLimiter.failsafe()) {
                    Object result = getResultFromCacheSafe(jp, currentMethod, requestExecLimiter);
                    // 从缓存中取值
                    logger.info("Result is from cache ：{}", JsonUtils.toJSON(result));
                    return result;
                }
                throw new RuntimeException("failfast to concurrent request limit " + requestExecLimiter.concurrency());
            }
        }
        Object result;
        try {
            result = jp.proceed();
            if(null != result){
                // 保存至缓存
                setResultIntoCache(jp, currentMethod, requestExecLimiter, result);
                logger.info("Result is cached ：{}", JsonUtils.toJSON(result));
            }
        } finally {
            semaphore.release();
        }
        return result;
    }

    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature)jp.getSignature()).getMethod();
    }

    private Semaphore getOrCreateQueue(ProceedingJoinPoint jp, RequestExecLimiter requestExecLimiter) {
        int concurrency = requestExecLimiter.concurrency();
        Assert.isTrue(concurrency > 0);
        MethodSignature signature = (MethodSignature)jp.getSignature();
        String identifier = signature.toLongString();
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

    private Logger getLogger(ProceedingJoinPoint joinPoint) throws Throwable{
        Class<?> rootClazz = joinPoint.getTarget().getClass();
        Logger logger = getLogger(joinPoint,rootClazz, rootClazz);
        if(logger == null){
            logger = localLogger;
        }
        return logger;
    }

    private Logger getLogger(ProceedingJoinPoint joinPoint, Class<?> clazz, Class<?> rootClazz) throws Exception{
        if(clazz.getName().equals("java.lang.Object")){
            return null;
        }
        if(hasLoggerField(clazz)){
            Field f = clazz.getDeclaredField("logger");
            f.setAccessible(true);
            return (Logger)f.get(joinPoint.getTarget());
        }else{
            return getLogger(joinPoint, clazz.getSuperclass(), rootClazz);
        }
    }

    private boolean hasLoggerField(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        if(null != fields){
            for(int i=0,len = fields.length;i<len;++i){
                if(FIELD_LOGGER_NAME.equals(fields[i].getName())){
                    return true;
                }
            }
        }
        return false;
    }
    
    private Object getResultFromCacheSafe(ProceedingJoinPoint jp, Method currentMethod, RequestExecLimiter requestExecLimiter) throws Exception{
        int i=0, tryTimes=requestExecLimiter.tryTimes();
        while(i++ < tryTimes){
            Object result = getResultFromCache(jp, currentMethod, requestExecLimiter);
            if(null != result){
                return result;
            }
            TimeUnit.MILLISECONDS.sleep(50);
        }
        return null;
    }
    
    private Object getResultFromCache(ProceedingJoinPoint jp, Method currentMethod, RequestExecLimiter requestExecLimiter) throws Exception{
        String cacheName = handleCacheName(jp, currentMethod, requestExecLimiter);
        String hashKey = handleHashKey(jp,currentMethod,requestExecLimiter);
        CacheType cacheType = requestExecLimiter.cacheType();
        try {
            if(CacheType.REDIS.equals(cacheType)){
                return redisTemplate.opsForHash().get(cacheName,hashKey);
            }else if(CacheType.MEMORY.equals(cacheType)){
                return LocalMemCache.get(cacheName + "_" + hashKey);
            }
            throw new RuntimeException("cacheType is not supported:" + cacheType);
        } catch (Throwable var7) {
            throw new CacheException(var7);
        }
    }

    private void setResultIntoCache(final ProceedingJoinPoint jp, final Method currentMethod, final RequestExecLimiter requestExecLimiter, final Object result) {
        TaskManager taskManager = TaskManager.get("Request-Exec-Limiter-Redis-Cache");
        taskManager.execute(new Runnable() {
            @Override
            public void run() {
                String cacheName = handleCacheName(jp,currentMethod,requestExecLimiter);
                String hashKey = handleHashKey(jp,currentMethod,requestExecLimiter);
                CacheType cacheType = requestExecLimiter.cacheType();
                try {
                    if(CacheType.REDIS.equals(cacheType)){
                        redisTemplate.opsForHash().put(cacheName,hashKey,result);
                        long cacheTimeout = requestExecLimiter.cacheTimeout();
                        TimeUnit timeUnit = requestExecLimiter.cacheTimeUnit();
                        redisTemplate.expire(cacheName, cacheTimeout,  timeUnit);
                    }else if(CacheType.MEMORY.equals(cacheType)){
                        LocalMemCache.put(cacheName + "_" + hashKey, result);
                    }else{
                        throw new RuntimeException("cacheType is not supported:" + cacheType);
                    }
                } catch (Throwable t) {
                    throw new CacheException(t);
                }
            }
        });
    }

    private String handleCacheName(ProceedingJoinPoint jp, Method currentMethod,RequestExecLimiter requestExecLimiter){
        String cacheName = requestExecLimiter.cacheName();
        if(ooh.bravo.util.StringUtils.isNotBlank(cacheName)){
            return cacheName;
        }
        String className = jp.getTarget().getClass().getName();
        return SystemConstants.SYSTEM_NAME + "-" + className + "-" + currentMethod.getName();
    }


    private String handleHashKey(ProceedingJoinPoint jp, Method currentMethod, RequestExecLimiter requestExecLimiter) {
        String key = requestExecLimiter.cacheKey();
        if(StringUtils.isBlank(key)) {
            key = this.generateHashKey(jp.getArgs());
        } else {
            key = this.parseSpelKeyIfNeeded(key, currentMethod, jp.getArgs());
        }
        return key;
    }

    private String generateHashKey(Object... arguments) {
        byte[] bytes = ArrayUtils.EMPTY_BYTE_ARRAY;
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
        return 100;
    }
}
