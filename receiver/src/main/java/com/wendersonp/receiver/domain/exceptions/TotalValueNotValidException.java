package com.wendersonp.receiver.domain.exceptions;

public class TotalValueNotValidException extends RuntimeException{
    public TotalValueNotValidException() {
        super("O total para esta ordem não é equivalente a soma dos valores dos produtos");
    }
}
