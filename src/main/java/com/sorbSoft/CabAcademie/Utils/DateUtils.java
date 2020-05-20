package com.sorbSoft.CabAcademie.Utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


public class DateUtils {

    public static Date removeSeconds(Date date) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static boolean isOverlapped(DateTime start1, DateTime end1, DateTime start2, DateTime end2) {
        Interval interval = new Interval( start1, end1 );
        Interval interval2 = new Interval( start2, end2 );
        return interval.overlaps( interval2 );
    }
}
