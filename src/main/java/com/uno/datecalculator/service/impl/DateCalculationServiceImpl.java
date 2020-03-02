package com.uno.datecalculator.service.impl;

import com.uno.datecalculator.entity.DateCalculationInfoEntity;
import com.uno.datecalculator.repository.DateCalculationRepository;
import com.uno.datecalculator.service.DateCalculationService;
import com.uno.datecalculator.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Service("dateCalculationService")
public class DateCalculationServiceImpl implements DateCalculationService {

    private final static Logger logger = LoggerFactory.getLogger(DateCalculationServiceImpl.class);

    // Days in each month and add one more day for February in a leap year
    private static final int[] daysInMonthArray = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // Days in each year and add one more day if in a leap year
    private static final int DAYS_IN_YEAR = 365;

    private final Calendar fromCalendar = Calendar.getInstance();
    private final Calendar toCalendar = Calendar.getInstance();

    private final Calendar allowedCalendar = Calendar.getInstance();

    @Autowired
    private DateCalculationRepository dateCalculationRepository;

    /**
     * Calculate days between two dates exclude fromDate and toDate.
     *
     * If two dates are in the same year, then calculate days from "fromDate" to the end of year and
     * days from "toDate" to the end of year. After that, subtract them and return the result
     *
     * If If two dates are NOT in the same year, then calculate days from "fromDate" to the end of year and
     * days from the beginning of year to "toDate". After that, calculate days between years of "fromDate" and
     * "toDate". Later, add these three days together and return the result.
     *
     * @param fromDate  beginning of the date
     * @param toDate    end of the date
     *
     * @return  days between two dates
     */
    @Override
    public long calculateDaysBetweenDates(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Input param cannot be null");
        }

        fromCalendar.setTime(fromDate);
        toCalendar.setTime(toDate);

        if (isBeforeAllowedDate(fromCalendar) || isBeforeAllowedDate(toCalendar)) {
            throw new IllegalArgumentException("Both dates will be greater than 01.01.1900");
        }

        if (fromCalendar.after(toCalendar)) {
            throw new IllegalArgumentException("fromDate must be less or equal than toDate");
        }

        logger.info("calculate days between dates [{}, {}]", DateUtil.formatDate(fromDate), DateUtil.formatDate(toDate));

        if (DateUtil.isSameDate(fromCalendar, toCalendar)) {
            return 0L;
        }

        int daysBetween = 0;

        if (DateUtil.isSameYear(fromCalendar, toCalendar)) {
            // same year
            int daysFromStartDateToEndOfYear = getDaysToEndOfYear(fromCalendar);
            int daysFromEndDateToEndOfYear = getDaysToEndOfYear(toCalendar);
            daysBetween = daysFromStartDateToEndOfYear - daysFromEndDateToEndOfYear - 1;
        } else {
            // different years
            int daysFromStartDateToEndOfYear = getDaysToEndOfYear(fromCalendar);
            int daysFromBeginningOfYearToEndDate = getDaysFromStartOfYear(toCalendar);
            int daysBetweenYears = getDaysBetweenYears(fromCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.YEAR));

            daysBetween = daysFromStartDateToEndOfYear + daysBetweenYears + daysFromBeginningOfYearToEndDate;
        }

        logger.info("Days between [{}] and [{}] is {}", DateUtil.formatDate(fromDate), DateUtil.formatDate(toDate), daysBetween);

        return daysBetween;
    }

    @Override
    public long calculateDaysAndSave(Date fromDate, Date toDate) {
        long daysBetweenDates = this.calculateDaysBetweenDates(fromDate, toDate);

        // Save into Amazon DynamoDB
        DateCalculationInfoEntity dateCalculationInfo = buildDateCalculationInfoEntity(fromDate, toDate, daysBetweenDates);

        logger.info("Save result into DynamoDB for {}", dateCalculationInfo);
        dateCalculationRepository.save(dateCalculationInfo);

        return daysBetweenDates;
    }

    /**
     * Return true if incoming date is before allowed date
     *
     * @param calendar
     * @return
     */
    private boolean isBeforeAllowedDate(Calendar calendar) {
        allowedCalendar.set(1900, Calendar.JANUARY, 1);
        return !calendar.after(allowedCalendar);
    }

    /**
     * Get days from startCalendar to the end of year which is Dec 31st
     *
     * @param startCalendar
     *
     * @return      days from startCalendar to the end of year
     */
    int getDaysToEndOfYear(Calendar startCalendar) {
        int daysBetween = 0;

        int month = startCalendar.get(Calendar.MONTH);
        int daysInMonth = daysInMonthArray[month];
        if (shouldPlusOneMoreDay(startCalendar.get(Calendar.YEAR), month)) {
            daysInMonth = daysInMonth + 1;
        }

        // days left in the current month
        daysBetween = daysInMonth - startCalendar.get(Calendar.DATE);

        // days left to the end of the year
        for (int monthIndex = month + 1; monthIndex < daysInMonthArray.length; monthIndex++) {
            daysBetween = daysBetween + daysInMonthArray[monthIndex];

            if (shouldPlusOneMoreDay(startCalendar.get(Calendar.YEAR), monthIndex)) {
                daysBetween = daysBetween + 1;
            }
        }

        logger.info("Days from [{}] to the end of year is {}", DateUtil.formatDate(startCalendar.getTime()), daysBetween);

        return daysBetween;
    }

    /**
     * Get days from Jan 1st to the endCalendar
     *
     * @param endCalendar
     * @return      days from the begnning of the year to the end of endCalendar
     */
    int getDaysFromStartOfYear(Calendar endCalendar) {
        int daysBetween = 0;

        int month = endCalendar.get(Calendar.MONTH);
        daysBetween = endCalendar.get(Calendar.DATE) - 1;

        for (int monthIndex = 0; monthIndex < month; monthIndex++) {
            daysBetween = daysBetween + daysInMonthArray[monthIndex];

            if (shouldPlusOneMoreDay(endCalendar.get(Calendar.YEAR), monthIndex)) {
                daysBetween++;
            }
        }

        logger.info("Days from the beginning of the year to [{}] is {}", DateUtil.formatDate(endCalendar.getTime()), daysBetween);

        return daysBetween;
    }

    private int getDaysBetweenYears(int fromYear, int toYear) {
        int daysBetweenYears = 0;

        for (int year = fromYear + 1; year < toYear; year++) {
            daysBetweenYears = daysBetweenYears + DAYS_IN_YEAR;

            if (DateUtil.isLeapYear(year)) {
                daysBetweenYears++;
            }
        }

        logger.info("Days from year [{}] to year [{}] is {}", fromYear, toYear, daysBetweenYears);

        return daysBetweenYears;
    }

    /**
     * Should add one more day or not.
     * Return true if month is FEBRUARY and year is a leap year.
     *
     * @param year
     *
     * @param month
     *
     * @return      return true if month is FEBRUARY and year is a leap year
     */
    private boolean shouldPlusOneMoreDay(int year, int month) {
        return DateUtil.isLeapYear(year) && DateUtil.isFebruary(month);
    }

    private DateCalculationInfoEntity buildDateCalculationInfoEntity(Date fromDate, Date toDate, long daysBetween) {
        DateCalculationInfoEntity dateCalculationInfo = new DateCalculationInfoEntity();

        dateCalculationInfo.setFromDate(DateUtil.formatDate(fromDate));
        dateCalculationInfo.setToDate(DateUtil.formatDate(toDate));
        dateCalculationInfo.setDaysBetween(daysBetween);
        dateCalculationInfo.setRequestDateTime(LocalDateTime.now().toString());

        return dateCalculationInfo;
    }
}
