package com.tignioj.freezeapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDateUtils {
    /**
     * 判断现在时间是否在传入的两个时间段内，排除日期(年月日)，只判断时间(时分秒)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean betweenStartTimeAndEndTime(Date startTime, Date endTime) {
        Calendar cNow = Calendar.getInstance();
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        Date now = new Date();

        cNow.setTime(now);
        cStart.setTime(startTime);
        cEnd.setTime(endTime);

        //设置成相同第日期，这时候只有时间不同了
        cNow.set(1970, 1, 1);
        cStart.set(1970, 1, 1);
        cEnd.set(1970, 1, 1);

        //开始时间比现在早， 现在比结束时间早
        return cStart.before(cNow) && cNow.before(cEnd);
    }

    public static String format(Date startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(startTime);
    }

    public static Date parse(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        try {
            return  sdf.parse(pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断a是否比b早
     * @param a
     * @param b
     * @return
     */
    public static boolean aAfterThanb(String a, String b) {
        return parse(a).after(parse(b));
    }
}
