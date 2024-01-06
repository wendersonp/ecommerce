package com.wendersonp.processor.domain.exception;

import lombok.Getter;

@Getter
public class FeeProcessingException extends RuntimeException{

    private final String responseBody;

    public FeeProcessingException(String message, Throwable cause, String responseBody) {
        super(message, cause);
        this.responseBody = responseBody;
    }

}
