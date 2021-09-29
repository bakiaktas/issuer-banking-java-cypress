package com.baktas.issuerbanking.service.account;

import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.utility.base.BaseService;

import java.math.BigDecimal;

public interface AccountService extends BaseService<AccountModel, Long> {
    BigDecimal checkBalance(BigDecimal balance, BigDecimal paymentAmount);
}