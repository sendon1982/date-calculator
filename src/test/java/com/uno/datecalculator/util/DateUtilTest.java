package com.uno.datecalculator.util;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class DateUtilTest {

    private final Calendar fromCalendar = Calendar.getInstance();
    private final Calendar toCalendar = Calendar.getInstance();

    @Test
    void test_isSameYear_True() {
        fromCalendar.set(2020, Calendar.FEBRUARY, 2);
        toCalendar.set(2020, Calendar.JANUARY, 22);

        boolean isSameYear = DateUtil.isSameYear(fromCalendar, toCalendar);
        assertThat(isSameYear, is(true));
    }

    @Test
    void test_isSameYear_False() {
        fromCalendar.set(3333, Calendar.FEBRUARY, 2);
        toCalendar.set(2020, Calendar.JANUARY, 22);

        boolean isSameYear = DateUtil.isSameYear(fromCalendar, toCalendar);
        assertThat(isSameYear, is(false));
    }

    @Test
    void test_isSameDate_SameYearOnly() {
        fromCalendar.set(2020, Calendar.FEBRUARY, 22);
        toCalendar.set(2020, Calendar.APRIL, 12);

        boolean isSameDate = DateUtil.isSameDate(fromCalendar, toCalendar);
        assertThat(isSameDate, is(false));
    }

    @Test
    void test_isSameDate_SameYearMonthOnly() {
        fromCalendar.set(2020, Calendar.FEBRUARY, 22);
        toCalendar.set(2020, Calendar.FEBRUARY, 11);

        boolean isSameDate = DateUtil.isSameDate(fromCalendar, toCalendar);
        assertThat(isSameDate, is(false));
    }

    @Test
    void test_isSameDate_SameDate() {
        fromCalendar.set(2020, Calendar.FEBRUARY, 22);
        toCalendar.set(2020, Calendar.FEBRUARY, 22);

        boolean isSameDate = DateUtil.isSameDate(fromCalendar, toCalendar);
        assertThat(isSameDate, is(true));
    }

    @Test
    void test_isLeapYear_DividedBy100_False() {
        boolean isLeapYear = DateUtil.isLeapYear(1800);
        assertThat(isLeapYear, is(false));
    }

    @Test
    void test_isLeapYear_DividedBy400_True() {
        boolean isLeapYear = DateUtil.isLeapYear(2000);
        assertThat(isLeapYear, is(true));
    }

    @Test
    void test_isLeapYear_DividedBy4Not100_True() {
        boolean isLeapYear = DateUtil.isLeapYear(2004);
        assertThat(isLeapYear, is(true));
    }

    @Test
    void test_isFebruary_False() {
        boolean isFebruary = DateUtil.isFebruary(Calendar.MARCH);
        assertThat(isFebruary, is(false));
    }

    @Test
    void test_isFebruary_True() {
        boolean isFebruary = DateUtil.isFebruary(Calendar.FEBRUARY);
        assertThat(isFebruary, is(true));
    }

    @Test
    void test_formatDate() {
        fromCalendar.set(2020, Calendar.FEBRUARY, 22);
        String stringDate = DateUtil.formatDate(fromCalendar.getTime());
        assertThat(stringDate, equalTo("22.02.2020"));
    }
}