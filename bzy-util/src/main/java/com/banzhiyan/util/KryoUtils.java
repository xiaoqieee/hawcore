package com.banzhiyan.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class KryoUtils {
    private static final KryoFactory FACTORY = new KryoFactory() {
        public Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setAsmEnabled(true);
            kryo.setAutoReset(true);
            kryo.setReferences(true);
            kryo.register(BigDecimal.class);
            kryo.register(HashMap.class);
            kryo.register(ArrayList.class);
            kryo.register(LinkedList.class);
            kryo.register(HashSet.class);
            kryo.register(TreeSet.class);
            kryo.register(Hashtable.class);
            kryo.register(Date.class);
            kryo.register(Calendar.class);
            kryo.register(ConcurrentHashMap.class);
            kryo.register(SimpleDateFormat.class);
            kryo.register(GregorianCalendar.class);
            kryo.register(Vector.class);
            kryo.register(BitSet.class);
            kryo.register(StringBuffer.class);
            kryo.register(StringBuilder.class);
            kryo.register(Object.class);
            kryo.register(Object[].class);
            kryo.register(String[].class);
            kryo.register(byte[].class);
            kryo.register(char[].class);
            kryo.register(int[].class);
            kryo.register(float[].class);
            kryo.register(double[].class);
            return kryo;
        }
    };
    private static final KryoPool POOL;

    private KryoUtils() {
    }

    public static Kryo get() {
        return POOL.borrow();
    }

    public static void release(Kryo kryo) {
        POOL.release(kryo);
    }

    public static byte[] serialize(Object obj) {
        ByteBufferOutput output = new ByteBufferOutput(2048, -1);
        Kryo kryo = get();

        byte[] var3;
        try {
            kryo.writeClassAndObject(output, obj);
            var3 = output.toBytes();
        } finally {
            try {
                output.close();
            } catch (Exception var10) {
                ;
            }

            release(kryo);
        }

        return var3;
    }

    public static <T> T deserialize(byte[] data) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        ByteBufferInput input = new ByteBufferInput(stream, data.length * 2);
        Kryo kryo = get();

        Object var4;
        try {
            var4 = kryo.readClassAndObject(input);
        } finally {
            try {
                input.close();
            } catch (Exception var11) {
                ;
            }

            release(kryo);
        }

        return (T)var4;
    }

    static {
        POOL = (new KryoPool.Builder(FACTORY)).softReferences().build();
    }
}

