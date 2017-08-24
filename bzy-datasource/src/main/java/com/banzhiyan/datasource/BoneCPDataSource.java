package com.banzhiyan.datasource;


import com.banzhiyan.datasource.util.JdbcUrlUtils;
import com.banzhiyan.security.db.PasswordAdapter;


/**
 * Created by xn025665 on 2017/8/24.
 */

public final class BoneCPDataSource extends com.jolbox.bonecp.BoneCPDataSource implements Closeable {
    private static final long serialVersionUID = -5407189890743525588L;

    public BoneCPDataSource() {
    }

    public void setPassword(String password) {
        super.setPassword(PasswordAdapter.decodePasswordIfNeeded(password));
    }

    public void setMaxPoolSize(int maxPoolSize) {
        super.setMaxConnectionsPerPartition(maxPoolSize);
    }

    public void setJdbcUrl(String jdbcUrl) {
        super.setJdbcUrl(JdbcUrlUtils.appendPropertiesIfNecessary(jdbcUrl));
    }
}

