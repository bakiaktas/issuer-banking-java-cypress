package com.baktas.issuerbanking.utility;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.baktas.issuerbanking.utility.constants.Variables.ONE_HUNDRED;

@UtilityClass
public class UtilityManager {
    public BigDecimal percentage(BigDecimal base, BigDecimal percent) {
        return base.multiply(percent).divide(ONE_HUNDRED);
    }

    public BigDecimal formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.UP);
    }
}