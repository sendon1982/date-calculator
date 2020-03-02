package com.uno.datecalculator.service;

import java.util.Date;

public interface DateCalculationService {

    /**
     * Calculate days between two dates exclude fromDate and toDate.
     *
     * @param fromDate  beginning of the date
     * @param toDate    end of the date
     *
     * @return  days between two dates
     */
    public long calculateDaysBetweenDates(Date fromDate, Date toDate);

    /**
     * Calculate days between two dates exclude fromDate and toDate and save result into Amazon DynamoDB
     *
     * @param fromDate  beginning of the date
     * @param toDate    end of the date
     *
     * @return  days between two dates
     */
    public long calculateDaysAndSave(Date fromDate, Date toDate);
}
