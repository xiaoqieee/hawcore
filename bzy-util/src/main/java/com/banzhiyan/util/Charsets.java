package com.banzhiyan.util;

import java.nio.charset.Charset;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class Charsets {
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset GB2312 = Charset.forName("GB2312");

    public Charsets() {
    }

    public static Charset toCharset(Charset charset) {
        return charset == null?Charset.defaultCharset():charset;
    }

    public static Charset toCharset(String charset) {
        return charset == null?Charset.defaultCharset():Charset.forName(charset);
    }
}
