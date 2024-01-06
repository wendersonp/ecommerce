package com.wendersonp.processor.domain.dto;

import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {

    private String numeroPedido;
    private String numeroOrdemExterno;
    private String chaveNFE;
    private String numeroNota;
    private LocalDateTime dataEmissao;
    private String pdf;
    private OrderStatusEnum status;

}
