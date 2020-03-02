package com.uno.datecalculator.web.rest;

import com.uno.datecalculator.config.DateConfig;
import com.uno.datecalculator.config.DynamoDBConfig;
import com.uno.datecalculator.service.DateCalculationService;
import com.uno.datecalculator.web.model.DateInfo;
import com.uno.datecalculator.web.model.ResponseStatus;
import com.uno.datecalculator.web.model.ResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class DateCalculationRestControllerTest {

    @Mock
    private DateCalculationService dateCalculationService;

    @InjectMocks
    private DateCalculationRestController dateCalculationRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(dateCalculationRestController, "dateCalculationService", dateCalculationService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(dateCalculationRestController).build();

        Mockito.when(dateCalculationService.calculateDaysAndSave(
                Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(1L);
    }

    @Test
    void test_getDaysBetween_success() throws Exception {
        String endpointUrl = String.format("http://localhost:8080/uno/api/date/daysBetween?fromDate=%s&toDate=%s", "01.01.2010", "03.01.2010");

        mockMvc.perform(get(endpointUrl)).andExpect(status().isOk());

//        ResponseEntity<ResponseWrapper<DateInfo>> response = restTemplate.exchange(
//                endpointUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<ResponseWrapper<DateInfo>>(){});
//        ResponseWrapper<DateInfo> responseWrapper = response.getBody();
//
//        assertThat(responseWrapper.getData().getDaysBetween(), equalTo(1L));
//        assertThat(responseWrapper.getMessage(), nullValue());
//        assertThat(responseWrapper.getStatus(), equalTo(ResponseStatus.SUCCESS.toString()));
    }
}