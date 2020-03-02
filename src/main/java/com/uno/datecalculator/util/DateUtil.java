package com.uno.datecalculator.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    /**
     * Check if two dates are in the same year
     *
     * @param fromCalendar  beginning of the date
     *
     * @param toCalendar    end of the date
     *
     * @return              return true if two dates belong to the same year
     */
    public static boolean isSameYear(Calendar fromCalendar, Calendar toCalendar) {
        return fromCalendar.get(Calendar.YEAR) == toCalendar.get(Calendar.YEAR);
    }

    /**
     * Check if two dates are in the same day
     *
     * @param fromCalendar  beginning of the date
     *
     * @param toCalendar    end of the date
     *
     * @return              return true if two dates belong to exact same day
     */
    public static boolean isSameDate(Calendar fromCalendar, Calendar toCalendar) {
        return (fromCalendar.get(Calendar.YEAR) == toCalendar.get(Calendar.YEAR))
                && (fromCalendar.get(Calendar.MONTH) == toCalendar.get(Calendar.MONTH))
                && (fromCalendar.get(Calendar.DATE) == toCalendar.get(Calendar.DATE));
    }

    /**
     * Check if the year is a leap year
     *
     * @param year
     * @return      return true if year is a leap year
     */
    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    /**
     * Check if the given month is February
     *
     * @param month
     * @return      return true if month is FEBRUARY
     */
    public static boolean isFebruary(int month) {
        return month == Calendar.FEBRUARY;
    }

    /**
     * Return a string respresentive of a date in format {@link #DATE_FORMAT}
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }
}
