package com.banzhiyan.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.ByteArrayBuffer;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class Bytes {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final char[] BASE16 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static final Map<Integer, byte[]> DECODE_TABLE_MAP = new ConcurrentHashMap();
    private static ThreadLocal<MessageDigest> MD = new ThreadLocal();

    public Bytes() {
    }

    public static byte[] toBytes(InputStream instream, int length) throws IOException {
        Assert.notNull(instream, "instream参数不能为null");
        ByteArrayBuffer buffer = new ByteArrayBuffer(length);
        byte[] tmp = new byte[8192];

        int l;
        while((l = instream.read(tmp)) != -1) {
            buffer.append(tmp, 0, l);
        }

        return buffer.toByteArray();
    }

    public static String toString(byte[] src, int beginIndex, int endIndex) {
        if(src == null) {
            return "null";
        } else {
            int iMax = src.length - 1;
            if(iMax == -1) {
                return "[]";
            } else {
                StringBuilder b = new StringBuilder();
                b.append('[');
                int i = 0;

                while(true) {
                    b.append(src[i]);
                    if(i == iMax) {
                        return b.append(']').toString();
                    }

                    b.append(", ");
                    ++i;
                }
            }
        }
    }

    public static byte[] copyOf(byte[] src, int length) {
        byte[] dest = new byte[length];
        System.arraycopy(src, 0, dest, 0, Math.min(src.length, length));
        return dest;
    }

    public static byte[] short2bytes(short v) {
        byte[] ret = new byte[2];
        short2bytes(v, ret);
        return ret;
    }

    public static void short2bytes(short v, byte[] b) {
        short2bytes(v, b, 0);
    }

    public static void short2bytes(short v, byte[] b, int off) {
        b[off + 1] = (byte)v;
        b[off + 0] = (byte)(v >>> 8);
    }

    public static byte[] int2bytes(int v) {
        byte[] ret = new byte[4];
        int2bytes(v, ret);
        return ret;
    }

    public static void int2bytes(int v, byte[] b) {
        int2bytes(v, b, 0);
    }

    public static void int2bytes(int v, byte[] b, int off) {
        b[off + 3] = (byte)v;
        b[off + 2] = (byte)(v >>> 8);
        b[off + 1] = (byte)(v >>> 16);
        b[off + 0] = (byte)(v >>> 24);
    }

    public static byte[] float2bytes(float v) {
        byte[] ret = new byte[4];
        float2bytes(v, ret);
        return ret;
    }

    public static void float2bytes(float v, byte[] b) {
        float2bytes(v, b, 0);
    }

    public static void float2bytes(float v, byte[] b, int off) {
        int i = Float.floatToIntBits(v);
        b[off + 3] = (byte)i;
        b[off + 2] = (byte)(i >>> 8);
        b[off + 1] = (byte)(i >>> 16);
        b[off + 0] = (byte)(i >>> 24);
    }

    public static byte[] long2bytes(long v) {
        byte[] ret = new byte[8];
        long2bytes(v, ret);
        return ret;
    }

    public static void long2bytes(long v, byte[] b) {
        long2bytes(v, b, 0);
    }

    public static void long2bytes(long v, byte[] b, int off) {
        b[off + 7] = (byte)((int)v);
        b[off + 6] = (byte)((int)(v >>> 8));
        b[off + 5] = (byte)((int)(v >>> 16));
        b[off + 4] = (byte)((int)(v >>> 24));
        b[off + 3] = (byte)((int)(v >>> 32));
        b[off + 2] = (byte)((int)(v >>> 40));
        b[off + 1] = (byte)((int)(v >>> 48));
        b[off + 0] = (byte)((int)(v >>> 56));
    }

    public static byte[] double2bytes(double v) {
        byte[] ret = new byte[8];
        double2bytes(v, ret);
        return ret;
    }

    public static void double2bytes(double v, byte[] b) {
        double2bytes(v, b, 0);
    }

    public static void double2bytes(double v, byte[] b, int off) {
        long j = Double.doubleToLongBits(v);
        b[off + 7] = (byte)((int)j);
        b[off + 6] = (byte)((int)(j >>> 8));
        b[off + 5] = (byte)((int)(j >>> 16));
        b[off + 4] = (byte)((int)(j >>> 24));
        b[off + 3] = (byte)((int)(j >>> 32));
        b[off + 2] = (byte)((int)(j >>> 40));
        b[off + 1] = (byte)((int)(j >>> 48));
        b[off + 0] = (byte)((int)(j >>> 56));
    }

    public static short bytes2short(byte[] b) {
        return bytes2short(b, 0);
    }

    public static short bytes2short(byte[] b, int off) {
        return (short)(((b[off + 1] & 255) << 0) + (b[off + 0] << 8));
    }

    public static int bytes2int(byte[] b) {
        return bytes2int(b, 0);
    }

    public static int bytes2int(byte[] b, int off) {
        return ((b[off + 3] & 255) << 0) + ((b[off + 2] & 255) << 8) + ((b[off + 1] & 255) << 16) + (b[off + 0] << 24);
    }

    public static long bytes2long(byte[] bytes) {
        long l = 0L;

        for(int i = 0; i < 8; ++i) {
            l <<= 8;
            l ^= (long)(bytes[i] & 255);
        }

        return l;
    }

    public static float bytes2float(byte[] b) {
        return bytes2float(b, 0);
    }

    public static float bytes2float(byte[] b, int off) {
        int i = ((b[off + 3] & 255) << 0) + ((b[off + 2] & 255) << 8) + ((b[off + 1] & 255) << 16) + (b[off + 0] << 24);
        return Float.intBitsToFloat(i);
    }

    public static String bytes2hex(byte[] bs) {
        return bytes2hex(bs, 0, bs.length);
    }

    public static String bytes2hex(byte[] bs, int off, int len) {
        if(off < 0) {
            throw new IndexOutOfBoundsException("bytes2hex: offset < 0, offset is " + off);
        } else if(len < 0) {
            throw new IndexOutOfBoundsException("bytes2hex: length < 0, length is " + len);
        } else if(off + len > bs.length) {
            throw new IndexOutOfBoundsException("bytes2hex: offset + length > array length.");
        } else {
            int r = off;
            int w = 0;
            char[] cs = new char[len * 2];

            for(int i = 0; i < len; ++i) {
                byte b = bs[r++];
                cs[w++] = BASE16[b >> 4 & 15];
                cs[w++] = BASE16[b & 15];
            }

            return new String(cs);
        }
    }

    public static byte[] hex2bytes(String str) {
        return hex2bytes(str, 0, str.length());
    }

    public static byte[] hex2bytes(String str, int off, int len) {
        if((len & 1) == 1) {
            throw new IllegalArgumentException("hex2bytes: ( len & 1 ) == 1.");
        } else if(off < 0) {
            throw new IndexOutOfBoundsException("hex2bytes: offset < 0, offset is " + off);
        } else if(len < 0) {
            throw new IndexOutOfBoundsException("hex2bytes: length < 0, length is " + len);
        } else if(off + len > str.length()) {
            throw new IndexOutOfBoundsException("hex2bytes: offset + length > array length.");
        } else {
            int num = len / 2;
            int r = off;
            int w = 0;
            byte[] b = new byte[num];

            for(int i = 0; i < num; ++i) {
                b[w++] = (byte)(hex(str.charAt(r++)) << 4 | hex(str.charAt(r++)));
            }

            return b;
        }
    }

    public static String bytes2base64(byte[] b) {
        return bytes2base64(b, 0, b.length, (char[])BASE64);
    }

    public static String bytes2base64(byte[] b, int offset, int length) {
        return bytes2base64(b, offset, length, BASE64);
    }

    public static String bytes2base64(byte[] b, String code) {
        return bytes2base64(b, 0, b.length, (String)code);
    }

    public static String bytes2base64(byte[] b, int offset, int length, String code) {
        if(code.length() < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        } else {
            return bytes2base64(b, offset, length, code.toCharArray());
        }
    }

    public static String bytes2base64(byte[] b, char[] code) {
        return bytes2base64(b, 0, b.length, (char[])code);
    }

    public static String bytes2base64(byte[] bs, int off, int len, char[] code) {
        if(off < 0) {
            throw new IndexOutOfBoundsException("bytes2base64: offset < 0, offset is " + off);
        } else if(len < 0) {
            throw new IndexOutOfBoundsException("bytes2base64: length < 0, length is " + len);
        } else if(off + len > bs.length) {
            throw new IndexOutOfBoundsException("bytes2base64: offset + length > array length.");
        } else if(code.length < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        } else {
            boolean pad = code.length > 64;
            int num = len / 3;
            int rem = len % 3;
            int r = off;
            int w = 0;
            char[] cs = new char[num * 4 + (pad?4:(rem == 0?0:rem + 1))];

            int b1;
            int b2;
            for(b1 = 0; b1 < num; ++b1) {
                b2 = bs[r++] & 255;
                int b3 = bs[r++] & 255;
                cs[w++] = code[b2 >> 2];
                cs[w++] = code[b2 << 4 & 63 | b2 >> 4];
                cs[w++] = code[b2 << 2 & 63 | b3 >> 6];
                cs[w++] = code[b3 & 63];
            }

            if(rem == 1) {
                b1 = bs[r++] & 255;
                cs[w++] = code[b1 >> 2];
                cs[w++] = code[b1 << 4 & 63];
                if(pad) {
                    cs[w++] = code[64];
                    cs[w++] = code[64];
                }
            } else if(rem == 2) {
                b1 = bs[r++] & 255;
                b2 = bs[r++] & 255;
                cs[w++] = code[b1 >> 2];
                cs[w++] = code[b1 << 4 & 63 | b2 >> 4];
                cs[w++] = code[b2 << 2 & 63];
                if(pad) {
                    cs[w++] = code[64];
                }
            }

            return new String(cs);
        }
    }

    public static byte[] base642bytes(String str) {
        return base642bytes(str, 0, str.length());
    }

    public static byte[] base642bytes(String str, int offset, int length) {
        return base642bytes(str, offset, length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");
    }

    public static byte[] base642bytes(String str, String code) {
        return base642bytes(str, 0, str.length(), (String)code);
    }

    public static byte[] base642bytes(String str, int off, int len, String code) {
        if(off < 0) {
            throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off);
        } else if(len < 0) {
            throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len);
        } else if(off + len > str.length()) {
            throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");
        } else if(code.length() < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        } else {
            int rem = len % 4;
            if(rem == 1) {
                throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
            } else {
                int num = len / 4;
                int size = num * 3;
                if(code.length() > 64) {
                    if(rem != 0) {
                        throw new IllegalArgumentException("base642bytes: base64 string length error.");
                    }

                    char pc = code.charAt(64);
                    if(str.charAt(off + len - 2) == pc) {
                        size -= 2;
                        --num;
                        rem = 2;
                    } else if(str.charAt(off + len - 1) == pc) {
                        --size;
                        --num;
                        rem = 3;
                    }
                } else if(rem == 2) {
                    ++size;
                } else if(rem == 3) {
                    size += 2;
                }

                int r = off;
                int w = 0;
                byte[] b = new byte[size];
                byte[] t = decodeTable(code);

                byte c2;
                byte c3;
                for(int i = 0; i < num; ++i) {
                    c2 = t[str.charAt(r++)];
                    c3 = t[str.charAt(r++)];
                    int c4 = t[str.charAt(r++)];
                    b[w++] = (byte)(c2 << 2 | c3 >> 4);
                    b[w++] = (byte)(c3 << 4 | c3 >> 2);
                    b[w++] = (byte)(c3 << 6 | c4);
                }

                byte c1;
                if(rem == 2) {
                    c1 = t[str.charAt(r++)];
                    c2 = t[str.charAt(r++)];
                    b[w++] = (byte)(c1 << 2 | c2 >> 4);
                } else if(rem == 3) {
                    c1 = t[str.charAt(r++)];
                    c2 = t[str.charAt(r++)];
                    c3 = t[str.charAt(r++)];
                    b[w++] = (byte)(c1 << 2 | c2 >> 4);
                    b[w++] = (byte)(c2 << 4 | c3 >> 2);
                }

                return b;
            }
        }
    }

    public static byte[] base642bytes(String str, char[] code) {
        return base642bytes(str, 0, str.length(), (char[])code);
    }

    public static byte[] base642bytes(String str, int off, int len, char[] code) {
        if(off < 0) {
            throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off);
        } else if(len < 0) {
            throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len);
        } else if(off + len > str.length()) {
            throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");
        } else if(code.length < 64) {
            throw new IllegalArgumentException("Base64 code length < 64.");
        } else {
            int rem = len % 4;
            if(rem == 1) {
                throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");
            } else {
                int num = len / 4;
                int size = num * 3;
                if(code.length > 64) {
                    if(rem != 0) {
                        throw new IllegalArgumentException("base642bytes: base64 string length error.");
                    }

                    char pc = code[64];
                    if(str.charAt(off + len - 2) == pc) {
                        size -= 2;
                    } else if(str.charAt(off + len - 1) == pc) {
                        --size;
                    }
                } else if(rem == 2) {
                    ++size;
                } else if(rem == 3) {
                    size += 2;
                }

                int r = off;
                int w = 0;
                byte[] b = new byte[size];

                int c1;
                int c2;
                int c3;
                for(c1 = 0; c1 < num; ++c1) {
                    c2 = indexOf(code, str.charAt(r++));
                    c3 = indexOf(code, str.charAt(r++));
                    int c4 = indexOf(code, str.charAt(r++));
                    b[w++] = (byte)(c2 << 2 | c3 >> 4);
                    b[w++] = (byte)(c3 << 4 | c3 >> 2);
                    b[w++] = (byte)(c3 << 6 | c4);
                }

                if(rem == 2) {
                    c1 = indexOf(code, str.charAt(r++));
                    c2 = indexOf(code, str.charAt(r++));
                    b[w++] = (byte)(c1 << 2 | c2 >> 4);
                } else if(rem == 3) {
                    c1 = indexOf(code, str.charAt(r++));
                    c2 = indexOf(code, str.charAt(r++));
                    c3 = indexOf(code, str.charAt(r++));
                    b[w++] = (byte)(c1 << 2 | c2 >> 4);
                    b[w++] = (byte)(c2 << 4 | c3 >> 2);
                }

                return b;
            }
        }
    }

    public static byte[] zip(byte[] bytes) throws IOException {
        UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
        DeflaterOutputStream os = new DeflaterOutputStream(bos);

        try {
            os.write(bytes);
        } finally {
            os.close();
            bos.close();
        }

        return bos.toByteArray();
    }

    public static byte[] unzip(byte[] bytes) throws IOException {
        UnsafeByteArrayInputStream bis = new UnsafeByteArrayInputStream(bytes);
        UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
        InflaterInputStream is = new InflaterInputStream(bis);

        byte[] var4;
        try {
            IOUtils.copyLarge(is, bos);
            var4 = bos.toByteArray();
        } finally {
            is.close();
            bis.close();
            bos.close();
        }

        return var4;
    }

    public static byte[] getMD5(String str) {
        return getMD5(str.getBytes());
    }

    public static byte[] getMD5(byte[] source) {
        MessageDigest md = getMessageDigest();
        return md.digest(source);
    }

    public static byte[] getMD5(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);

        byte[] var2;
        try {
            var2 = getMD5((InputStream)is);
        } finally {
            is.close();
        }

        return var2;
    }

    public static byte[] getMD5(InputStream is) throws IOException {
        return getMD5(is, 8192);
    }

    private static byte hex(char c) {
        if(c <= 57) {
            return (byte)(c - 48);
        } else if(c >= 97 && c <= 102) {
            return (byte)(c - 97 + 10);
        } else if(c >= 65 && c <= 70) {
            return (byte)(c - 65 + 10);
        } else {
            throw new IllegalArgumentException("hex string format error [" + c + "].");
        }
    }

    private static int indexOf(char[] cs, char c) {
        int i = 0;

        for(int len = cs.length; i < len; ++i) {
            if(cs[i] == c) {
                return i;
            }
        }

        return -1;
    }

    private static byte[] decodeTable(String code) {
        int hash = code.hashCode();
        byte[] ret = (byte[])DECODE_TABLE_MAP.get(Integer.valueOf(hash));
        if(ret == null) {
            if(code.length() < 64) {
                throw new IllegalArgumentException("Base64 code length < 64.");
            }

            ret = new byte[128];

            int i;
            for(i = 0; i < 128; ++i) {
                ret[i] = -1;
            }

            for(i = 0; i < 64; ++i) {
                ret[code.charAt(i)] = (byte)i;
            }

            DECODE_TABLE_MAP.put(Integer.valueOf(hash), ret);
        }

        return ret;
    }

    private static byte[] getMD5(InputStream is, int bs) throws IOException {
        MessageDigest md = getMessageDigest();

        for(byte[] buf = new byte[bs]; is.available() > 0; md.update(buf)) {
            int total = 0;

            int read;
            while((read = is.read(buf, total, bs - total)) > 0) {
                total += read;
                if(total >= bs) {
                    break;
                }
            }
        }

        return md.digest();
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest ret = (MessageDigest)MD.get();
        if(ret == null) {
            try {
                ret = MessageDigest.getInstance("MD5");
                MD.set(ret);
            } catch (NoSuchAlgorithmException var2) {
                throw new RuntimeException(var2);
            }
        }

        return ret;
    }

    public static int readInputStreamToArray(InputStream in, byte[] buffer, int length) throws IOException {
        int actualReadLength = 0;
        int l;
        while((l = in.read(buffer, actualReadLength, length - actualReadLength)) != -1) {
            actualReadLength += l;
            if(actualReadLength == length) {
                break;
            }
        }

        return actualReadLength;
    }
}

