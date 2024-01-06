package com.wendersonp.processor.infrastructure.client.sefaz.dto;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.receiver.domain.dto.ItemDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class SefazRequestItemDTO {

    private long sku;
    private int amount;
    private double value;
    private int icmsValue;
    private int pisValue;
    private int difaulValue;
    private int fcpIcmsValue;

    public SefazRequestItemDTO(ItemDTO item, ItemFeesDTO fees) {
        this.sku = item.getSku();
        this.amount = item.getQuantidade();
        this.value = ((double) item.getValor()) / 100;
        this.icmsValue = fees.getValorIcms();
        this.difaulValue = fees.getValorDifaul();
        this.fcpIcmsValue = fees.getValorFcpIcms();
        this.pisValue = fees.getValorPis();
    }

    public static List<SefazRequestItemDTO> buildFromList(List<ItemDTO> items, List<ItemFeesDTO> fees) {
        var feeMap = fees.stream().collect(Collectors.toMap(ItemFeesDTO::getSku, Function.identity()));
        return items
                .stream()
                .map(item -> new SefazRequestItemDTO(item, feeMap.get(item.getSku())))
                .toList();
    }
}
