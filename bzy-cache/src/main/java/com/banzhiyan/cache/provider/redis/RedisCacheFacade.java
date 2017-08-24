package com.banzhiyan.cache.provider.redis;

import com.banzhiyan.cache.CacheFacade;
import com.banzhiyan.cache.exception.CacheException;
import com.banzhiyan.cache.provider.AbstractBaseCache;
import com.banzhiyan.redis.serializer.CustomSerializationRedisSerializer;
import com.banzhiyan.redis.spring.ObjectRedisTemplate;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.util.SafeEncoder;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class RedisCacheFacade extends AbstractBaseCache implements CacheFacade {
    private final ObjectRedisTemplate<Object> redisTemplate;
    private final RedisSerializer<Object> serializer;

    public RedisCacheFacade(ObjectRedisTemplate<Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.serializer = new CustomSerializationRedisSerializer();
    }

    protected Object fromCache(final String cacheName, final String key) {
        return this.redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                List<byte[]> values = connection.hMGet(RedisCacheFacade.this.keyBytes(cacheName), new byte[][]{RedisCacheFacade.this.keyBytes(key), RedisCacheFacade.this.keyBytes(RedisCacheFacade.this.timeoutKey(key))});
                Object result = null;
                int size;
                if(values != null && (size = values.size()) > 0) {
                    byte[] object = (byte[])values.get(0);
                    if(object != null) {
                        byte[] timeout;
                        if(size > 1 && (timeout = (byte[])values.get(1)) != null) {
                            long timestamp = ((Long)RedisCacheFacade.this.serializer.deserialize(timeout)).longValue();
                            if(timestamp > System.currentTimeMillis()) {
                                result = RedisCacheFacade.this.serializer.deserialize(object);
                            }
                        } else {
                            result = RedisCacheFacade.this.serializer.deserialize(object);
                        }
                    }
                }

                return result;
            }
        });
    }

    protected Callable<Object> getSet(final String cacheName, final String key, final long timeout, final TimeUnit timeUnit, final ProceedingJoinPoint jp) {
        Callable<Object> callable = new Callable<Object>() {
            public Object call() throws Exception {
                try {
                    final Object object = jp.proceed();
                    if(object != null) {
                        RedisCacheFacade.this.redisTemplate.execute(new RedisCallback<Void>() {
                            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                                Map<byte[], byte[]> values = new HashMap(2);
                                values.put(RedisCacheFacade.this.keyBytes(key), RedisCacheFacade.this.serializer.serialize(object));
                                String timeoutKey = RedisCacheFacade.this.timeoutKey(key);
                                if(timeout > 0L) {
                                    values.put(RedisCacheFacade.this.keyBytes(timeoutKey), RedisCacheFacade.this.serializer.serialize(Long.valueOf(System.currentTimeMillis() + timeUnit.toMillis(timeout))));
                                } else {
                                    values.put(RedisCacheFacade.this.keyBytes(timeoutKey), RedisCacheFacade.this.serializer.serialize(Long.valueOf(9223372036854775807L)));
                                }

                                connection.hMSet(RedisCacheFacade.this.keyBytes(cacheName), values);
                                return null;
                            }
                        });
                    }

                    return object;
                } catch (Throwable var2) {
                    throw new CacheException(var2);
                }
            }
        };
        return callable;
    }

    public void remove(final String cacheName, final String key, final boolean isPrefix) {
        if(StringUtils.isNotBlank(key)) {
            this.redisTemplate.execute(new RedisCallback<Void>() {
                public Void doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] cacheNameBytes = RedisCacheFacade.this.keyBytes(cacheName);
                    if(isPrefix) {
                        Set<byte[]> hashKeys = connection.hKeys(cacheNameBytes);
                        if(hashKeys != null) {
                            List<byte[]> fields = new LinkedList();
                            Iterator i$ = hashKeys.iterator();

                            while(i$.hasNext()) {
                                byte[] hashKey = (byte[])i$.next();
                                if(SafeEncoder.encode(hashKey).startsWith(key)) {
                                    fields.add(hashKey);
                                }
                            }

                            if(fields.size() > 0) {
                                connection.hDel(cacheNameBytes, (byte[][])fields.toArray(new byte[fields.size()][]));
                            }
                        }
                    } else {
                        connection.hDel(cacheNameBytes, new byte[][]{RedisCacheFacade.this.keyBytes(key)});
                    }

                    return null;
                }
            });
        }

    }

    public void removeAll(final String cacheName) {
        this.redisTemplate.execute(new RedisCallback<Void>() {
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(new byte[][]{RedisCacheFacade.this.keyBytes(cacheName)});
                return null;
            }
        });
    }

    private String timeoutKey(String key) {
        return key + "-timeout";
    }

    private byte[] keyBytes(String key) {
        return SafeEncoder.encode(key);
    }
}

