package com.wendersonp.receiver.api.handler;

import com.wendersonp.receiver.domain.exceptions.TotalValueNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TotalValueNotValidException.class})
    public ResponseEntity<ErrorDetail> handleBadRequestException(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, request, ex);
    }


    private ResponseEntity<ErrorDetail> buildResponse(HttpStatus status, HttpServletRequest request, Exception ex) {
        return ResponseEntity.status(status).body(ErrorDetail.builder()
                .statusCode(status.value())
                .path(request.getPathInfo())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
}
