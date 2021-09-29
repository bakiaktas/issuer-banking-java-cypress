package com.baktas.issuerbanking.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.ENTITY_NOT_FOUND_ERROR_CODE;
import static com.baktas.issuerbanking.utility.constants.Variables.ENTITY_NOT_FOUND;

@ExceptionMessage(code = ENTITY_NOT_FOUND_ERROR_CODE, reason = ENTITY_NOT_FOUND, httpStatus = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends DomainException {
    public EntityNotFoundException(HttpStatus httpStatus, String code, Map<String, Object> params) {
        super(httpStatus, code, params);
    }
}