package com.baktas.issuerbanking.exception.payment;

import com.baktas.issuerbanking.exception.DomainException;
import com.baktas.issuerbanking.exception.ExceptionMessage;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.PAYMENT_BALANCE_NOT_VALID_ERROR_CODE;
import static com.baktas.issuerbanking.utility.constants.Variables.NOT_VALID;

@ExceptionMessage(code = PAYMENT_BALANCE_NOT_VALID_ERROR_CODE, reason = NOT_VALID, httpStatus = HttpStatus.BAD_REQUEST)
public class PaymentBalanceException extends DomainException {
    public PaymentBalanceException(HttpStatus httpStatus, String code, Map<String, Object> params) {
        super(httpStatus, code, params);
    }
}