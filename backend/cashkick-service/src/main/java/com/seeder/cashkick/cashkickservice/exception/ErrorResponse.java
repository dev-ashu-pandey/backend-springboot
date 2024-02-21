package com.seeder.cashkick.cashkickservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private HttpStatus httpStatus;
}
