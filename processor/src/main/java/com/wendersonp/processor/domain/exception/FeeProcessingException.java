package com.wendersonp.processor.domain.exception;

public class FeeProcessingException extends RuntimeException{
    public FeeProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
