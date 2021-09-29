package com.baktas.issuerbanking.service.transaction;

import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.repository.entity.Transaction;
import com.baktas.issuerbanking.utility.base.BaseMapper;
import com.baktas.issuerbanking.utility.config.CycleAvoidingMappingContext;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CycleAvoidingMappingContext.class, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TransactionMapper extends BaseMapper<TransactionModel, Transaction> {
    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);
}