package com.baktas.issuerbanking.service.account;

import com.baktas.issuerbanking.exception.DomainException;
import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.repository.AccountRepository;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.utility.base.BaseMapperManager;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.PAYMENT_BALANCE_NOT_VALID_ERROR;
import static com.baktas.issuerbanking.utility.constants.Variables.ZERO;

@Service
public class AccountServiceImpl extends BaseMapperManager<AccountModel, Account> implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        super.baseMapper = Mappers.getMapper(AccountMapper.class);
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountModel findById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(DomainException.entityNotFound(String.format(ENTITY_NOT_FOUND_ERROR, Account.class.getSimpleName(), id)));
        return entity2Model(account);
    }

    @Override
    public AccountModel saveOrUpdate(AccountModel T) {
        return entity2Model(accountRepository.save(model2Entity(T)));
    }

    @Override
    public BigDecimal checkBalance(BigDecimal balance, BigDecimal paymentAmount) {
        if (balance.compareTo(paymentAmount) < ZERO) {
            throw DomainException.paymentBalanceNotValid(PAYMENT_BALANCE_NOT_VALID_ERROR).get();
        }

        return balance;
    }
}