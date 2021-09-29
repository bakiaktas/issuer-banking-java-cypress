package com.baktas.issuerbanking.utility.constants;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Variables {
    public static final int ZERO = 0;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal(2);
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static final String ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND";
    public static final String NOT_VALID = "NOT_VALID";
}