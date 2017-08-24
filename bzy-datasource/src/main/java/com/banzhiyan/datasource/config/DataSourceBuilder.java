package com.banzhiyan.datasource.config;

import com.banzhiyan.datasource.BoneCPDataSource;
import com.banzhiyan.datasource.C3P0DataSource;
import com.banzhiyan.datasource.HikariDataSource;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class DataSourceBuilder {
    public DataSourceBuilder() {
    }

    public static HikariDataSource hikariCP() {
        HikariCPConfig config = HikariCPConfig.builder();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setAutoCommit(config.isAutoCommit());
        dataSource.setConnectionTestQuery(config.getConnectionTestQuery());
        dataSource.setConnectionTimeout((long)config.getConnectionTimeout());
        dataSource.setMaximumPoolSize(config.getMaxPoolSize());
        dataSource.setIdleTimeout((long)config.getIdleTimeout());
        dataSource.setInitializationFailFast(config.isInitializationFailFast());
        dataSource.setValidationTimeout((long)config.getValidationTimeout());
        dataSource.setReadOnly(config.isReadOnly());
        dataSource.setPoolName(config.getPoolName());
        dataSource.setMaxLifetime((long)config.getMaxLifetime());
        dataSource.setRegisterMbeans(config.isRegisterMbeans());
        dataSource.setTransactionIsolation(config.getTransactionIsolationName());
        dataSource.addDataSourceProperty("cachePrepStmts", Boolean.valueOf(config.isCachePrepStmts()));
        dataSource.addDataSourceProperty("prepStmtCacheSize", Integer.valueOf(config.getPrepStmtCacheSize()));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", Integer.valueOf(config.getPrepStmtCacheSqlLimit()));
        dataSource.addDataSourceProperty("useUnicode", "true");
        dataSource.addDataSourceProperty("characterEncoding", "UTF-8");
        return dataSource;
    }

    public static BoneCPDataSource boneCP() {
        BoneCPConfig config = BoneCPConfig.builder();
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setPartitionCount(config.getPartitionCount());
        dataSource.setPoolAvailabilityThreshold(config.getPoolAvailabilityThreshold());
        dataSource.setMinConnectionsPerPartition(config.getMinConnectionsPerPartition());
        dataSource.setMaxConnectionsPerPartition(config.getMaxConnectionsPerPartition());
        dataSource.setAcquireIncrement(config.getAcquireIncrement());
        dataSource.setAcquireRetryAttempts(config.getAcquireRetryAttempts());
        dataSource.setAcquireRetryDelayInMs(config.getAcquireRetryDelayInMs());
        dataSource.setConnectionTimeoutInMs(config.getConnectionTimeoutInMs());
        dataSource.setNullOnConnectionTimeout(config.isNullOnConnectionTimeout());
        dataSource.setLazyInit(config.isLazyInit());
        dataSource.setStatementsCacheSize(config.getStatementsCacheSize());
        dataSource.setQueryExecuteTimeLimitInMs(config.getQueryExecuteTimeLimitInMs());
        dataSource.setDefaultTransactionIsolation(config.getDefaultTransactionIsolation());
        dataSource.setDisableJMX(config.isDisableJMX());
        dataSource.setCloseConnectionWatch(config.isCloseConnectionWatch());
        dataSource.setPoolName(config.getPoolName());
        dataSource.setDisableConnectionTracking(config.isDisableConnectionTracking());
        dataSource.setConnectionTestStatement(config.getConnectionTestStatement());
        dataSource.setIdleConnectionTestPeriodInMinutes(config.getIdleConnectionTestPeriodInMinutes());
        dataSource.setIdleMaxAgeInMinutes(config.getIdleMaxAgeInMinutes());
        return dataSource;
    }

    public static C3P0DataSource c3p0() {
        C3P0Config config = C3P0Config.builder();
        C3P0DataSource dataSource = new C3P0DataSource();
        dataSource.setInitialPoolSize(config.getInitialPoolSize());
        dataSource.setMinPoolSize(config.getMinPoolSize());
        dataSource.setMaxPoolSize(config.getMaxPoolSize());
        dataSource.setAcquireIncrement(config.getAcquireIncrement());
        dataSource.setAcquireRetryAttempts(config.getAcquireRetryAttempts());
        dataSource.setAcquireRetryDelay(config.getAcquireRetryDelay());
        dataSource.setCheckoutTimeout(config.getCheckoutTimeout());
        dataSource.setNumHelperThreads(config.getNumHelperThreads());
        dataSource.setMaxStatementsPerConnection(config.getMaxStatementsPerConnection());
        dataSource.setPropertyCycle(config.getPropertyCycle());
        dataSource.setBreakAfterAcquireFailure(config.isBreakAfterAcquireFailure());
        dataSource.setTestConnectionOnCheckout(config.isTestConnectionOnCheckout());
        dataSource.setTestConnectionOnCheckin(config.isTestConnectionOnCheckin());
        dataSource.setIdleConnectionTestPeriod(config.getIdleConnectionTestPeriod());
        dataSource.setAutomaticTestTable(config.getAutomaticTestTable());
        dataSource.setMaxIdleTime(config.getMaxIdleTime());
        dataSource.setUnreturnedConnectionTimeout(config.getUnreturnedConnectionTimeout());
        dataSource.setDebugUnreturnedConnectionStackTraces(config.isDebugUnreturnedConnectionStackTraces());
        return dataSource;
    }
}

