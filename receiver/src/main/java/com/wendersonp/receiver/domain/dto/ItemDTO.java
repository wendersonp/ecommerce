package com.wendersonp.receiver.domain.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    @Positive(message = "O campo deve ser positivo")
    private int sku;

    @Positive(message = "O campo deve ser positivo")
    private int quantidade;

    @Positive(message = "O campo deve ser positivo")
    private int valor;
}
