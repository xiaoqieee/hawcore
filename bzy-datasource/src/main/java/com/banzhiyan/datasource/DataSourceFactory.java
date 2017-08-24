package com.banzhiyan.datasource;

import com.banzhiyan.datasource.config.DataSourceBuilder;
import org.springframework.beans.factory.DisposableBean;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class DataSourceFactory implements DisposableBean {
    private final String driverClass;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private Closeable closeable;

    public DataSourceFactory(String driverClass, String jdbcUrl, String username, String password, int maxPoolSize) {
        this.driverClass = driverClass;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
    }

    public HikariDataSource createHikariCPDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.hikariCP();
        dataSource.setDriverClass(this.driverClass);
        dataSource.setJdbcUrl(this.jdbcUrl);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        dataSource.setMaxPoolSize(this.maxPoolSize);
        this.closeable = dataSource;
        return dataSource;
    }

    public BoneCPDataSource createBoneCPDataSource() {
        BoneCPDataSource dataSource = DataSourceBuilder.boneCP();
        dataSource.setDriverClass(this.driverClass);
        dataSource.setJdbcUrl(this.jdbcUrl);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        dataSource.setMaxPoolSize(this.maxPoolSize);
        this.closeable = dataSource;
        return dataSource;
    }

    public C3P0DataSource createC3p0DataSource() throws Exception {
        C3P0DataSource dataSource = DataSourceBuilder.c3p0();
        dataSource.setDriverClass(this.driverClass);
        dataSource.setJdbcUrl(this.jdbcUrl);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        dataSource.setMaxPoolSize(this.maxPoolSize);
        this.closeable = dataSource;
        return dataSource;
    }

    public void destroy() throws Exception {
        if(this.closeable != null) {
            this.closeable.close();
        }

    }
}
