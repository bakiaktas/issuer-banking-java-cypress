package com.baktas.issuerbanking.utility;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Variables {
    public static String ACCOUNT_URL = "/v1/accounts/{accountId}";
    public static String TRANSACTION_URL = "/v1/transactions/";
    public static final Long ACCOUNT_ID = 5574L;
    public static final String TRANSACTION_ID = "da41c586-e217-4885-84b8-43ac845b943d";
    public static final Long NOT_EXISTING_ACCOUNT_ID = 2L;
    public static final Long NOT_POSITIVE_ACCOUNT_ID = 0L;
    public static final BigDecimal BALANCE = new BigDecimal("1001.88");
    public static final BigDecimal AMOUNT = new BigDecimal("100");
    public static final BigDecimal NOT_POSITIVE_AMOUNT = new BigDecimal("0");
}