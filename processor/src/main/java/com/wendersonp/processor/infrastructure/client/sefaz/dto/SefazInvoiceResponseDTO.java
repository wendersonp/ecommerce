package com.wendersonp.processor.infrastructure.client.sefaz.dto;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class SefazInvoiceResponseDTO {

    private String nfeKey;

    private String invoiceNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime issuanceDate;

    private String invoice;

    public InvoiceDTO toInvoiceDTO(OrderDTO order) {
        return InvoiceDTO.builder()
                .numeroPedido(order.getOrdemPedido().getNumeroPedido())
                .numeroOrdemExterno(order.getOrdemPedido().getNumeroOrdemExterno())
                .chaveNFE(nfeKey)
                .numeroNota(invoiceNumber)
                .dataEmissao(issuanceDate)
                .pdf(invoice)
                .status(OrderStatusEnum.EM_PROCESSAMENTO)
                .build();
    }
}
