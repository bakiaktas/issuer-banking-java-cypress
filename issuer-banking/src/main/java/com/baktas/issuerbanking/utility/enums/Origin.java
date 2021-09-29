package com.baktas.issuerbanking.utility.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

import static com.baktas.issuerbanking.utility.constants.Variables.ONE;
import static com.baktas.issuerbanking.utility.constants.Variables.TWO;

@AllArgsConstructor
@Getter
public enum Origin {
    VISA(ONE),
    MASTER(TWO);

    public final BigDecimal commissionRate;
}