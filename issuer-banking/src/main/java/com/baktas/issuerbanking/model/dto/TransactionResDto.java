package com.baktas.issuerbanking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TransactionResDto implements Serializable {
    private static final long serialVersionUID = -18555152648270751L;

    private String transactionId;
}