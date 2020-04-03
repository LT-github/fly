package com.lt.fly.utils;


import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 */
public class DateUtil {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";// 时间格式
    public static final String DEFAULT_FORMAT1 = "yyyy/MM/dd HH:mm:ss";// 时间格式1
    public static final String DEFAULT_FORMATS = "yyyy-MM-dd";
    public static final String DATE_FOMATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String Message_TIME = "yyyy年MM月dd日HH点mm分";

    public static final long ONE_DAY_TIME = 86400000;


    /**
     * 格式化时间(Date 转换成String)
     *
     * @param date   时间
     * @param format 时间格式 如： DEFAULT_FORMAT= "yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    /**
     * 字符串格式化为时间
     *
     * @param dateStr 时间字符串
     * @param format  时间格式 如：DEFAULT_FORMAT1 = "yyyy/MM/dd HH:mm:ss";// 时间格式1
     * @return
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        if (!StringUtils.isEmpty(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 获取当天的开始时间,格式：yyyy-MM-dd 00:00:00
     *
     * @return
     */
    public static Date getCurrentStartTime() {
        return getSomeDayStartTime(new Date(), DEFAULT_FORMAT);
    }

    /**
     * 获取当天的结束时间,格式：yyyy-MM-dd 23:59:59
     *
     * @return
     */
    public static Date getCurrentEndTime() {
        return getSomeDayEndTime(new Date(), DEFAULT_FORMAT);
    }

    public static Date getSomeDayStartTime(Date date, String format) {
        return getSomeDayStartAndEndTime(date, format, " 00:00:00");
    }

    public static Date getSomeDayEndTime(Date date, String format) {
        return getSomeDayStartAndEndTime(date, format, " 23:59:59");

    }

    public static Date getSomeDayStartAndEndTime(Date date, String format, String format2) {
        String d = format(date, DEFAULT_FORMAT);
        String[] split = d.split("");
        String startTimeStr = split[0] + format2;
        return parseDate(startTimeStr, format);

    }

    /**
     * 获取某个时间之后多少天的时间
     *
     * @param date 某时间
     * @param day  几天
     * @return
     */
    public static Date getTimeToAfter(Date date, int day) {
        return new Date(date.getTime() + ONE_DAY_TIME * day);
    }

    /**
     * 获取某个时间之前多少天的时间
     *
     * @param date 某时间
     * @param day  几天
     * @return
     */
    public static Date getTimeToBeffre(Date date, int day) {
        return new Date(date.getTime() - ONE_DAY_TIME * day);
    }

    /**
     * 获取某个时间的下几分钟的时间戳
     *
     * @param time 某个时间
     * @param m    分钟数
     * @return
     */
    public static Long getTimeToMin(long time, long m) {
        long min = time / (1000 * 60);
        if (min * 1000 * 60 < time) {
            min++;
        }
        for (int i = 0; i < m + 1; i++) {
            if (min % m == 0) {
                return min * 1000 * 60;
            }
            min++;
        }
        return null;
    }

    /**
     * 时间戳转换为时间格式
     *
     * @param lt
     * @return
     */
    public static String stampToDate(long lt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT1);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }


    /**
     * 获取两个时间戳范围内有多少天
     */

    //计算间隔日，比较两个时间是否同一天，如果两个时间都是同一天的话，返回0。
    // 两个比较的时间都不是同一天的话，根据传参位置 可返回正数/负数。两个比较的时间都是同一天的话，返回0。返回时间区间
    public static List<Long> daysBetween(Long start, Long end) {

        List<Long> list = new ArrayList<Long>();

        Long n = end - start;
        int days = (int) (n / (ONE_DAY_TIME));

        Long areEnd = 0l;
        //如果是同一天
        if (days == 0) {
            list.add(start);
            list.add(start + ONE_DAY_TIME);
        } else {

            for (int i = 0; i < days + 2; i++) {

                list.add(start);
                areEnd = start + ONE_DAY_TIME;

                start = areEnd;
            }
        }

        return list;

    }


    /**
     * 获取指定某一天的开始时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @param timeZone  如 GMT+8:00
     * @return
     */
    public static Long getDailyStartTime(Long timeStamp, String timeZone) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定某一天的结束时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @param timeZone  如 GMT+8:00
     * @return
     */
    public static Long getDailyEndTime(Long timeStamp, String timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定日期所在月份开始的时间戳
     *
     * @param timeStrem 指定日期
     * @return
     */
    public static Long getMonthBegin(Long timeStrem) {


        Calendar c = Calendar.getInstance();
        //c.setTime(date);
        c.setTimeInMillis(timeStrem);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTimeInMillis();
    }


    /**
     * 获取指定日期所在月份结束的时间戳
     *
     * @param timeStrem 指定日期
     * @return
     */
    public static Long getMonthEnd(Long timeStrem) {


        Calendar c = Calendar.getInstance();
        //c.setTime(date);
        c.setTimeInMillis(timeStrem);
        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 999);
        // 获取本月最后一天的时间戳
        return c.getTimeInMillis();

    }

    /**
     * 根据传入的时间获得当前时间所在周的第一天和第七天日期
     *
     * @param timeStrem       时间
     * @param firstday 周日作为周一为0，周一作为周一1。
     * @return
     */
    public static List<Long> getWeekStartTime(Long timeStrem, int firstday) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStrem);
//		logger.debug(String.valueOf(c.get(Calendar.DAY_OF_WEEK)));
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            c.add(Calendar.DATE, -1);
        }
        List<Long> list = new ArrayList<Long>();
        Calendar cf = Calendar.getInstance();
        cf.setTime(c.getTime());
        cf.set(Calendar.DAY_OF_WEEK, cf.getFirstDayOfWeek());
        cf.add(Calendar.DATE, firstday);
        Calendar ce = Calendar.getInstance();
        ce.setTime(c.getTime());
        ce.set(Calendar.DAY_OF_WEEK, cf.getFirstDayOfWeek() + 6);
        ce.add(Calendar.DATE, firstday);
//		logger.debug(sdf.format(tm));
//		logger.debug("第一天:"+sdf.format(cf.getTime()));
//		logger.debug("第七天:"+sdf.format(ce.getTime()));
//		logger.debug("========");
        list.add(cf.getTimeInMillis());
        list.add(ce.getTimeInMillis());


        return list;

    }

    //获取某个日期的开始时间戳
    public static Long getDayStartTime(Long timeStrem) {
        Calendar calendar = Calendar.getInstance();
        if (null != timeStrem) calendar.setTimeInMillis(timeStrem);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    //获取某个日期的结束时间戳
    public static Long getDayEndTime(Long timeStrem) {
        Calendar calendar = Calendar.getInstance();
        if (null != timeStrem) calendar.setTimeInMillis(timeStrem);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * @param time 时间
     * @param num  加的数，-num就是减去
     * @return 减去相应的数量的月份的日期
     * @throws ParseException Date
     */
    public static Date monthAddNum(Date time, Integer num) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = format.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MONTH, num);
        Date newTime = calendar.getTime();
        return newTime;
    }


    public static String formatDateTime(long mss) {
        String DateTimes = null;
        mss = mss/1000;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        if (days > 0) {
            DateTimes = days + "天" + hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (hours > 0) {
            DateTimes = hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (minutes > 0) {
            DateTimes = minutes + "分钟"
                    + seconds + "秒";
        } else {
            DateTimes = seconds + "秒";
        }

        return DateTimes;
    }

	public static void main(String[] args) {
		System.out.println(formatDateTime(21600000));

	}


    /**
     * 时间戳转换为String 带格式
     * @param timestamp 时间戳
     * @param format  格式
     * @return
     */
	public static String timestampToString(Long timestamp,String format){
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat(format); //设置格式
        return simpleDateFormat.format(timestamp);
    }

}
