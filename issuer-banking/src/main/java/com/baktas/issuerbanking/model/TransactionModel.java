package com.baktas.issuerbanking.model;

import com.baktas.issuerbanking.utility.enums.MessageType;
import com.baktas.issuerbanking.utility.enums.Origin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TransactionModel implements Serializable {
    private static final long serialVersionUID = 2702963486623696150L;

    private String transactionId;
    private MessageType messageType;
    private Long accountId;
    private Origin origin;
    private BigDecimal amount;
    private String refTransactionId;
}