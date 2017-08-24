package com.banzhiyan.util;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class NullSafe {
    private NullSafe() {
    }

    public static boolean equals(Object a, Object b) {
        return a == b?true:(a == null?b == null:(b == null?false:a.equals(b)));
    }

    public static int hashCode(Object o) {
        return o == null?0:o.hashCode();
    }

    public static String toString(Object o) {
        return o == null?"(null)":o.toString();
    }
}
