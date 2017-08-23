package com.banzhiyan.util;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60000L;
    public static final long MILLIS_PER_HALFHOUR = 1800000L;
    public static final long MILLIS_PER_HOUR = 3600000L;
    public static final long MILLIS_PER_DAY = 86400000L;
    public static final long MILLIS_PER_YEAR = 31536000000L;

    public DateUtils() {
    }

    public static Date parse(String pattern, String date) throws ParseException {
        return DateFormatUtils.parse(pattern, date);
    }

    public static String format(String pattern, Date d) {
        return DateFormatUtils.format(pattern, d);
    }
}
