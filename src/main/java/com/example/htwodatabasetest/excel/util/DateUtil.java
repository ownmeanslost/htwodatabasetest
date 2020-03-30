package com.example.htwodatabasetest.excel.util;


import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 日期格式：年月日
     */
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /**
     * 时间格式：年月日 时分秒
     */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_DATETIME1 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 日期格式：年月日
     */
    public static final String FORMAT_DATE_NOLINE = "yyyyMMdd";
    /**
     * 日期格式：年月日
     */
    public static final String FORMAT_DATETIME_NOLINE = "yyyyMMddHHmmss";
    /**
     * 日期格式：年
     */
    public static final String FORMAT_YYYY = "yyyy";
    /**
     * 日期格式：月
     */
    public static final String FORMAT_MM = "MM";
    /**
     * 日期格式：日
     */
    public static final String FORMAT_DD = "dd";

    /**
     * 去掉日期时分秒
     * @param dateTime 日期+时间
     * @return 日期
     * @throws ParseException 异常
     */
    public static Date removeTime(Date dateTime) throws ParseException {
        return dateTime == null ? null : ScmDateFormatUtil.parse(ScmDateFormatUtil.format(dateTime, FORMAT_DATE), FORMAT_DATE);
    }
    
    public static String format(Date dateTime,String pattern) {
        return ScmDateFormatUtil.format(dateTime, pattern);
    }

    /**
     * 获取今天日期的字符串，格式：yyyy-MM-dd
     * @return 今天日期的字符串
     */
    public static String getTodayString() {
        return format(new Date(), FORMAT_DATE);
    }

    /**
     * 获取输入日期的下一天
     * @param date 时间
     * @return 输入时间的下一天
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    /*
    *
     *功能描述 获取上一天24点
     * @author shaofan.li
     * @date
     * @param
     * @return
     */
    public static String getLastDayFor24(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATETIME1);

        //前X天的字符串
        String pre = simpleDateFormat.format(date);
        //前x天午夜24点的字符串
        return pre.substring(0, 10) + " 24:00:00";
    }

    public static Date getLastDayFor24ReturnDate(Date date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATETIME1);
        return simpleDateFormat.parse(getLastDayFor24(date));
    }

    /**
     * 获取输入日期的上一天
     * @param date 时间
     * @return 输入时间的下一天
     */
    public static String getBeforeDay(Date date, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATETIME1);
        return simpleDateFormat.format(date);
    }


    /**
     * 获取输入日期的上一天
     * @param date 时间
     * @return 输入时间的下一天
     */
    public static Date addMinute(Date date, int min) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min);
        date = calendar.getTime();

        return date;
    }
    /**
     * 获取输入日期的上一天
     * @param date 时间
     * @return 输入时间的下一天
     */
    public static Date getBeforeDayReturnDate(Date date, int day) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATETIME1);
        return simpleDateFormat.parse(getBeforeDay(date, day));
    }



    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATETIME1);
        return simpleDateFormat.format(date);
    }

    /**
     * 比较两个时间
     * @param date1
     * @param date2
     */
    public static int compareDate(Date date1, Date date2){
        if(date1 ==null || date2 == null){
            return -1;
        }
        Calendar calendar = DateUtils.toCalendar(date1);
        Calendar anotherCalendar = DateUtils.toCalendar(date2);
        return calendar.compareTo(anotherCalendar);
    }

    /**
     * 获取某时间之前的日期（以月为计量单位）
     * @param date 参考日期
     * @param monthAmount 月数
     * @return 参考日期date之前monthAmount个月的日期
     */
    public static Date getBeforeDateByMonth(Date date, int monthAmount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -monthAmount);
        return calendar.getTime();
    }

    /**
     * 获取某时间之前的日期字符串（以月为计量单位）
     * @param date 参考日期
     * @param monthAmount 月数
     * @return 参考日期date之前monthAmount个月的日期字符串（格式：yyyy-MM-dd）
     */
    public static String getBeforeDateStringByMonth(Date date, int monthAmount) {
        return format(getBeforeDateByMonth(date, monthAmount), FORMAT_DATE);
    }

    /**
     * 时间按小时添加或减小
     * @param date 参考日期
     * @param hours 需要添加或减少的小时数
     *
     */
    public static Date addDateByHour(Date date, int hours) {
       if(null == date){
           date = new Date();
       }
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(Calendar.HOUR, hours);
       return calendar.getTime();
    }

    /**
     * 通用equals方法
     * @author xiaowei.zhou
     * @return 是否相等
     */
    public static boolean equals(Date var1, Date var2) {
        return var1 == null? var2 == null: var1.equals(var2);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
  public static Date strToDateLong(String strDate) throws ParseException {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.parse(strDate);

  }

    /*public static void main(String[] args) throws ParseException {
        Date date = strToDateLong("2019-05-11 12:00:00");


    }*/
}
