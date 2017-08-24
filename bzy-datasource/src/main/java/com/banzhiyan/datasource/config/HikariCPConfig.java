package com.banzhiyan.datasource.config;

import com.banzhiyan.util.JsonUtils;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class HikariCPConfig extends Config {
    private int connectionTimeout;
    private int validationTimeout;
    private int idleTimeout;
    private int maxLifetime;
    private int maxPoolSize;
    private int prepStmtCacheSqlLimit;
    private int prepStmtCacheSize;
    private boolean registerMbeans;
    private boolean initializationFailFast;
    private boolean readOnly;
    private boolean autoCommit;
    private boolean cachePrepStmts;
    private String transactionIsolationName;
    private String poolName;
    private String connectionTestQuery;

    public HikariCPConfig() {
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public int getValidationTimeout() {
        return this.validationTimeout;
    }

    public int getIdleTimeout() {
        return this.idleTimeout;
    }

    public int getMaxLifetime() {
        return this.maxLifetime;
    }

    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public boolean isRegisterMbeans() {
        return this.registerMbeans;
    }

    public boolean isInitializationFailFast() {
        return this.initializationFailFast;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public boolean isAutoCommit() {
        return this.autoCommit;
    }

    public String getTransactionIsolationName() {
        return this.transactionIsolationName;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public String getConnectionTestQuery() {
        return this.connectionTestQuery;
    }

    public int getPrepStmtCacheSqlLimit() {
        return this.prepStmtCacheSqlLimit;
    }

    public int getPrepStmtCacheSize() {
        return this.prepStmtCacheSize;
    }

    public boolean isCachePrepStmts() {
        return this.cachePrepStmts;
    }

    public static HikariCPConfig builder() {
        return (HikariCPConfig) JsonUtils.parseJSON(get("HikariCP"), HikariCPConfig.class);
    }
}
