package com.uno.datecalculator.repository;

import com.uno.datecalculator.entity.DateCalculationInfoEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DateCalculationRepository extends CrudRepository<DateCalculationInfoEntity, String> {

}