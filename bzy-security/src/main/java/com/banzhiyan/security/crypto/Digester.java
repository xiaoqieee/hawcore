package com.banzhiyan.security.crypto;

import com.banzhiyan.util.Hex;
import com.banzhiyan.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class Digester {
    private final MessageDigest messageDigest;
    private final int iterations;

    public Digester(String algorithm, int iterations) {
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var4) {
            throw new IllegalStateException("No such hashing algorithm", var4);
        }

        this.iterations = iterations < 1?1:iterations;
    }

    public Digester reset() {
        this.messageDigest.reset();
        return this;
    }

    public byte[] digest(byte[] value) {
        byte[] result = value;
        MessageDigest var3 = this.messageDigest;
        synchronized(this.messageDigest) {
            for(int i = 0; i < this.iterations; ++i) {
                result = this.messageDigest.digest(result);
            }

            return result;
        }
    }

    public String digestHex(byte[] value) {
        return Hex.encodeHexString(this.digest(value));
    }

    public String digestHex(String value) {
        return Hex.encodeHexString(this.digest(StringUtils.getBytesUtf8(value)));
    }
}

