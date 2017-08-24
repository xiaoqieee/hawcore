package com.banzhiyan.datasource.config;

import com.banzhiyan.util.JsonUtils;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class C3P0Config extends Config {
    private int initialPoolSize;
    private int minPoolSize;
    private int maxPoolSize;
    private int acquireIncrement;
    private int acquireRetryAttempts;
    private int acquireRetryDelay;
    private int checkoutTimeout;
    private int numHelperThreads;
    private int maxStatementsPerConnection;
    private int propertyCycle;
    private int maxIdleTime;
    private int unreturnedConnectionTimeout;
    private int idleConnectionTestPeriod;
    private boolean breakAfterAcquireFailure;
    private boolean testConnectionOnCheckout;
    private boolean testConnectionOnCheckin;
    private boolean debugUnreturnedConnectionStackTraces;
    private String automaticTestTable;

    public C3P0Config() {
    }

    public int getInitialPoolSize() {
        return this.initialPoolSize;
    }

    public int getMinPoolSize() {
        return this.minPoolSize;
    }

    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public int getAcquireIncrement() {
        return this.acquireIncrement;
    }

    public int getAcquireRetryAttempts() {
        return this.acquireRetryAttempts;
    }

    public int getAcquireRetryDelay() {
        return this.acquireRetryDelay;
    }

    public int getCheckoutTimeout() {
        return this.checkoutTimeout;
    }

    public int getNumHelperThreads() {
        return this.numHelperThreads;
    }

    public int getMaxStatementsPerConnection() {
        return this.maxStatementsPerConnection;
    }

    public int getPropertyCycle() {
        return this.propertyCycle;
    }

    public int getMaxIdleTime() {
        return this.maxIdleTime;
    }

    public int getUnreturnedConnectionTimeout() {
        return this.unreturnedConnectionTimeout;
    }

    public int getIdleConnectionTestPeriod() {
        return this.idleConnectionTestPeriod;
    }

    public boolean isBreakAfterAcquireFailure() {
        return this.breakAfterAcquireFailure;
    }

    public boolean isTestConnectionOnCheckout() {
        return this.testConnectionOnCheckout;
    }

    public boolean isTestConnectionOnCheckin() {
        return this.testConnectionOnCheckin;
    }

    public boolean isDebugUnreturnedConnectionStackTraces() {
        return this.debugUnreturnedConnectionStackTraces;
    }

    public String getAutomaticTestTable() {
        return this.automaticTestTable;
    }

    public static C3P0Config builder() {
        return (C3P0Config) JsonUtils.parseJSON(get("C3P0"), C3P0Config.class);
    }
}

