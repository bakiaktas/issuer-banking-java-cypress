package com.baktas.issuerbanking.utility;

import com.baktas.issuerbanking.exception.DomainException;
import com.baktas.issuerbanking.exception.EntityNotFoundException;
import com.baktas.issuerbanking.exception.payment.PaymentAmountException;
import com.baktas.issuerbanking.exception.payment.PaymentTransactionIdException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ErrorBuilder {
    public static EntityNotFoundException createEntityNotFoundException(String message) {
        Supplier<EntityNotFoundException> entityNotFoundExceptionSupplier = DomainException.entityNotFound(message);
        return entityNotFoundExceptionSupplier.get();
    }

    public static PaymentAmountException createPaymentAmountException(String message) {
        Supplier<PaymentAmountException> paymentAmountExceptionSupplier = DomainException.paymentAmountNotValid(message);
        return paymentAmountExceptionSupplier.get();
    }

    public static PaymentTransactionIdException createPaymentTransactionIdException(String message) {
        Supplier<PaymentTransactionIdException> paymentTransactionIdExceptionSupplier = DomainException.paymentTransactionIdNotValid(message);
        return paymentTransactionIdExceptionSupplier.get();
    }
}