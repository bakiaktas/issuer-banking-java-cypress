package com.baktas.issuerbanking.service.account;

import com.baktas.issuerbanking.model.AccountModel;
import com.baktas.issuerbanking.repository.entity.Account;
import com.baktas.issuerbanking.utility.base.BaseMapper;
import com.baktas.issuerbanking.utility.config.CycleAvoidingMappingContext;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CycleAvoidingMappingContext.class, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AccountMapper extends BaseMapper<AccountModel, Account> {
    AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);
}