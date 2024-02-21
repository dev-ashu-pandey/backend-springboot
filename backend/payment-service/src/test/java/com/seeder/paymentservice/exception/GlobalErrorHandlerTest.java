package com.seeder.paymentservice.exception;

import com.seeder.payment.paymentservice.exception.EntityException;
import com.seeder.payment.paymentservice.exception.EntityNotFoundException;
import com.seeder.payment.paymentservice.exception.ErrorResponse;
import com.seeder.payment.paymentservice.exception.GlobalErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidExceptionMock;

    @Mock
    private EntityNotFoundException entityNotFoundExceptionMock;

    @Mock
    private EntityException entityExceptionMock;

    @InjectMocks
    private GlobalErrorHandler globalErrorHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleNotFound() {
        when(entityNotFoundExceptionMock.getMessage()).thenReturn("Entity not found");

        ResponseEntity<ErrorResponse> responseEntity = globalErrorHandler.handleNotFound(entityNotFoundExceptionMock);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Entity not found", responseEntity.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        BindingResult bindingResultMock = mock(BindingResult.class);
        FieldError fieldErrorMock = new FieldError("objectName", "fieldName", "Field cannot be empty");
        when(methodArgumentNotValidExceptionMock.getBindingResult()).thenReturn(bindingResultMock);
        when(bindingResultMock.getAllErrors()).thenReturn(List.of(fieldErrorMock));

        ResponseEntity<Map<String,String>> responseEntity = globalErrorHandler.handleMethodArgumentNotValidException(methodArgumentNotValidExceptionMock);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("fieldName", "Field cannot be empty");
        assertEquals(expectedErrors, responseEntity.getBody());
    }

    @Test
    void handleAnyException() {
        when(entityExceptionMock.getMessage()).thenReturn("Entity exception occurred");
        ErrorResponse errorResponse = new ErrorResponse("Entity exception occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<ErrorResponse> responseEntity = globalErrorHandler.handleAnyException(entityExceptionMock);

        assertEquals(errorResponse.getHttpStatus(), responseEntity.getStatusCode());
        assertEquals(errorResponse.getMessage(), responseEntity.getBody().getMessage());
    }

}
