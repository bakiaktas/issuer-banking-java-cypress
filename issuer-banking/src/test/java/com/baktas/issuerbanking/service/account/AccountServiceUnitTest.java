package com.baktas.issuerbanking.service.account;

import com.baktas.issuerbanking.exception.EntityNotFoundException;
import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.repository.AccountRepository;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.utility.ModelBuilder;
import com.baktas.issuerbanking.utility.base.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static com.baktas.issuerbanking.utility.Variables.*;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceUnitTest extends BaseUnitTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    public void givenAccountIdWhenFindByIdThenResultIsAccountModel() {
        // given
        long accountId = ACCOUNT_ID;

        // when
        final Account account = ModelBuilder.createAccount(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        final AccountModel accountModel = accountService.findById(accountId);

        // then
        verify(accountRepository, times(1)).findById(longArgumentCaptor.capture());
        assertAll("Should return all true",
                () -> assertThat(longArgumentCaptor.getAllValues()).isEqualTo(Arrays.asList(accountId)),
                () -> assertThat(longArgumentCaptor.getAllValues()).hasSize(1).extracting(Long::longValue).isEqualTo(Arrays.asList(accountId))
        );

        assertThat(accountModel.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountModel.getBalance()).isEqualTo(BALANCE);
    }

    @Test
    public void givenNotExistingAccountIdWhenBalanceThenResultNotFoundException() throws Exception {
        // given
        long accountId = NOT_EXISTING_ACCOUNT_ID;

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        final Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> accountService.findById(accountId));

        // then
        verify(accountRepository, times(1)).findById(longArgumentCaptor.capture());
        assertAll("Should return all true",
                () -> assertThat(longArgumentCaptor.getAllValues()).isEqualTo(Arrays.asList(accountId)),
                () -> assertThat(longArgumentCaptor.getAllValues()).hasSize(1).extracting(Long::longValue).isEqualTo(Arrays.asList(accountId))
        );

        assertThat(((EntityNotFoundException)exception).getParams().get("message")).isEqualTo(String.format(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), accountId)));
    }
}