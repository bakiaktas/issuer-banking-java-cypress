package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.model.ErrorModel;
import com.baktas.issuerbanking.model.dto.TransactionReqDto;
import com.baktas.issuerbanking.model.dto.TransactionResDto;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.utility.ModelBuilder;
import com.baktas.issuerbanking.utility.base.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.baktas.issuerbanking.utility.Variables.*;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.*;
import static com.baktas.issuerbanking.utility.constants.Variables.ENTITY_NOT_FOUND;
import static com.baktas.issuerbanking.utility.constants.Variables.NOT_VALID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenTransactionReqDtoForPaymentWhenTransactionResDtoThenResultIsOk() throws Exception {
        // given
        TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, ACCOUNT_ID, AMOUNT);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String resultString = mockHttpServletResponse.getContentAsString();
        TransactionResDto transactionResDto = objectMapper.readValue(resultString, TransactionResDto.class);
        assertTrue(StringUtils.isNotEmpty(transactionResDto.getTransactionId()));
    }

    @Test
    public void givenTransactionReqDtoWithNotExistingAccountIdWhenTransactionResDtoThenResultNotFoundException() throws Exception {
        // given
        TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, NOT_EXISTING_ACCOUNT_ID, AMOUNT);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
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
                () -> assertEquals(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), NOT_EXISTING_ACCOUNT_ID), errorModel.getMessage()));
    }

    @Test
    public void givenTransactionReqDtoWithNotPositiveAccountIdWhenTransactionResDtoThenResultNotValidException() throws Exception {
        // given
        TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, NOT_POSITIVE_ACCOUNT_ID, AMOUNT);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String resultString = mockHttpServletResponse.getContentAsString();
        ErrorModel errorModel;

        JSONArray jsonArr = new JSONArray(resultString);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            errorModel = objectMapper.readValue(jsonObj.toString(), ErrorModel.class);

            ErrorModel finalErrorModel = errorModel;
            assertAll("Should return all true",
                    () -> assertEquals(NOT_POSITIVE_ERROR_CODE, finalErrorModel.getCode()),
                    () -> assertEquals(NOT_VALID, finalErrorModel.getReason()),
                    () -> assertEquals(String.format(NOT_POSITIVE_ERROR, "accountId"), finalErrorModel.getMessage()));
        }
    }

    @Test
    public void givenTransactionReqDtoWithNotPositiveAmountWhenTransactionResDtoThenResultNotValidException() throws Exception {
        // given
        TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, ACCOUNT_ID, NOT_POSITIVE_AMOUNT);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String resultString = mockHttpServletResponse.getContentAsString();
        ErrorModel errorModel = objectMapper.readValue(resultString, ErrorModel.class);
        assertAll("Should return all true",
                () -> assertEquals(PAYMENT_AMOUNT_NOT_VALID_ERROR_CODE, errorModel.getCode()),
                () -> assertEquals(NOT_VALID, errorModel.getReason()),
                () -> assertEquals(PAYMENT_AMOUNT_NOT_VALID_ERROR, errorModel.getMessage()));
    }

    @Test
    public void givenTransactionReqDtoWithNotEmptyTransactionIdWhenTransactionResDtoThenResultNotValidException() throws Exception {
        // given
        TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(TRANSACTION_ID, ACCOUNT_ID, AMOUNT);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        String resultString = mockHttpServletResponse.getContentAsString();
        ErrorModel errorModel = objectMapper.readValue(resultString, ErrorModel.class);
        assertAll("Should return all true",
                () -> assertEquals(PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR_CODE, errorModel.getCode()),
                () -> assertEquals(NOT_VALID, errorModel.getReason()),
                () -> assertEquals(PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR, errorModel.getMessage()));
    }
}