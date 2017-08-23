package com.banzhiyan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final ThreadLocal<ConcurrentMap<String, SimpleDateFormat>> locals = new ThreadLocal();

    public DateFormatUtils() {
    }

    public static Date parse(String pattern, String date) throws ParseException {
        if(date == null) {
            return null;
        } else {
            SimpleDateFormat fmt = getDateFormat(pattern);

            try {
                return fmt.parse(date);
            } catch (ParseException var5) {
                ParseException ex = new ParseException(var5.getMessage() + " - expected '" + pattern + '\'', var5.getErrorOffset());
                ex.setStackTrace(var5.getStackTrace());
                throw ex;
            }
        }
    }

    public static String format(String pattern, Date d) {
        return d == null?null:getDateFormat(pattern).format(d);
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        ConcurrentMap<String, SimpleDateFormat> map = (ConcurrentMap)locals.get();
        if(map == null) {
            map = new ConcurrentHashMap();
            locals.set(map);
        }

        SimpleDateFormat df = (SimpleDateFormat)((ConcurrentMap)map).get(pattern);
        if(df == null) {
            df = new SimpleDateFormat(pattern);
            ((ConcurrentMap)map).put(pattern, df);
        }

        return df;
    }
}
