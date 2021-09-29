package com.baktas.issuerbanking.model.dto;

import com.baktas.issuerbanking.utility.enums.MessageType;
import com.baktas.issuerbanking.utility.enums.Origin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.NOT_POSITIVE_ERROR;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TransactionReqDto implements Serializable {
    private static final long serialVersionUID = 4784503234792185365L;

    private String transactionId;

    private MessageType messageType;

    @Positive(message = NOT_POSITIVE_ERROR)
    private Long accountId;

    private Origin origin;

    private BigDecimal amount;
}