package com.baktas.issuerbanking.service.transaction;

import com.baktas.issuerbanking.exception.EntityNotFoundException;
import com.baktas.issuerbanking.exception.payment.PaymentAmountException;
import com.baktas.issuerbanking.exception.payment.PaymentTransactionIdException;
import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.repository.TransactionRepository;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.repository.entity.Transaction;
import com.baktas.issuerbanking.service.account.AccountService;
import com.baktas.issuerbanking.utility.ModelBuilder;
import com.baktas.issuerbanking.utility.base.BaseUnitTest;
import com.baktas.issuerbanking.utility.enums.MessageType;
import com.baktas.issuerbanking.utility.enums.Origin;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.baktas.issuerbanking.utility.ErrorBuilder.*;
import static com.baktas.issuerbanking.utility.Variables.*;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionUnitTest extends BaseUnitTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void givenTransactionReqDtoForPaymentWhenTransactionResDtoThenResultIsOk() throws Exception {
        // given
        final long accountId = ACCOUNT_ID;
        final Transaction transaction =  ModelBuilder.createTransaction(TRANSACTION_ID, accountId, AMOUNT);
        final TransactionModel transactionModel = ModelBuilder.createTransactionModel(StringUtils.EMPTY, accountId, AMOUNT);
        final AccountModel accountModel = ModelBuilder.createAccountModel(accountId);

        // when
        when(accountService.findById(transactionModel.getAccountId())).thenReturn(accountModel);
        when(accountService.checkBalance(any(), any())).thenReturn(any());
        when(transactionMapper.model2Entity(transactionModel)).thenReturn(transaction);
        when(transactionService.saveOrUpdate(transactionModel)).thenReturn(transactionModel);
        when(accountService.saveOrUpdate(accountModel)).thenReturn(accountModel);

        final TransactionModel result = transactionService.makePaymentOrAdjustment(transactionModel);

        // then
        assertThat(result.getTransactionId()).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(accountId);
        assertThat(result.getMessageType()).isEqualTo(MessageType.PAYMENT);
        assertThat(result.getOrigin()).isEqualTo(Origin.MASTER);
    }

    @Test
    public void givenTransactionReqDtoWithNotExistingAccountIdWhenTransactionResDtoThenResultNotFoundException() throws Exception {
        // given
        final long accountId = NOT_EXISTING_ACCOUNT_ID;
        final String errorMsg = String.format(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), accountId));
        final EntityNotFoundException entityNotFoundException = createEntityNotFoundException(errorMsg);
        final TransactionModel transactionModel = ModelBuilder.createTransactionModel(StringUtils.EMPTY, accountId, AMOUNT);

        // when
        when(transactionService.saveOrUpdate(transactionModel)).thenThrow(entityNotFoundException);

        final Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> transactionService.saveOrUpdate(transactionModel));

        // then
        assertThat(((EntityNotFoundException) exception).getParams().get("message")).isEqualTo(errorMsg);
    }

    @Test
    public void givenTransactionReqDtoWithNotPositiveAmountWhenTransactionResDtoThenResultNotValidException() throws Exception {
        // given
        final String errorMsg = PAYMENT_AMOUNT_NOT_VALID_ERROR;
        final PaymentAmountException paymentAmountException = createPaymentAmountException(errorMsg);
        final TransactionModel transactionModel = ModelBuilder.createTransactionModel(StringUtils.EMPTY, ACCOUNT_ID, AMOUNT);

        // when
        when(transactionService.saveOrUpdate(transactionModel)).thenThrow(paymentAmountException);

        final Throwable exception = assertThrows(PaymentAmountException.class,
                () -> transactionService.saveOrUpdate(transactionModel));

        // then
        assertThat(((PaymentAmountException) exception).getParams().get("message")).isEqualTo(errorMsg);
    }

    @Test
    public void givenTransactionReqDtoWithNotEmptyTransactionIdWhenTransactionResDtoThenResultNotValidException() throws Exception {
        // given
        final String errorMsg = PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR;
        final PaymentTransactionIdException paymentTransactionIdException = createPaymentTransactionIdException(errorMsg);
        final TransactionModel transactionModel = ModelBuilder.createTransactionModel(TRANSACTION_ID, ACCOUNT_ID, AMOUNT);

        // when
        when(transactionService.saveOrUpdate(transactionModel)).thenThrow(paymentTransactionIdException);

        final Throwable exception = assertThrows(PaymentTransactionIdException.class,
                () -> transactionService.saveOrUpdate(transactionModel));

        // then
        assertThat(((PaymentTransactionIdException) exception).getParams().get("message")).isEqualTo(errorMsg);
    }
}