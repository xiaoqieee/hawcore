package com.banzhiyan.util;

import java.nio.charset.Charset;

import com.banzhiyan.core.util.EnvUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class ZkClientCreator {
    private static final ZkClientCreator.StringSerializer STRING_SERIALIZER = new ZkClientCreator.StringSerializer();

    public ZkClientCreator() {
    }

    public static ZkClient get() {
        return get(STRING_SERIALIZER);
    }

    public static ZkClient get(ZkSerializer zkSerializer) {
        return new ZkClient(zkServers(), 3000, 3000, zkSerializer);
    }

    public static String zkServers() {
        return EnvUtils.getOrThrow("cfg_zk_servers");
    }

    private static class StringSerializer implements ZkSerializer {
        private final Charset charset;

        public StringSerializer() {
            this(Charsets.UTF_8);
        }

        public StringSerializer(Charset charset) {
            this.charset = charset;
        }

        public String deserialize(byte[] bytes) {
            return bytes == null?null:new String(bytes, this.charset);
        }

        public byte[] serialize(Object data) throws ZkMarshallingError {
            return data == null?null:String.valueOf(data).getBytes(this.charset);
        }
    }
}
