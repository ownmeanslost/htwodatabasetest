package com.example.htwodatabasetest.excel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ScmDateFormatUtil
 * @Author: shaofan.li
 * @Description:
 * @Date: 2019/11/23 12:12
 */
public class ScmDateFormatUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<Map<String, SimpleDateFormat>> sdfLocal = new ThreadLocal();

    private ScmDateFormatUtil() {
    }

    public static String format(Date date, String format) {
        return instance(format).format(date);
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String date, String format) throws ParseException {
        return instance(format).parse(date);
    }

    public static Date parse(String date) throws ParseException {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat instance(String format) {
        Map<String, SimpleDateFormat> map = (Map)sdfLocal.get();
        if (null == map) {
            map = new HashMap();
            sdfLocal.set(map);
        }

        SimpleDateFormat sdf = (SimpleDateFormat)((Map)map).get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format);
            ((Map)map).put(format, sdf);
        }

        return sdf;
    }
}
