package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.exception.EntityNotFoundException;
import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.model.dto.AccountResDto;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.service.account.AccountService;
import com.baktas.issuerbanking.utility.ModelBuilder;
import com.baktas.issuerbanking.utility.base.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

import static com.baktas.issuerbanking.utility.ErrorBuilder.createEntityNotFoundException;
import static com.baktas.issuerbanking.utility.Variables.NOT_EXISTING_ACCOUNT_ID;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountControllerUnitTestWithoutMockMvc extends BaseUnitTest {
    @Mock
    private AccountService accountService;

    @InjectMocks()
    private AccountController accountController;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    public void givenAccountIdWhenBalanceThenResultIsOk() throws Exception {
        // given
        final long accountId = NOT_EXISTING_ACCOUNT_ID;
        final AccountModel accountModel = ModelBuilder.createAccountModel(accountId);
        final AccountResDto accountResDto = ModelBuilder.createAccountResDto();

        // when
        when(accountService.findById(accountId)).thenReturn(accountModel);

        final AccountResDto _accountResDto = accountController.getBalance(accountId);

        // then
        verify(accountService, times(1)).findById(longArgumentCaptor.capture());
        assertAll("Should return all true",
                () -> assertThat(longArgumentCaptor.getAllValues()).isEqualTo(Arrays.asList(accountId)),
                () -> assertThat(longArgumentCaptor.getAllValues()).hasSize(1).extracting(Long::longValue).isEqualTo(Arrays.asList(accountId)),
                () -> assertThat(_accountResDto.getBalance()).isEqualTo(accountResDto.getBalance())
        );
    }

    @Test
    public void givenNotExistingAccountIdWhenBalanceThenResultNotFoundException() throws Exception {
        // given
        final long accountId = NOT_EXISTING_ACCOUNT_ID;
        final String errorMsg = String.format(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), accountId));
        final EntityNotFoundException entityNotFoundException = createEntityNotFoundException(errorMsg);

        // when
        when(accountService.findById(accountId)).thenThrow(entityNotFoundException);

        final Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> accountController.getBalance(accountId));

        // then
        verify(accountService, times(1)).findById(longArgumentCaptor.capture());
        assertAll("Should return all true",
                () -> assertThat(longArgumentCaptor.getAllValues()).isEqualTo(Arrays.asList(accountId)),
                () -> assertThat(longArgumentCaptor.getAllValues()).hasSize(1).extracting(Long::longValue).isEqualTo(Arrays.asList(accountId)),
                () -> assertThat(((EntityNotFoundException) exception).getParams().get("message")).isEqualTo(errorMsg)
        );
    }
}