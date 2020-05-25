package com.sorbSoft.CabAcademie.Utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;

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

    public static Date fromUtcToTimeZoned(Date date, String timeZone) {
        DateTime dateTime = new DateTime(date);
        Date calculated = fromUtcToTimeZoned(dateTime, timeZone).toDate();
        date.setTime(calculated.getTime());
        return date;
    }

    public static DateTime fromUtcToTimeZoned(DateTime date, String timeZone) {

        if(timeZone.equals("0:00") || timeZone.equals("00:00")) {
            return date;
        } else {
            if(timeZone.contains("+")){
                timeZone = timeZone.replace("+", "");
                String hrsMinutes[] = timeZone.split(":");
                String hrsStr = hrsMinutes[0];
                String minutesStr = hrsMinutes[1];

                Integer hourShift = Integer.parseInt(hrsStr);
                Integer minutesShift = Integer.parseInt(minutesStr);

                date = date.plusHours(hourShift);
                date = date.plusMinutes(minutesShift);
            }

            if(timeZone.contains("-")){
                timeZone = timeZone.replace("-", "");

                String hrsMinutes[] = timeZone.split(":");
                String hrsStr = hrsMinutes[0];
                String minutesStr = hrsMinutes[1];

                Integer hourShift = Integer.parseInt(hrsStr);
                Integer minutesShift = Integer.parseInt(minutesStr);

                date = date.minusHours(hourShift);
                date = date.minusMinutes(minutesShift);
            }
        }

        return date;
    }

    public static Date fromTimeZonedToUtc(Date date, String timeZone) {
        DateTime dateTime = new DateTime(date);
        Date calculated = fromTimeZonedToUtc(dateTime, timeZone).toDate();
        date.setTime(calculated.getTime());
        return date;
    }

    public static DateTime fromTimeZonedToUtc(DateTime date, String timeZone) {

        if(timeZone.equals("0:00") || timeZone.equals("00:00")) {
            return date;
        } else {
            if(timeZone.contains("+")){
                timeZone = timeZone.replace("+", "");
                String hrsMinutes[] = timeZone.split(":");
                String hrsStr = hrsMinutes[0];
                String minutesStr = hrsMinutes[1];

                Integer hourShift = Integer.parseInt(hrsStr);
                Integer minutesShift = Integer.parseInt(minutesStr);

                date = date.minusHours(hourShift);
                date = date.minusMinutes(minutesShift);

            }

            if(timeZone.contains("-")){
                timeZone = timeZone.replace("-", "");

                String hrsMinutes[] = timeZone.split(":");
                String hrsStr = hrsMinutes[0];
                String minutesStr = hrsMinutes[1];

                Integer hourShift = Integer.parseInt(hrsStr);
                Integer minutesShift = Integer.parseInt(minutesStr);

                date = date.plusHours(hourShift);
                date = date.plusMinutes(minutesShift);
            }
        }

        return date;
    }
}
