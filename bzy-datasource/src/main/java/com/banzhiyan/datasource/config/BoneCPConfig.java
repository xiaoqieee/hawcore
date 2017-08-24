package com.banzhiyan.datasource.config;

import com.banzhiyan.util.JsonUtils;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class BoneCPConfig extends Config {
    private int partitionCount;
    private int poolAvailabilityThreshold;
    private int minConnectionsPerPartition;
    private int maxConnectionsPerPartition;
    private int acquireIncrement;
    private int acquireRetryAttempts;
    private int statementsCacheSize;
    private long acquireRetryDelayInMs;
    private long connectionTimeoutInMs;
    private long queryExecuteTimeLimitInMs;
    private long idleConnectionTestPeriodInMinutes;
    private long idleMaxAgeInMinutes;
    private boolean nullOnConnectionTimeout;
    private boolean lazyInit;
    private boolean closeConnectionWatch;
    private boolean disableJMX;
    private boolean disableConnectionTracking;
    private String defaultTransactionIsolation;
    private String poolName;
    private String connectionTestStatement;

    public BoneCPConfig() {
    }

    public int getPartitionCount() {
        return this.partitionCount;
    }

    public int getPoolAvailabilityThreshold() {
        return this.poolAvailabilityThreshold;
    }

    public int getMinConnectionsPerPartition() {
        return this.minConnectionsPerPartition;
    }

    public int getMaxConnectionsPerPartition() {
        return this.maxConnectionsPerPartition;
    }

    public int getAcquireIncrement() {
        return this.acquireIncrement;
    }

    public int getAcquireRetryAttempts() {
        return this.acquireRetryAttempts;
    }

    public int getStatementsCacheSize() {
        return this.statementsCacheSize;
    }

    public long getAcquireRetryDelayInMs() {
        return this.acquireRetryDelayInMs;
    }

    public long getConnectionTimeoutInMs() {
        return this.connectionTimeoutInMs;
    }

    public long getQueryExecuteTimeLimitInMs() {
        return this.queryExecuteTimeLimitInMs;
    }

    public long getIdleConnectionTestPeriodInMinutes() {
        return this.idleConnectionTestPeriodInMinutes;
    }

    public long getIdleMaxAgeInMinutes() {
        return this.idleMaxAgeInMinutes;
    }

    public boolean isNullOnConnectionTimeout() {
        return this.nullOnConnectionTimeout;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public boolean isCloseConnectionWatch() {
        return this.closeConnectionWatch;
    }

    public boolean isDisableJMX() {
        return this.disableJMX;
    }

    public boolean isDisableConnectionTracking() {
        return this.disableConnectionTracking;
    }

    public String getDefaultTransactionIsolation() {
        return this.defaultTransactionIsolation;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public String getConnectionTestStatement() {
        return this.connectionTestStatement;
    }

    public static BoneCPConfig builder() {
        return (BoneCPConfig) JsonUtils.parseJSON(get("BoneCP"), BoneCPConfig.class);
    }
}

