package com.baktas.issuerbanking.utility;

import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.model.dto.AccountResDto;
import com.baktas.issuerbanking.model.dto.TransactionReqDto;
import com.baktas.issuerbanking.model.dto.TransactionResDto;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.repository.entity.Transaction;
import com.baktas.issuerbanking.utility.enums.MessageType;
import com.baktas.issuerbanking.utility.enums.Origin;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

import static com.baktas.issuerbanking.utility.Variables.BALANCE;
import static com.baktas.issuerbanking.utility.Variables.TRANSACTION_ID;

@UtilityClass
public class ModelBuilder {
    //region Account
    public static Account createAccount(Long accountId) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(BALANCE);

        return account;
    }

    public static AccountModel createAccountModel(Long accountId) {
        AccountModel accountModel = new AccountModel();
        accountModel.setId(accountId);
        accountModel.setBalance(BALANCE);

        return accountModel;
    }

    public static AccountResDto createAccountResDto() {
        return AccountResDto.builder()
                .balance(BALANCE)
                .build();
    }
    //endregion

    //region Transaction
    public static Transaction createTransaction(String transactionId, Long accountId, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setMessageType(MessageType.PAYMENT);
        transaction.setAccountId(accountId);
        transaction.setOrigin(Origin.MASTER);
        transaction.setAmount(amount);
        transaction.setRefTransactionId(StringUtils.EMPTY);

        return transaction;
    }

    public static TransactionModel createTransactionModel(String transactionId, Long accountId, BigDecimal amount) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionId(transactionId);
        transactionModel.setMessageType(MessageType.PAYMENT);
        transactionModel.setAccountId(accountId);
        transactionModel.setOrigin(Origin.MASTER);
        transactionModel.setAmount(amount);
        transactionModel.setRefTransactionId(StringUtils.EMPTY);

        return transactionModel;
    }

    public static TransactionReqDto createTransactionReqDto(String transactionId, Long accountId, BigDecimal amount) {
        return TransactionReqDto.builder()
                .transactionId(transactionId)
                .messageType(MessageType.PAYMENT)
                .accountId(accountId)
                .origin(Origin.MASTER)
                .amount(amount)
                .build();
    }

    public static TransactionResDto createTransactionResDto() {
        return TransactionResDto.builder()
                .transactionId(TRANSACTION_ID)
                .build();
    }
    //endregion
}