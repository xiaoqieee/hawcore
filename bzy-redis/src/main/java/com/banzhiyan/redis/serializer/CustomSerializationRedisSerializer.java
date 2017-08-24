package com.banzhiyan.redis.serializer;

import com.banzhiyan.util.KryoUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
/**
 * Created by xn025665 on 2017/8/24.
 */
public class CustomSerializationRedisSerializer implements RedisSerializer<Object> {
    private Converter<Object, byte[]> serializer = new SerializingConverter();
    private Converter<byte[], Object> deserializer = new DeserializingConverter();
    private static final byte[] EMPTY_ARRAY = new byte[0];

    public CustomSerializationRedisSerializer() {
    }

    private static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    public Object deserialize(byte[] bytes) {
        if(isEmpty(bytes)) {
            return null;
        } else {
            try {
                return KryoUtils.deserialize(bytes);
            } catch (Exception var5) {
                try {
                    return this.deserializer.convert(bytes);
                } catch (Exception var4) {
                    var4.printStackTrace();
                    return null;
                }
            }
        }
    }

    public byte[] serialize(Object object) {
        if(object == null) {
            return EMPTY_ARRAY;
        } else {
            try {
                return KryoUtils.serialize(object);
            } catch (Exception var5) {
                try {
                    return (byte[])this.serializer.convert(object);
                } catch (Exception var4) {
                    throw new SerializationException("Cannot serialize", var4);
                }
            }
        }
    }
}
