package com.baktas.issuerbanking.exception;

import com.baktas.issuerbanking.exception.adjustment.AdjustmentAmountException;
import com.baktas.issuerbanking.exception.adjustment.AdjustmentAmountExceptionForPayment;
import com.baktas.issuerbanking.exception.adjustment.AdjustmentTransactionIdException;
import com.baktas.issuerbanking.exception.payment.PaymentAmountException;
import com.baktas.issuerbanking.exception.payment.PaymentBalanceException;
import com.baktas.issuerbanking.exception.payment.PaymentTransactionIdException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
@Getter
public abstract class DomainException extends RuntimeException {
    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(Throwable throwable) {
        super(throwable);
    }

    private HttpStatus httpStatus;
    private String code;
    private Map<String, Object> params;

    public static Supplier<EntityNotFoundException> entityNotFound(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new EntityNotFoundException(HttpStatus.NOT_FOUND, null, params);
        };
    }

    public static Supplier<PaymentBalanceException> paymentBalanceNotValid(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new PaymentBalanceException(HttpStatus.BAD_REQUEST, null, params);
        };
    }

    public static Supplier<PaymentAmountException> paymentAmountNotValid(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new PaymentAmountException(HttpStatus.BAD_REQUEST, null, params);
        };
    }

    public static Supplier<PaymentTransactionIdException> paymentTransactionIdNotValid(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new PaymentTransactionIdException(HttpStatus.BAD_REQUEST, null, params);
        };
    }

    public static Supplier<AdjustmentAmountException> adjustmentAmountNotValid(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new AdjustmentAmountException(HttpStatus.BAD_REQUEST, null, params);
        };
    }

    public static Supplier<AdjustmentTransactionIdException> adjustmentTransactionNotValid(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new AdjustmentTransactionIdException(HttpStatus.BAD_REQUEST, null, params);
        };
    }

    public static Supplier<AdjustmentAmountExceptionForPayment> paymentAdjustmentAmountNotValid(String message) {
        return () -> {
            Map<String, Object> params = putStringObjectMap(message);
            return new AdjustmentAmountExceptionForPayment(HttpStatus.BAD_REQUEST, null, params);
        };
    }

    private static Map<String, Object> putStringObjectMap(String message) {
        Map<String, Object> params = new HashMap<>();
        params.put("message", message);
        return params;
    }
}