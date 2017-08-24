package com.banzhiyan.redis.jedis.spring;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.util.Set;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class JedisConnectionWrapper extends JedisConnection {
    public JedisConnectionWrapper(Jedis jedis, Pool<Jedis> pool, int dbIndex) {
        super(jedis, pool, dbIndex);
    }

    public JedisConnectionWrapper(Jedis jedis) {
        super(jedis);
    }

    public void flushDb() {
        throw new UnsupportedOperationException("'flushDB()' is not allowed.");
    }

    public void flushAll() {
        throw new UnsupportedOperationException("''flushAll()' is not allowed.'");
    }

    public Set<byte[]> keys(byte[] pattern) {
        throw new UnsupportedOperationException("''keys()' is not allowed.'");
    }
}
