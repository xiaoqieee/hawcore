package com.banzhiyan.redis.spring;

import com.banzhiyan.redis.serializer.CustomSerializationRedisSerializer;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
/**
 * Created by xn025665 on 2017/8/24.
 */
public class ObjectRedisTemplate<T> extends RedisTemplate<String, T> {
    public ObjectRedisTemplate() {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<?> objectSerializer = new CustomSerializationRedisSerializer();
        this.setKeySerializer(stringSerializer);
        this.setValueSerializer(objectSerializer);
        this.setHashKeySerializer(stringSerializer);
        this.setHashValueSerializer(objectSerializer);
    }

    public ObjectRedisTemplate(RedisConnectionFactory connectionFactory) {
        this();
        this.setConnectionFactory(connectionFactory);
        this.afterPropertiesSet();
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }
}
