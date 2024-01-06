package com.wendersonp.processor.infrastructure.client.tribute.service.impl;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.dto.fixture.Fixture;
import com.wendersonp.processor.domain.exception.FeeProcessingException;
import com.wendersonp.processor.infrastructure.client.tribute.TributeItemClient;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TributeItemConnectorImplTest {

    @Mock
    private TributeItemClient client;

    @InjectMocks
    private TributeItemConnectorImpl service;


    @Test
    @DisplayName("must get item fee with success")
    void requestItemFee() {
        int sku = 324226428;
        ItemFeesDTO item = new Fixture<ItemFeesDTO>().fromFile("static/tribute_valid.json", ItemFeesDTO.class);

        when(client.getItemFee(sku)).thenReturn(item);
        var response = service.requestItemFee(sku);

        assertEquals(item, response);
    }

    @Test
    @DisplayName("must get item fee throw exception")
    void requestItemFeeThrowException() {
        int sku = 324226428;

        when(client.getItemFee(sku)).thenThrow(FeignException.class);

        assertThrows(FeeProcessingException.class, () -> service.requestItemFee(sku));
    }
}