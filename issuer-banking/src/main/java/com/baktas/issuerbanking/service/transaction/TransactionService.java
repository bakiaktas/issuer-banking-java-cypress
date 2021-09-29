package com.baktas.issuerbanking.service.transaction;

import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.utility.base.BaseService;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService extends BaseService<TransactionModel, String> {
    TransactionModel makePaymentOrAdjustment(TransactionModel transactionModel);

    List<TransactionModel> getTransactionModelListByRefTransactionId(String refTransactionId);

    BigDecimal checkAmount(BigDecimal oldAmount, BigDecimal adjustmentAmount);

    BigDecimal getAmountSum(List<TransactionModel> transactionModels);
}