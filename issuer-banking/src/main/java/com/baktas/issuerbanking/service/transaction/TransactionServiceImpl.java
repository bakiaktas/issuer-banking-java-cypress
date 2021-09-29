package com.baktas.issuerbanking.service.transaction;

import com.baktas.issuerbanking.exception.DomainException;
import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.repository.TransactionRepository;
import com.baktas.issuerbanking.repository.entity.Transaction;
import com.baktas.issuerbanking.service.account.AccountService;
import com.baktas.issuerbanking.utility.base.BaseMapperManager;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ADJUSTMENT_AMOUNT_FOR_PAYMENT_NOT_VALID_ERROR;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR;
import static com.baktas.issuerbanking.utility.constants.Variables.ZERO;

@Service
public class TransactionServiceImpl extends BaseMapperManager<TransactionModel, Transaction> implements TransactionService {
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository) {
        super.baseMapper = Mappers.getMapper(TransactionMapper.class);
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionModel findById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(DomainException.entityNotFound(String.format(ENTITY_NOT_FOUND_ERROR, Transaction.class.getSimpleName(), id)));
        return entity2Model(transaction);
    }

    @Override
    public TransactionModel saveOrUpdate(TransactionModel T) {
        return entity2Model(transactionRepository.save(model2Entity(T)));
    }

    @Override
    public TransactionModel makePaymentOrAdjustment(TransactionModel transactionModel) {
        switch(transactionModel.getMessageType()) {
            case PAYMENT:
                return TransactionManager.makePayment(transactionModel, this, accountService);
            case ADJUSTMENT:
                return TransactionManager.makeAdjustment(transactionModel, this, accountService);
            default:
                return null;
        }
    }

    @Override
    public List<TransactionModel> getTransactionModelListByRefTransactionId(String refTransactionId) {
        return entityList2ModelList(transactionRepository.findByRefTransactionId(refTransactionId));
    }

    @Override
    public BigDecimal checkAmount(BigDecimal oldAmount, BigDecimal adjustmentAmount) {
        if (oldAmount.compareTo(adjustmentAmount.negate()) < ZERO) {
            throw DomainException.paymentAdjustmentAmountNotValid(ADJUSTMENT_AMOUNT_FOR_PAYMENT_NOT_VALID_ERROR).get();
        }

        return adjustmentAmount;
    }

    @Override
    public BigDecimal getAmountSum(List<TransactionModel> transactionModels) {
        return transactionModels.stream()
                .map(transaction -> transaction.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}