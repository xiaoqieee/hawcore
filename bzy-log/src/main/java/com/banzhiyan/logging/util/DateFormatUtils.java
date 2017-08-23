package com.banzhiyan.logging.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class DateFormatUtils {
    private static final String HH_MM_SS_SSS = "HH:mm:ss.SSS";
    private static final ThreadLocal<SimpleDateFormat> locals = new ThreadLocal();

    DateFormatUtils() {
    }

    public static String format(Date d) {
        return getDateFormat().format(d);
    }

    private static SimpleDateFormat getDateFormat() {
        SimpleDateFormat df = locals.get();
        if(df == null) {
            df = new SimpleDateFormat("HH:mm:ss.SSS");
            locals.set(df);
        }

        return df;
    }
}
