package com.baktas.issuerbanking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AccountResDto implements Serializable {
    private static final long serialVersionUID = 3774100726109879871L;

    private BigDecimal balance;
}