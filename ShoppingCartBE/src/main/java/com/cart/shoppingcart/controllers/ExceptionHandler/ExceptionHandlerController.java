package com.cart.shoppingcart.controllers.ExceptionHandler;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cart.shoppingcart.dtos.common.ErrorApi;

@ControllerAdvice
public class ExceptionHandlerController {
        @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleError(Exception exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleError(MethodArgumentNotValidException exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    private ErrorApi buildError(String message, HttpStatus status) {
        return ErrorApi.builder()
            .timestamp(String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())))
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message)
            .build();
    }
}
