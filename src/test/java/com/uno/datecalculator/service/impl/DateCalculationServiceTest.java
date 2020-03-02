package com.uno.datecalculator.service.impl;

import com.uno.datecalculator.entity.DateCalculationInfoEntity;
import com.uno.datecalculator.repository.DateCalculationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.util.AssertionErrors.fail;

class DateCalculationServiceTest {

    private DateCalculationServiceImpl service;

    @Mock
    private DateCalculationRepository dateCalculationRepository;

    private final Calendar fromCalendar = Calendar.getInstance();
    private final Calendar toCalendar = Calendar.getInstance();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new DateCalculationServiceImpl();
        ReflectionTestUtils.setField(service, "dateCalculationRepository", dateCalculationRepository);
    }

    @Test
    void test_calculateDaysBetweenDates_FromDateNull() {
        try {
            service.calculateDaysBetweenDates(null, toCalendar.getTime());
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("Input param cannot be null"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_toDateNull() {
        try {
            service.calculateDaysBetweenDates(fromCalendar.getTime(), null);
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("Input param cannot be null"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_FromDateAndToDateBothNull() {
        try {
            service.calculateDaysBetweenDates(null, null);
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("Input param cannot be null"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_FromDateLessThan_1900_01_01() {
        fromCalendar.set(1900, Calendar.JANUARY, 1);
        toCalendar.set(2020, Calendar.JANUARY, 1);

        try {
            service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("Both dates will be greater than 01.01.1900"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_ToDateLessThan_1900_01_01() {
        fromCalendar.set(2020, Calendar.JANUARY, 1);
        toCalendar.set(1800, Calendar.JANUARY, 1);

        try {
            service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("Both dates will be greater than 01.01.1900"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_BothFromDateAndToDateLessThan_1900_01_01() {
        fromCalendar.set(1800, Calendar.JANUARY, 1);
        toCalendar.set(1899, Calendar.JANUARY, 1);

        try {
            service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("Both dates will be greater than 01.01.1900"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_FromDateLargeThanToDate() {
        fromCalendar.set(2001, Calendar.FEBRUARY, 1);
        toCalendar.set(2001, Calendar.JANUARY, 1);

        try {
            service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
            fail("Should not come here!");
        } catch (IllegalArgumentException e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), containsString("fromDate must be less or equal than toDate"));
        }
    }

    @Test
    void test_calculateDaysBetweenDates_SameDate() {
        fromCalendar.set(2020, Calendar.JANUARY, 1);
        toCalendar.set(2020, Calendar.JANUARY, 1);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(0L));
    }

    @Test
    void test_calculateDaysBetweenDates_SameYear_ZeroDaysBetween() {
        fromCalendar.set(2020, Calendar.JANUARY, 1);
        toCalendar.set(2020, Calendar.JANUARY, 2);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(0L));
    }

    @Test
    void test_calculateDaysBetweenDates_SameYear_OnlyDayBetween() {
        fromCalendar.set(2020, Calendar.JANUARY, 1);
        toCalendar.set(2020, Calendar.JANUARY, 3);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(1L));
    }

    @Test
    void test_calculateDaysBetweenDates_SameYear_LeapYearWithFebruary() {
        fromCalendar.set(2020, Calendar.JANUARY, 1);
        toCalendar.set(2020, Calendar.MARCH, 3);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(61L));
    }

    @Test
    void test_daysBetween_DifferentYears_SameMonthAndDate() {
        fromCalendar.set(2020, Calendar.JANUARY, 1);
        toCalendar.set(2030, Calendar.JANUARY, 1);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(3652L));
    }

    @Test
    void test_daysBetween_DifferentYearsAndMonthAndDate() {
        fromCalendar.set(1998, Calendar.MARCH, 28);
        toCalendar.set(2000, Calendar.DECEMBER, 23);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(1000L));
    }

    @Test
    void test_daysBetween_HugeDifferentYears_PastAndFuture() {
        fromCalendar.set(1900, Calendar.JANUARY, 2);
        toCalendar.set(9999, Calendar.JANUARY, 1);

        long daysBetweenDates = service.calculateDaysBetweenDates(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(2958097L));
    }

    @Test
    void test_calculateDaysAndSave_DifferentYearsAndMonthAndDate() {
        Mockito.when(dateCalculationRepository.save(Mockito.any(DateCalculationInfoEntity.class))).thenReturn(null);

        fromCalendar.set(1998, Calendar.MARCH, 28);
        toCalendar.set(2000, Calendar.DECEMBER, 23);

        long daysBetweenDates = service.calculateDaysAndSave(fromCalendar.getTime(), toCalendar.getTime());
        assertThat(daysBetweenDates, is(1000L));

        Mockito.verify(dateCalculationRepository).save(MockitoHamcrest.argThat(allOf(
                hasProperty("fromDate", equalTo("28.03.1998")),
                hasProperty("toDate", equalTo("23.12.2000")),
                hasProperty("daysBetween", equalTo(1000L)),
                hasProperty("requestDateTime", notNullValue())
        )));

        Mockito.verifyNoMoreInteractions(dateCalculationRepository);
    }

    @Test
    void test_daysToEndOfYear_LeapYearWithFeb() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.FEBRUARY, 21);

        int daysToEndOfYear = service.getDaysToEndOfYear(calendar);
        assertThat(daysToEndOfYear, is(314));
    }

    @Test
    void test_daysToEndOfYear_LeapYearNoFeb() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.APRIL, 15);

        int daysToEndOfYear = service.getDaysToEndOfYear(calendar);
        assertThat(daysToEndOfYear, is(260));
    }

    @Test
    void test_daysToEndOfYear_NotLeapYearWithFeb() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.FEBRUARY, 15);

        int daysToEndOfYear = service.getDaysToEndOfYear(calendar);
        assertThat(daysToEndOfYear, is(319));
    }

    @Test
    void test_getDaysFromStartOfYear_LeapYearWithFeb() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.MARCH, 21);

        int daysToEndOfYear = service.getDaysFromStartOfYear(calendar);
        assertThat(daysToEndOfYear, is(80));
    }

    @Test
    void test_getDaysFromStartOfYear_LeapYearNoFeb() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JANUARY, 15);

        int daysToEndOfYear = service.getDaysFromStartOfYear(calendar);
        assertThat(daysToEndOfYear, is(14));
    }

    @Test
    void test_getDaysFromStartOfYear_NotLeapYearWithFeb() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.FEBRUARY, 15);

        int daysToEndOfYear = service.getDaysFromStartOfYear(calendar);
        assertThat(daysToEndOfYear, is(45));
    }
}