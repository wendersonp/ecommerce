package com.wendersonp.receiver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemDTO {
    private int sku;
    private int quantidade;
    private int valor;
}
