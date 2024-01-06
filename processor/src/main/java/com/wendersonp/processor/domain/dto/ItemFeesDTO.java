package com.wendersonp.processor.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class ItemFeesDTO {

    private int sku;
    private int valorIcms;
    private int valorPis;
    private int valorDifaul;
    private int valorFcpIcms;

}
