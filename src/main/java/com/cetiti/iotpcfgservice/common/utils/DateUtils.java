package com.cetiti.iotpcfgservice.common.utils;

import com.google.common.base.Preconditions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 得到某天的23点59分59秒的unix时间。
     *
     * @param date
     * @return
     */
    public static long getDayEnd(Date date) {
        if (date == null) {
            date = new Date();
        }

        String string = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Date dayEnd = null;
        try {
            dayEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string + " 23:59:59");
        } catch (ParseException e) {
            return 0;
        }

        return dayEnd.getTime() / 1000;
    }

    /**
     * 得到今天的23点59分59秒999毫秒的Date。
     *
     * @return
     */
    public static Date todayLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        todayLastDate();
    }

    /**
     * 得到某天的00点00分00秒的unix时间。
     *
     * @param date
     * @return
     */
    public static long getDayStart(Date date) {
        if (date == null) {
            date = new Date();
        }

        String string = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Date dayEnd = null;
        try {
            dayEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string + " 00:00:00");
        } catch (ParseException e) {
            return 0;
        }

        return dayEnd.getTime() / 1000;
    }

    /**
     * 两个日期相差的天数。
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        Preconditions.checkArgument(date1 != null);
        Preconditions.checkArgument(date2 != null);

        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
