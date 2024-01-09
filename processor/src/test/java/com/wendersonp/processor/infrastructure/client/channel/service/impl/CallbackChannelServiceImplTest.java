package com.wendersonp.processor.infrastructure.client.channel.service.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.dto.fixture.Fixture;
import com.wendersonp.processor.domain.exception.ChannelCallbackException;
import com.wendersonp.processor.infrastructure.client.channel.CallbackChannelClient;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallbackChannelServiceImplTest {

    @Mock
    private CallbackChannelClient client;

    @InjectMocks
    private CallbackChannelServiceImpl service;

    @Test
    @DisplayName("must communicate invoice of sale with success")
    void communicateSuccessfulOrder() {
        var invoice = new Fixture<InvoiceDTO>().fromFile("static/invoice_valid.json", InvoiceDTO.class);
        String response = "Venda " + invoice.getNumeroOrdemExterno() + " recebida com sucesso";
        when(client.communicateInvoice(invoice)).thenReturn(response);
        service.communicateSuccessfulOrder(invoice);
        verify(client, times(1)).communicateInvoice(invoice);
    }

    @Test
    @DisplayName("must throw exception when trying to communicate invoice")
    void communicateFailedOrder() {
        var invoice = new Fixture<InvoiceDTO>().fromFile("static/invoice_valid.json", InvoiceDTO.class);
        when(client.communicateInvoice(invoice)).thenThrow(FeignException.class);
        assertThrows(ChannelCallbackException.class, () -> service.communicateSuccessfulOrder(invoice));
    }
}