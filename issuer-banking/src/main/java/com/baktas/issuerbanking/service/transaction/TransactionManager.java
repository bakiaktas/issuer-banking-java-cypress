package com.baktas.issuerbanking.service.transaction;

import com.baktas.issuerbanking.exception.DomainException;
import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.service.account.AccountService;
import com.baktas.issuerbanking.utility.UtilityManager;
import com.baktas.issuerbanking.utility.enums.MessageType;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.*;
import static com.baktas.issuerbanking.utility.constants.Variables.ZERO;

@UtilityClass
public class TransactionManager {
    public TransactionModel makePayment(TransactionModel transactionModel, TransactionService transactionService, AccountService accountService) {
        if (transactionModel.getAmount().compareTo(BigDecimal.ZERO) <= ZERO) {
            throw DomainException.paymentAmountNotValid(PAYMENT_AMOUNT_NOT_VALID_ERROR).get();
        }

        if (!ObjectUtils.isEmpty(transactionModel.getTransactionId())) {
            throw DomainException.paymentTransactionIdNotValid(PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR).get();
        }

        AccountModel accountModel = accountService.findById(transactionModel.getAccountId());
        final BigDecimal amount = transactionModel.getAmount();
        final BigDecimal balance = accountModel.getBalance();
        final BigDecimal commission = UtilityManager.percentage(amount, transactionModel.getOrigin().getCommissionRate());
        final BigDecimal calculatedAmount = amount.add(commission);

        accountService.checkBalance(balance, calculatedAmount);

        final BigDecimal formattedAmount = UtilityManager.formatAmount(calculatedAmount);

        transactionModel.setTransactionId(UUID.randomUUID().toString());
        transactionModel.setAmount(formattedAmount);
        transactionService.saveOrUpdate(transactionModel);

        if (!ObjectUtils.isEmpty(transactionModel.getTransactionId())) {
            BigDecimal updatedBalance = balance.subtract(formattedAmount);
            accountModel.setBalance(updatedBalance);
            accountService.saveOrUpdate(accountModel);
        }

        return transactionModel;
    }

    public TransactionModel makeAdjustment(TransactionModel transactionModel, TransactionService transactionService, AccountService accountService) {
        if (ObjectUtils.isEmpty(transactionModel.getTransactionId())) {
            throw DomainException.adjustmentTransactionNotValid(ADJUSTMENT_TRANSACTION_ID_NOT_VALID_ERROR).get();
        }

        final String transactionId = transactionModel.getTransactionId();
        final TransactionModel existingTransactionModel = transactionService.findById(transactionId);
        final BigDecimal adjustedAmount = transactionModel.getAmount();

        AccountModel accountModel = accountService.findById(transactionModel.getAccountId());
        final BigDecimal balance = accountModel.getBalance();
        final BigDecimal commission = UtilityManager.percentage(adjustedAmount, existingTransactionModel.getOrigin().getCommissionRate());
        final BigDecimal calculatedAmount = adjustedAmount.add(commission);

        if (adjustedAmount.compareTo(BigDecimal.ZERO) > ZERO) {
            accountService.checkBalance(balance, calculatedAmount);
        } else if (adjustedAmount.compareTo(BigDecimal.ZERO) < ZERO) {
            final BigDecimal existingAmount = existingTransactionModel.getAmount();
            final List<TransactionModel> adjustedTransactionListModel = transactionService.getTransactionModelListByRefTransactionId(transactionId);
            final BigDecimal adjustedAmountSum = transactionService.getAmountSum(adjustedTransactionListModel);
            final BigDecimal oldAmount = existingAmount.add(adjustedAmountSum);
            transactionService.checkAmount(oldAmount, calculatedAmount);
        } else {
            throw DomainException.adjustmentAmountNotValid(ADJUSTMENT_AMOUNT_NOT_VALID_ERROR).get();
        }

        final BigDecimal formattedAmount = UtilityManager.formatAmount(calculatedAmount);

        TransactionModel newTransactionModel = new TransactionModel(
                UUID.randomUUID().toString(), MessageType.ADJUSTMENT,
                existingTransactionModel.getAccountId(), existingTransactionModel.getOrigin(),
                formattedAmount, existingTransactionModel.getTransactionId());

        transactionService.saveOrUpdate(newTransactionModel);

        if (!ObjectUtils.isEmpty(newTransactionModel.getTransactionId())) {
            BigDecimal updatedBalance = balance.subtract(formattedAmount);
            accountModel.setBalance(updatedBalance);
            accountService.saveOrUpdate(accountModel);
        }

        return newTransactionModel;
    }
}