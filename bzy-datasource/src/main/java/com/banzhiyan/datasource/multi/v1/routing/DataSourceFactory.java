package com.banzhiyan.datasource.multi.v1.routing;

import com.banzhiyan.datasource.BoneCPDataSource;
import com.banzhiyan.datasource.C3P0DataSource;
import com.banzhiyan.datasource.HikariDataSource;
import com.banzhiyan.datasource.config.DataSourceBuilder;

/**
 * Created by xn025665 on 2017/8/24.
 */
class DataSourceFactory {
    DataSourceFactory() {
    }

    public HikariDataSource createHikariDataSource() {
        return DataSourceBuilder.hikariCP();
    }

    public BoneCPDataSource createBoneCPDataSource() {
        return DataSourceBuilder.boneCP();
    }

    public C3P0DataSource createC3p0DataSource() {
        return DataSourceBuilder.c3p0();
    }
}
