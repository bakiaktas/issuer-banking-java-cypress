package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.model.TransactionModel;
import com.baktas.issuerbanking.model.dto.TransactionReqDto;
import com.baktas.issuerbanking.model.dto.TransactionResDto;
import com.baktas.issuerbanking.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "The Transaction API")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(value = "/", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public TransactionResDto makePaymentOrAdjustment(@Valid @RequestBody TransactionReqDto transactionReqDto) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionId(transactionReqDto.getTransactionId());
        transactionModel.setMessageType(transactionReqDto.getMessageType());
        transactionModel.setAccountId(transactionReqDto.getAccountId());
        transactionModel.setOrigin(transactionReqDto.getOrigin());
        transactionModel.setAmount(transactionReqDto.getAmount());

        return TransactionResDto.builder().transactionId(transactionService.makePaymentOrAdjustment(transactionModel).getTransactionId()).build();
    }
}