package com.baktas.issuerbanking.exception;

import com.baktas.issuerbanking.model.ErrorModel;
import com.baktas.issuerbanking.utility.constants.ErrorMsg;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baktas.issuerbanking.utility.constants.ErrorMsg.NOT_VALID_ERROR;
import static com.baktas.issuerbanking.utility.constants.ErrorMsg.NOT_VALID_ERROR_CODE;
import static com.baktas.issuerbanking.utility.constants.Variables.NOT_VALID;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorModel>> handleMethodArgsNotValidHandler(MethodArgumentNotValidException ex, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ErrorModel> errorModelList = new ArrayList<>();
        String code;
        ErrorModel errorModel = null;
        for (FieldError fld : fieldErrors) {
            if (fld.getField().equals("accountId")) {
                code = ErrorMsg.NOT_POSITIVE_ERROR_CODE;

                errorModel = ErrorModel.builder()
                        .code(code)
                        .reason(NOT_VALID)
                        .message(String.format(fld.getDefaultMessage(), fld.getField()))
                        .build();
            }

            errorModelList.add(errorModel);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModelList);
    }

    @ExceptionHandler({JsonMappingException.class})
    public ResponseEntity<ErrorModel> handleJsonMappingException(JsonMappingException ex, WebRequest request) {
        try {
            if (ex.getCause() instanceof JsonParseException) {
                JsonParseException exception = (JsonParseException) ex.getCause();
                return handleJsonParseException(exception, request);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @ExceptionHandler({JsonParseException.class})
    public ResponseEntity<ErrorModel> handleJsonParseException(JsonParseException ex, WebRequest request) throws IOException {
        return buildResponseEntityErrorModel(ex, HttpStatus.BAD_REQUEST, NOT_VALID_ERROR_CODE, NOT_VALID, String.format(NOT_VALID_ERROR, ex.getProcessor().getCurrentName()));
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<ErrorModel> handleInvalidFormatException(InvalidFormatException ex, WebRequest request) {
        String reference = StringUtils.EMPTY;
        try {
            List<JsonMappingException.Reference> referenceList = ex.getPath().stream().filter(x -> x.getFieldName() != null).collect(Collectors.toList());
            reference = referenceList.stream().map(x -> x.getFieldName()).collect(Collectors.joining("."));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return buildResponseEntityErrorModel(ex, HttpStatus.BAD_REQUEST, NOT_VALID_ERROR_CODE, NOT_VALID, String.format(NOT_VALID_ERROR, reference));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ErrorModel> sendError(DomainException ex) {
        ExceptionMessage exceptionMessage = AnnotationUtils.findAnnotation(ex.getClass(), ExceptionMessage.class);
        HttpStatus status = getHttpStatus(exceptionMessage, ex);
        String code = getCode(exceptionMessage, ex);
        String message = getMessage(ex.getParams());
        return buildResponseEntityErrorModel(ex, status, code, exceptionMessage.reason(), message);
    }

    private ResponseEntity<ErrorModel> buildResponseEntityErrorModel(Exception ex, HttpStatus status, String code, String reason, String message) {
        ErrorModel errorModel = ErrorModel.builder()
                .code(code)
                .reason(reason)
                .message(message)
                .build();

        return ResponseEntity.status(status).body(errorModel);
    }

    private HttpStatus getHttpStatus(ExceptionMessage exceptionMessage, DomainException ex) {
        if (Objects.nonNull(ex.getHttpStatus()))
            return HttpStatus.valueOf(ex.getHttpStatus().name());

        if (Objects.nonNull(exceptionMessage.httpStatus()))
            return HttpStatus.INTERNAL_SERVER_ERROR;

        return HttpStatus.valueOf(exceptionMessage.httpStatus().name());
    }

    private String getCode(ExceptionMessage exceptionMessage, DomainException ex) {
        if (StringUtils.isNotEmpty(ex.getCode()))
            return ex.getCode();

        return exceptionMessage.code();
    }

    private String getMessage(Map<String, Object> params) {
        return Objects.nonNull(params) ? params.get("message").toString() : StringUtils.EMPTY;
    }
}