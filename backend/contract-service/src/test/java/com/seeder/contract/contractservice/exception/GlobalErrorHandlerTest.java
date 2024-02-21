package com.seeder.contract.contractservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalErrorHandlerTest {

    @Test
    void handleMethodArgumentNotValid() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        GlobalErrorHandler globalException = new GlobalErrorHandler();
        ResponseEntity<Map<String, String>> responseEntity = globalException.handleMethodArgumentNotValid(exception);

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expectedErrors, responseEntity.getBody());
    }

    @Test
    void handleIllegalArgument() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal argument");

        GlobalErrorHandler globalException = new GlobalErrorHandler();
        ResponseEntity<ErrorResponse> responseEntity = globalException.handleIllegalArgument(exception);

        ErrorResponse expectedErrorResponse = new ErrorResponse("Illegal argument", HttpStatus.CONFLICT);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(expectedErrorResponse.getMessage(), responseEntity.getBody().getMessage());
        assertEquals(expectedErrorResponse.getHttpStatus(), responseEntity.getBody().getHttpStatus());
    }

    @Test
    void handleAnyException() {

        EntityException exception = new EntityException("Entity exception");

        GlobalErrorHandler globalException = new GlobalErrorHandler();
        ResponseEntity<ErrorResponse> responseEntity = globalException.handleAnyException(exception);

        ErrorResponse expectedErrorResponse = new ErrorResponse("Entity exception", HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(expectedErrorResponse.getMessage(), responseEntity.getBody().getMessage());
    }
}
