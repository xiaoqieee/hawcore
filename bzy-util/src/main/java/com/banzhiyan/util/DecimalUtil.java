package com.banzhiyan.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class DecimalUtil {
    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");
    public static final BigDecimal ZERO;
    private static final int SCALE = 2;

    private DecimalUtil() {
    }

    public static BigDecimal format(double value) {
        return new BigDecimal(FORMAT.format(value));
    }

    public static BigDecimal format(long value) {
        return new BigDecimal(FORMAT.format(value));
    }

    public static BigDecimal formatWithoutScale(BigDecimal value) {
        return value.setScale(0, 6);
    }

    public static BigDecimal format(BigDecimal value) {
        return value.setScale(2, 6);
    }

    public static BigDecimal add(BigDecimal d1, BigDecimal d2) {
        return format(d1.add(d2));
    }

    public static BigDecimal add(BigDecimal v1, BigDecimal... vs) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal[] arr$ = vs;
        int len$ = vs.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            BigDecimal v = arr$[i$];
            sum = add(sum, v);
        }

        return add(v1, sum);
    }

    public static BigDecimal subtract(BigDecimal v1, BigDecimal... vs) {
        BigDecimal sum = add(BigDecimal.ZERO, vs);
        return subtract(v1, sum);
    }

    public static BigDecimal subtract(BigDecimal d1, BigDecimal d2) {
        return format(d1.subtract(d2));
    }

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        return format(v1.divide(v2, MathContext.DECIMAL128));
    }

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2, int roundingMode) {
        return v1.divide(v2, 2, roundingMode);
    }

    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        return format(v1.multiply(v2));
    }

    private static int compareBigDecimal(BigDecimal v1, BigDecimal v2) {
        return format(v1).compareTo(format(v2));
    }

    public static boolean gt(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) > 0;
    }

    public static boolean ge(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) >= 0;
    }

    public static boolean eq(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) == 0;
    }

    public static boolean lt(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) < 0;
    }

    public static boolean le(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) <= 0;
    }

    public static boolean ne(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) != 0;
    }

    static {
        FORMAT.setRoundingMode(RoundingMode.HALF_EVEN);
        ZERO = BigDecimal.ZERO;
    }
}
