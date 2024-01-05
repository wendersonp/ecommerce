package com.wendersonp.processor.domain.exception;

import lombok.Getter;

@Getter
public class InvoiceProcessingException extends RuntimeException{

    private final String responseBody;

    public InvoiceProcessingException(String message, Throwable cause, String responseBody) {
        super(message, cause);
        this.responseBody = responseBody;
    }

}
