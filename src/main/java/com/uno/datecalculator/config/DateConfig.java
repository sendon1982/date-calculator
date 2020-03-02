package com.uno.datecalculator.config;

import com.uno.datecalculator.util.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateConfig {

    @Bean
    public FormattingConversionService formattingConversionService() {
        DefaultFormattingConversionService formattingConversionService = new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT));
        registrar.registerFormatters(formattingConversionService);

        return formattingConversionService;
    }
}