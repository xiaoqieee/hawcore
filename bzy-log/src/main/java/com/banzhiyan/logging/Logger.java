package com.banzhiyan.logging;

/**
 * Created by xn025665 on 2017/8/23.
 */

public interface Logger {
    void error(String var1);

    void warn(String var1);

    void info(String var1);

    void debug(String var1);

    void error(String var1, Throwable var2);

    void warn(String var1, Throwable var2);

    void info(String var1, Throwable var2);

    void debug(String var1, Throwable var2);

    void error(String var1, Object... var2);

    void warn(String var1, Object... var2);

    void info(String var1, Object... var2);

    void debug(String var1, Object... var2);

    void catching(Throwable var1);

    <T extends Throwable> T throwing(T var1);

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();
}

