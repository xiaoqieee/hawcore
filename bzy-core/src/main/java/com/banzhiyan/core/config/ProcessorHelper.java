package com.banzhiyan.core.config;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ProcessorHelper {
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors() + 1;

    public ProcessorHelper() {
    }

    public static String oneToTriple() {
        return number() + "-" + triple();
    }

    public static String oneToFourfold() {
        return number() + "-" + fourfold();
    }

    public static int number() {
        return PROCESSORS;
    }

    public static int duple() {
        return number() * 2;
    }

    public static int triple() {
        return number() * 3;
    }

    public static int fourfold() {
        return number() * 4;
    }
}
