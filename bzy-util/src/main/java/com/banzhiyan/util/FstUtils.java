package com.banzhiyan.util;

import org.nustaq.serialization.FSTConfiguration;
/**
 * Created by xn025665 on 2017/8/23.
 */
public final class FstUtils {
    private static final ThreadLocal<FSTConfiguration> FSTS = new ThreadLocal<FSTConfiguration>() {
        protected FSTConfiguration initialValue() {
            return FSTConfiguration.createDefaultConfiguration();
        }
    };

    public FstUtils() {
    }

    public static byte[] serialize(Object obj) {
        return ((FSTConfiguration)FSTS.get()).asByteArray(obj);
    }

    public static <T> T deserialize(byte[] data) {
        return (T)((FSTConfiguration)FSTS.get()).asObject(data);
    }
}
