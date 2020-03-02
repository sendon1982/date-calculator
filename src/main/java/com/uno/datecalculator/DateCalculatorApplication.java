package com.uno.datecalculator;

import com.uno.datecalculator.config.AppConfig;
import com.uno.datecalculator.config.DateConfig;
import com.uno.datecalculator.config.DynamoDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class DateCalculatorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DateCalculatorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
