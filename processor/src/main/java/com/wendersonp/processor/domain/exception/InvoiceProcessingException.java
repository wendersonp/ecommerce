package com.wendersonp.processor.domain.exception;

public class InvoiceProcessingException extends RuntimeException{
    public InvoiceProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
