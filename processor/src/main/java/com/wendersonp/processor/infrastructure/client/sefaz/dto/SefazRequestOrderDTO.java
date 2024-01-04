package com.wendersonp.processor.infrastructure.client.sefaz.dto;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class SefazRequestOrderDTO {

    private String orderNumber;
    private String externalOrderNumber;
    private SefazRequestClientDTO customer;
    private List<SefazRequestItemDTO> products;

    public SefazRequestOrderDTO(OrderDTO order, List<ItemFeesDTO> fees) {
        this.orderNumber = order.getOrdemPedido().getNumeroPedido();
        this.externalOrderNumber = order.getOrdemPedido().getNumeroOrdemExterno();
        this.customer = new SefazRequestClientDTO(order.getCliente());
        this.products = SefazRequestItemDTO.buildFromList(order.getItens(), fees);
    }
}
