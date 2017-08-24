package com.banzhiyan.redis.jedis.spring;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;
import com.banzhiyan.redis.pool.PoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.PassThroughExceptionTranslationStrategy;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConverters;
import org.springframework.data.redis.connection.jedis.JedisSentinelConnection;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class JedisConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(JedisConnectionFactory.class);
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(JedisConverters.exceptionConverter());
    private JedisShardInfo shardInfo;
    private String hostName;
    private int port;
    private final int timeout;
    private String password;
    private final boolean usePool;
    private Pool<Jedis> pool;
    private PoolConfig poolConfig;
    private int dbIndex;
    private boolean convertPipelineAndTxResults;
    private RedisSentinelConfiguration sentinelConfig;
    private static final int MAXIMUM_RETRIES = 50;

    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig) {
        this.hostName = "localhost";
        this.port = 6379;
        this.timeout = 15000;
        this.usePool = true;
        this.poolConfig = new PoolConfig();
        this.dbIndex = 0;
        this.convertPipelineAndTxResults = true;
        this.sentinelConfig = sentinelConfig;
        this.poolConfig = this.buildPoolConfig();
    }

    private PoolConfig buildPoolConfig() {
        PoolConfig config = new PoolConfig();
        config.setMaxTotal(2048);
        config.setMaxIdle(64);
        return config;
    }

    /** @deprecated */
    @Deprecated
    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, PoolConfig poolConfig, String c) {
        this(sentinelConfig);
    }

    protected Jedis fetchJedisConnector() {
        try {
            if(this.pool != null) {
                return this.getResource();
            } else {
                Jedis jedis = new Jedis(this.getShardInfo());
                jedis.connect();
                return jedis;
            }
        } catch (Exception var2) {
            throw new RedisConnectionFailureException("Cannot get Jedis connection", var2);
        }
    }

    private Jedis getResource() {
        int i = 0;

        while(i < 50) {
            try {
                return (Jedis)this.pool.getResource();
            } catch (JedisConnectionException var3) {
                this.silentSleeping();
                ++i;
            }
        }

        throw new JedisConnectionException("Could not get a resource from the pool.");
    }

    private void silentSleeping() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException var2) {
            ;
        }

    }

    private JedisConnection postProcessConnection(JedisConnection connection) {
        if(logger.isDebugEnabled()) {
            Client client = connection.getNativeConnection().getClient();
            logger.debug("Opened connection to Redis server {}:{}#{}", new Object[]{client.getHost(), Integer.valueOf(client.getPort()), client.getDB()});
        }

        return connection;
    }

    public void afterPropertiesSet() {
        if(this.shardInfo == null) {
            this.shardInfo = new JedisShardInfo(this.hostName, this.port);
            if(StringUtils.hasLength(this.password)) {
                this.shardInfo.setPassword(this.password);
            }

            this.shardInfo.setSoTimeout(15000);
        }

        this.pool = this.createPool();
    }

    private Pool<Jedis> createPool() {
        return this.isRedisSentinelAware()?this.createRedisSentinelPool(this.sentinelConfig):this.createRedisPool();
    }

    private Pool<Jedis> createRedisSentinelPool(RedisSentinelConfiguration config) {
        return new JedisSentinelPool(config.getMaster().getName(), this.convertToJedisSentinelSet(config.getSentinels()), this.getPoolConfig() != null?this.getPoolConfig():new PoolConfig(), this.getTimeoutFrom(this.getShardInfo()), this.getShardInfo().getPassword(), this.getDatabase());
    }

    private Pool<Jedis> createRedisPool() {
        return new JedisPool(this.getPoolConfig(), this.getShardInfo().getHost(), this.getShardInfo().getPort(), this.getTimeoutFrom(this.getShardInfo()), this.getShardInfo().getPassword(), this.getDatabase());
    }

    public void destroy() {
        if(this.pool != null) {
            try {
                this.pool.destroy();
            } catch (Exception var2) {
                logger.warn("Cannot properly close Jedis pool", var2);
            }

            this.pool = null;
        }

    }

    public JedisConnection getConnection() {
        Jedis jedis = this.fetchJedisConnector();
        JedisConnection connection = new JedisConnectionWrapper(jedis, this.pool, this.getDatabase());
        connection.setConvertPipelineAndTxResults(this.convertPipelineAndTxResults);
        return this.postProcessConnection(connection);
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return null;
    }

    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return EXCEPTION_TRANSLATION.translate(ex);
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public JedisShardInfo getShardInfo() {
        return this.shardInfo;
    }

    public void setShardInfo(JedisShardInfo shardInfo) {
        this.shardInfo = shardInfo;
    }

    public int getTimeout() {
        return 15000;
    }

    public void setTimeout(int timeout) {
    }

    public boolean getUsePool() {
        return true;
    }

    public PoolConfig getPoolConfig() {
        return this.poolConfig;
    }

    public void setPoolConfig(PoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getDatabase() {
        return this.dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    public boolean getConvertPipelineAndTxResults() {
        return this.convertPipelineAndTxResults;
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    public boolean isRedisSentinelAware() {
        return this.sentinelConfig != null;
    }

    public RedisSentinelConnection getSentinelConnection() {
        if(!this.isRedisSentinelAware()) {
            throw new InvalidDataAccessResourceUsageException("No Sentinels configured");
        } else {
            return new JedisSentinelConnection(this.getActiveSentinel());
        }
    }

    private Jedis getActiveSentinel() {
        Assert.notNull(this.sentinelConfig);
        Iterator i$ = this.sentinelConfig.getSentinels().iterator();

        Jedis jedis;
        do {
            if(!i$.hasNext()) {
                throw new InvalidDataAccessResourceUsageException("no sentinel found");
            }

            RedisNode node = (RedisNode)i$.next();
            jedis = new Jedis(node.getHost(), node.getPort().intValue());
        } while(!jedis.ping().equalsIgnoreCase("pong"));

        return jedis;
    }

    private Set<String> convertToJedisSentinelSet(Collection<RedisNode> nodes) {
        if(CollectionUtils.isEmpty(nodes)) {
            return Collections.emptySet();
        } else {
            Set<String> convertedNodes = new LinkedHashSet(nodes.size());
            Iterator i$ = nodes.iterator();

            while(i$.hasNext()) {
                RedisNode node = (RedisNode)i$.next();
                if(node != null) {
                    convertedNodes.add(node.asString());
                }
            }

            return convertedNodes;
        }
    }

    private int getTimeoutFrom(JedisShardInfo shardInfo) {
        return shardInfo.getSoTimeout();
    }

    public void setUsePool(boolean usePool) {
    }
}

