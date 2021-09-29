package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.exception.ControllerAdvisor;
import com.baktas.issuerbanking.exception.EntityNotFoundException;
import com.baktas.issuerbanking.exception.payment.PaymentAmountException;
import com.baktas.issuerbanking.exception.payment.PaymentTransactionIdException;
import com.baktas.issuerbanking.model.ErrorModel;
import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.model.dto.TransactionReqDto;
import com.baktas.issuerbanking.model.dto.TransactionResDto;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.service.transaction.TransactionService;
import com.baktas.issuerbanking.utility.ModelBuilder;
import com.baktas.issuerbanking.utility.base.BaseUnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.baktas.issuerbanking.utility.ErrorBuilder.*;
import static com.baktas.issuerbanking.utility.Variables.*;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.*;
import static com.baktas.issuerbanking.utility.constants.Variables.ENTITY_NOT_FOUND;
import static com.baktas.issuerbanking.utility.constants.Variables.NOT_VALID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class TransactionControllerUnitTest extends BaseUnitTest {
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks()
    private TransactionController transactionController;

    @BeforeEach
    public void init() {
        mockMvc = standaloneSetup(transactionController)
                .setControllerAdvice(new ControllerAdvisor()).build();
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Test
    public void givenTransactionReqDtoForPaymentWhenTransactionResDtoThenResultIsOk() throws Exception {
        // given
        long accountId = ACCOUNT_ID;
        final TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, accountId, AMOUNT);
        final TransactionModel transactionModel = ModelBuilder.createTransactionModel(TRANSACTION_ID, accountId, AMOUNT);
        final TransactionResDto transactionResDto = ModelBuilder.createTransactionResDto();

        // when
        when(transactionService.makePaymentOrAdjustment(any())).thenReturn(transactionModel);

        // then
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);
        mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(transactionResDto.getTransactionId()));
    }

    @Test
    public void givenTransactionReqDtoWithNotExistingAccountIdWhenTransactionResDtoThenResultNotFoundException() throws Exception {
        // given
        long accountId = NOT_EXISTING_ACCOUNT_ID;
        final TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, accountId, AMOUNT);
        final String errorMsg = String.format(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), accountId));
        final EntityNotFoundException entityNotFoundException = createEntityNotFoundException(errorMsg);

        // when
        when(transactionService.makePaymentOrAdjustment(any())).thenThrow(entityNotFoundException);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(APPLICATION_JSON_VALUE)
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
                () -> assertEquals(errorMsg, errorModel.getMessage()));
    }

    @Test
    public void givenTransactionReqDtoWithNotPositiveAmountWhenTransactionResDtoThenResultNotValidException() throws Exception {
        // given
        final TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(StringUtils.EMPTY, ACCOUNT_ID, NOT_POSITIVE_AMOUNT);
        final String errorMsg = PAYMENT_AMOUNT_NOT_VALID_ERROR;
        final PaymentAmountException paymentAmountException = createPaymentAmountException(errorMsg);

        // when
        when(transactionService.makePaymentOrAdjustment(any())).thenThrow(paymentAmountException);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(APPLICATION_JSON_VALUE)
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
        final TransactionReqDto transactionReqDto = ModelBuilder.createTransactionReqDto(TRANSACTION_ID, ACCOUNT_ID, NOT_POSITIVE_AMOUNT);
        final String errorMsg = PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR;
        final PaymentTransactionIdException paymentTransactionIdException = createPaymentTransactionIdException(errorMsg);

        // when
        when(transactionService.makePaymentOrAdjustment(any())).thenThrow(paymentTransactionIdException);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String transactionReqDtoJson = ow.writeValueAsString(transactionReqDto);
        MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
                        .content(transactionReqDtoJson)
                        .contentType(APPLICATION_JSON_VALUE)
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