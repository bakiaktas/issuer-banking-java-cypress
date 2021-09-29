package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.exception.ControllerAdvisor;
import com.baktas.issuerbanking.exception.EntityNotFoundException;
import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.model.ErrorModel;
import com.baktas.issuerbanking.model.dto.AccountResDto;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.service.account.AccountService;
import com.baktas.issuerbanking.utility.ModelBuilder;
import com.baktas.issuerbanking.utility.base.BaseUnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.baktas.issuerbanking.utility.ErrorBuilder.createEntityNotFoundException;
import static com.baktas.issuerbanking.utility.Variables.ACCOUNT_URL;
import static com.baktas.issuerbanking.utility.Variables.NOT_EXISTING_ACCOUNT_ID;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR_CODE;
import static com.baktas.issuerbanking.utility.constants.Variables.ENTITY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AccountControllerUnitTest extends BaseUnitTest {
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Mock
    private AccountService accountService;

    @InjectMocks()
    private AccountController accountController;

    @BeforeEach
    public void init() {
        mockMvc = standaloneSetup(accountController)
                .setControllerAdvice(new ControllerAdvisor()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenAccountIdWhenBalanceThenResultIsOk() throws Exception {
        // given
        final long accountId = NOT_EXISTING_ACCOUNT_ID;
        final AccountModel accountModel = ModelBuilder.createAccountModel(accountId);
        final AccountResDto accountResDto = ModelBuilder.createAccountResDto();

        // when
        when(accountService.findById(accountId)).thenReturn(accountModel);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNT_URL, NOT_EXISTING_ACCOUNT_ID)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(accountResDto.getBalance()));
    }

    @Test
    public void givenNotExistingAccountIdWhenBalanceThenResultNotFoundException() throws Exception {
        // given
        final long accountId = NOT_EXISTING_ACCOUNT_ID;
        final String errorMsg = String.format(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), accountId));
        final EntityNotFoundException entityNotFoundException = createEntityNotFoundException(errorMsg);

        // when
        when(accountService.findById(accountId)).thenThrow(entityNotFoundException);

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
                () -> assertEquals(errorMsg, errorModel.getMessage()));
    }
}