package me.yingrui.segment.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String toDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(date);
    }

    public static Date toDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateTime = dateFormat.parse(date);
            return dateTime;
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
