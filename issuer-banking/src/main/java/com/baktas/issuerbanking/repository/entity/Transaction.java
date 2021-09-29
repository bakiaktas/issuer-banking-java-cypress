package com.baktas.issuerbanking.repository.entity;

import com.baktas.issuerbanking.utility.enums.MessageType;
import com.baktas.issuerbanking.utility.enums.Origin;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "TRANSACTION", schema = "XYZ")
public class Transaction implements Serializable {
    private static final long serialVersionUID = -5959847141655912063L;

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Enumerated(STRING)
    @Column(name = "message_type", length = 10)
    private MessageType messageType;

    @Column(name = "account_id")
    private Long accountId;

    @Enumerated(STRING)
    @Column(name = "origin", length = 6)
    private Origin origin;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "ref_transaction_id")
    private String refTransactionId;
}