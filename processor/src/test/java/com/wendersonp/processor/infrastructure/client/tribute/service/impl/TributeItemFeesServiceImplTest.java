package com.wendersonp.processor.infrastructure.client.tribute.service.impl;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.dto.fixture.Fixture;
import com.wendersonp.processor.infrastructure.client.tribute.service.TributeItemConnector;
import com.wendersonp.receiver.domain.dto.ItemDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TributeItemFeesServiceImplTest {

    @Mock
    private TributeItemConnector connector;

    @InjectMocks
    private TributeItemFeesServiceImpl service;

    @Test
    void getFeesForItems() {
        List<ItemFeesDTO> fees = new Fixture<ItemFeesDTO>().listFromFile("static/tributes_valid.json", ItemFeesDTO.class);
        List<ItemDTO> items = new Fixture<ItemDTO>().listFromFile("static/items_valid.json", ItemDTO.class);

        fees.forEach(fee ->
            when(connector.requestItemFee(fee.getSku())).thenReturn(fee)
        );

        List<ItemFeesDTO> response = service.getFeesForItems(items);

        fees.sort(Comparator.comparingInt(ItemFeesDTO::getSku));
        response = response
                .stream()
                .sorted(Comparator.comparingInt(ItemFeesDTO::getSku))
                .toList();

        assertEquals(fees, response);
    }
}