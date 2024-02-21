package com.seeder.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalErrorHandlerTest {

    @Test
    void handleNotFoundError_ValidException_ReturnsNotFoundResponse() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        
        ResponseEntity<ErrorResponse> responseEntity = new GlobalErrorHandler().handleNotFoundError(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Entity not found", responseEntity.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValid_ValidException_ReturnsBadRequestResponse() {

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Field cannot be empty");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        
        ResponseEntity<Map<String,String>> responseEntity = new GlobalErrorHandler().handleMethodArgumentNotValid(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Field cannot be empty", responseEntity.getBody().get("fieldName"));
    }

    @Test
    void handleEntityAlreadyExists_ValidException_ReturnsBadRequestResponse() {
        EntityAlreadyExistException exception = new EntityAlreadyExistException("Entity already exists");
        
        ResponseEntity<ErrorResponse> responseEntity = new GlobalErrorHandler().handleEntityAlreadyExists(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Entity already exists", responseEntity.getBody().getMessage());
    }

    @Test
    void handleInvalidUserCredentials_ValidException_ReturnsUnauthorizedResponse() {
        InvalidUserCredentials exception = new InvalidUserCredentials("Invalid credentials");
        
        ResponseEntity<ErrorResponse> responseEntity = new GlobalErrorHandler().handleInvalidUserCredentials(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Invalid credentials", responseEntity.getBody().getMessage());
    }

    @Test
    void handleAnyException_ValidException_ReturnsInternalServerErrorResponse() {
        EntityException exception = new EntityException("Internal Server Error");
        
        ResponseEntity<ErrorResponse> responseEntity = new GlobalErrorHandler().handleAnyException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal Server Error", responseEntity.getBody().getMessage());
    }
}
