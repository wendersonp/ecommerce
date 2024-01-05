package com.wendersonp.processor.infrastructure.client.sefaz.service.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.dto.fixture.Fixture;
import com.wendersonp.processor.domain.exception.InvoiceProcessingException;
import com.wendersonp.processor.infrastructure.client.sefaz.SefazClient;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazInvoiceResponseDTO;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazRequestOrderDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SefazInvoiceServiceImplTest {

    @Mock
    private SefazClient client;

    @InjectMocks
    private SefazInvoiceServiceImpl service;

    @Test
    @DisplayName("must authorize sale and get invoice with success")
    void authorizeOrder() {
        OrderDTO order = new Fixture<OrderDTO>().fromFile("static/order_valid.json", OrderDTO.class);
        var feesFixture = new Fixture<ItemFeesDTO>();
        List<ItemFeesDTO> fees = feesFixture.listFromFile("static/tributes_valid.json", ItemFeesDTO.class);
        InvoiceDTO expectedInvoice = new Fixture<InvoiceDTO>().fromFile("static/invoice_valid.json", InvoiceDTO.class);
        SefazInvoiceResponseDTO sefazResponse = new Fixture<SefazInvoiceResponseDTO>().fromFile("static/sefaz_valid_response.json", SefazInvoiceResponseDTO.class);
        SefazRequestOrderDTO sefazRequest = new Fixture<SefazRequestOrderDTO>().fromFile("static/sefaz_valid_request.json", SefazRequestOrderDTO.class);

        when(client.authorizeOrder(sefazRequest)).thenReturn(sefazResponse);

        var actualInvoice = service.authorizeOrder(order, fees);
        assertEquals(expectedInvoice, actualInvoice);
    }

    @Test
    @DisplayName("must throw exception when trying to invoice")
    void authorizeOrderThrowException() {
        OrderDTO order = new Fixture<OrderDTO>().fromFile("static/order_valid.json", OrderDTO.class);
        var feesFixture = new Fixture<ItemFeesDTO>();
        List<ItemFeesDTO> fees = feesFixture.listFromFile("static/tributes_valid.json", ItemFeesDTO.class);
        SefazRequestOrderDTO sefazRequest = new Fixture<SefazRequestOrderDTO>().fromFile("static/sefaz_valid_request.json", SefazRequestOrderDTO.class);

        when(client.authorizeOrder(sefazRequest)).thenThrow(FeignException.class);

        assertThrows(InvoiceProcessingException.class, () -> service.authorizeOrder(order, fees));
    }
}