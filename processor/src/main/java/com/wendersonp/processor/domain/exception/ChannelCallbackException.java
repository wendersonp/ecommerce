package com.wendersonp.processor.domain.exception;

import lombok.Getter;

@Getter
public class ChannelCallbackException extends RuntimeException{

    private final String responseBody;
    public ChannelCallbackException(String message, Throwable cause, String responseBody) {
        super(message, cause);
        this.responseBody = responseBody;
    }
}
