package com.uno.datecalculator.web.rest;

import com.uno.datecalculator.service.DateCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        MvcResult mvcResult = mockMvc.perform(get(endpointUrl)).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content, containsString("daysBetween"));
    }
}