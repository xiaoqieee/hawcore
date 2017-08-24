package com.banzhiyan.datasource;

import com.banzhiyan.datasource.util.JdbcUrlUtils;
import com.banzhiyan.security.db.PasswordAdapter;
import com.mchange.v2.c3p0.AbstractComboPooledDataSource;

import javax.naming.Referenceable;
import java.io.*;


/**
 * Created by xn025665 on 2017/8/24.
 */

public final class C3P0DataSource extends AbstractComboPooledDataSource implements Serializable, Referenceable, Closeable {
    private static final long serialVersionUID = 1L;
    private static final short VERSION = 2;

    public C3P0DataSource() {
    }

    public C3P0DataSource(boolean autoregister) {
        super(autoregister);
    }

    public C3P0DataSource(String configName) {
        super(configName);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeShort(2);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        short version = ois.readShort();
        switch(version) {
            case 2:
                return;
            default:
                throw new IOException("Unsupported Serialized Version: " + version);
        }
    }

    public void setUsername(String username) {
        super.setUser(username);
    }

    public void setPassword(String password) {
        super.setPassword(PasswordAdapter.decodePasswordIfNeeded(password));
    }

    public void setJdbcUrl(String jdbcUrl) {
        super.setJdbcUrl(JdbcUrlUtils.appendPropertiesIfNecessary(jdbcUrl));
    }
}

