package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.model.ErrorModel;
import com.baktas.issuerbanking.model.dto.AccountResDto;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.utility.base.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.baktas.issuerbanking.utility.Variables.*;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR_CODE;
import static com.baktas.issuerbanking.utility.constants.Variables.ENTITY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenAccountIdWhenBalanceThenResultIsOk() throws Exception {
        // given
        final long accountId = ACCOUNT_ID;

        // when
        MvcResult mvcResult = mockMvc.perform(get(ACCOUNT_URL, accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(BALANCE))
                .andReturn();

        // then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String resultString = mockHttpServletResponse.getContentAsString();
        AccountResDto accountResDto = objectMapper.readValue(resultString, AccountResDto.class);
        assertEquals(BALANCE, accountResDto.getBalance());
    }

    @Test
    public void givenNotExistingAccountIdWhenBalanceThenResultNotFoundException() throws Exception {
        // given
       final long accountId = NOT_EXISTING_ACCOUNT_ID;

        // when
        MvcResult mvcResult = mockMvc.perform(get(ACCOUNT_URL, accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String resultString = mockHttpServletResponse.getContentAsString();
        ErrorModel errorModel = objectMapper.readValue(resultString, ErrorModel.class);
        assertAll("Should return all true",
                () -> assertEquals(ENTITY_NOT_FOUND_ERROR_CODE, errorModel.getCode()),
                () -> assertEquals(ENTITY_NOT_FOUND, errorModel.getReason()),
                () -> assertEquals(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), accountId), errorModel.getMessage()));
    }
}