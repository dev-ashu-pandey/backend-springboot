package cashkickservice.exception;

import com.seeder.cashkick.cashkickservice.exception.EntityException;
import com.seeder.cashkick.cashkickservice.exception.EntityNotFoundException;
import com.seeder.cashkick.cashkickservice.exception.ErrorResponse;
import com.seeder.cashkick.cashkickservice.exception.GlobalErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalErrorHandlerTest {

    @Test
    void handleNotFoundException() {
        GlobalErrorHandler globalException = new GlobalErrorHandler();
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        ResponseEntity<ErrorResponse> responseEntity = globalException.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Entity not found", responseEntity.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        GlobalErrorHandler globalException = new GlobalErrorHandler();
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, String>> responseEntity = globalException.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("error message", responseEntity.getBody().get("fieldName"));
    }
    @Test
    void handleGlobalException() {
        GlobalErrorHandler globalException = new GlobalErrorHandler();
        EntityException exception = new EntityException("Internal server error");
        ResponseEntity<ErrorResponse> responseEntity = globalException.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal server error", responseEntity.getBody().getMessage());
    }
}
