package com.banzhiyan.datasource;

import com.banzhiyan.datasource.util.JdbcUrlUtils;
import com.banzhiyan.security.db.PasswordAdapter;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class HikariDataSource extends com.zaxxer.hikari.HikariDataSource implements Closeable {
    public HikariDataSource() {
    }

    public void setPassword(String password) {
        super.setPassword(PasswordAdapter.decodePasswordIfNeeded(password));
    }

    public void setMaxPoolSize(int maxPoolSize) {
        super.setMaximumPoolSize(maxPoolSize);
    }

    public void setDriverClass(String driverClass) {
        super.setDriverClassName(driverClass);
    }

    public void setUser(String user) {
        super.setUsername(user);
    }

    public void setJdbcUrl(String jdbcUrl) {
        super.setJdbcUrl(JdbcUrlUtils.appendPropertiesIfNecessary(jdbcUrl));
    }
}
