package com.uno.datecalculator.web.rest;

import com.uno.datecalculator.service.DateCalculationService;
import com.uno.datecalculator.util.DateUtil;
import com.uno.datecalculator.web.model.DateInfo;
import com.uno.datecalculator.web.model.ResponseStatus;
import com.uno.datecalculator.web.model.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/uno/api/date")
public class DateCalculationRestController {

    @Autowired
    private DateCalculationService dateCalculationService;

    @GetMapping(value = "/daysBetween", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<DateInfo>> getDaysBetween(
            @RequestParam @DateTimeFormat(pattern = DateUtil.DATE_FORMAT) Date fromDate,
            @RequestParam @DateTimeFormat(pattern = DateUtil.DATE_FORMAT) Date toDate) {

        DateInfo dateInfo = new DateInfo();
        long daysBetween = dateCalculationService.calculateDaysAndSave(fromDate, toDate);
        dateInfo.setDaysBetween(daysBetween);

        return new ResponseEntity<>(new ResponseWrapper<>(ResponseStatus.SUCCESS.toString(), dateInfo), HttpStatus.OK);
    }
}
