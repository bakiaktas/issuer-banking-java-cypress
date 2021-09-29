package com.baktas.issuerbanking.utility.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMsg {
    public static final String NOT_VALID_ERROR_CODE = "1000";
    public static final String NOT_VALID_ERROR = "%s is not valid.";

    public static final String NOT_POSITIVE_ERROR_CODE = "1001";
    public static final String NOT_POSITIVE_ERROR = "%s must be positive!";

    public static final String ENTITY_NOT_FOUND_ERROR_CODE = "1002";
    public static final String ENTITY_NOT_FOUND_ERROR = "%s not found with by id: %s";

    public static final String PAYMENT_BALANCE_NOT_VALID_ERROR_CODE = "1003";
    public static final String PAYMENT_BALANCE_NOT_VALID_ERROR = "Inadequate balance to make payment!";

    public static final String PAYMENT_AMOUNT_NOT_VALID_ERROR_CODE = "1004";
    public static final String PAYMENT_AMOUNT_NOT_VALID_ERROR = "Payment amount must be positive!";

    public static final String PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR_CODE = "1005";
    public static final String PAYMENT_TRANSACTION_ID_NOT_VALID_ERROR = "Transaction id must be null for payment!";

    public static final String ADJUSTMENT_TRANSACTION_ID_NOT_VALID_ERROR_CODE = "1006";
    public static final String ADJUSTMENT_TRANSACTION_ID_NOT_VALID_ERROR = "Transaction id cannot be null for adjustment!";

    public static final String ADJUSTMENT_AMOUNT_NOT_VALID_ERROR_CODE = "1007";
    public static final String ADJUSTMENT_AMOUNT_NOT_VALID_ERROR = "Adjustment amount cannot be zero!";

    public static final String ADJUSTMENT_AMOUNT_FOR_PAYMENT_NOT_VALID_ERROR_CODE = "1007";
    public static final String ADJUSTMENT_AMOUNT_FOR_PAYMENT_NOT_VALID_ERROR = "Adjustment amount cannot be greater than old payment amount!";
}