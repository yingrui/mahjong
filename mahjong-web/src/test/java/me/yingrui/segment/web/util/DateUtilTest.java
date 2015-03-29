package me.yingrui.segment.web.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static me.yingrui.segment.web.util.DateUtil.toDateString;
import static me.yingrui.segment.web.util.DateUtil.toDateTime;

public class DateUtilTest {

    @Test
    public void should_convert_date_time_to_date_string() {
        Date today = new Date(0);
        String todayString = toDateString(today);
        assertEquals("1970-01-01", todayString);
    }

    @Test
    public void should_convert_date_string_to_date_time() {
        Date today = toDateTime("1970-01-01");
        assertEquals("1970-01-01", toDateString(today));
    }
}
