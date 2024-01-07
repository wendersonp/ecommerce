package com.wendersonp.receiver.api.handler;

import com.wendersonp.receiver.domain.exceptions.TotalValueNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            HttpStatusCode status,
            @NotNull WebRequest request) {
        var errors = getValidationErrors(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetail.builder()
                .statusCode(status.value())
                .path(null)
                .errors(errors)
                .message(ex.getBody().getTitle())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    private Map<String, String> getValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });
        return errors;
    }
}
