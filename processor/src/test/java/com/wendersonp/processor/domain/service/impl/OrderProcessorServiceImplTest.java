package com.wendersonp.processor.domain.service.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.dto.fixture.Fixture;
import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import com.wendersonp.processor.domain.exception.ChannelCallbackException;
import com.wendersonp.processor.domain.exception.FeeProcessingException;
import com.wendersonp.processor.domain.exception.InvoiceProcessingException;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.service.InvoiceService;
import com.wendersonp.processor.domain.service.ItemFeesService;
import com.wendersonp.processor.domain.service.OrderCallbackChannelService;
import com.wendersonp.processor.domain.service.SaleOrderPersistenceService;
import com.wendersonp.processor.domain.util.Util;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessorServiceImplTest {

    @Mock
    private ItemFeesService itemFeesService;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private OrderCallbackChannelService callbackChannelService;

    @Mock
    private SaleOrderPersistenceService persistenceService;

    @InjectMocks
    private OrderProcessorServiceImpl processorService;

    @Captor
    private ArgumentCaptor<SaleOrderEntity> saleCaptor;

    @Test
    @DisplayName("must process order and create invoice with success")
    void processTest() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);
        List<ItemFeesDTO> fees = new Fixture<ItemFeesDTO>()
                .listFromFile("static/tributes_valid.json", ItemFeesDTO.class);
        InvoiceDTO invoice = new Fixture<InvoiceDTO>().fromFile("static/invoice_valid.json", InvoiceDTO.class);

        when(itemFeesService.getFeesForItems(order.getItens())).thenReturn(fees);
        when(invoiceService.authorizeOrder(order, fees)).thenReturn(invoice);

        processorService.process(order);

        verify(callbackChannelService, times(1)).communicateSuccessfulOrder(invoice);
        verify(persistenceService, times(1)).save(saleCaptor.capture());

        SaleOrderEntity entity = saleCaptor.getValue();
        assertSuccess(entity, order, invoice);
    }

    @Test
    @DisplayName("must process order and throw channel callback exception")
    void processTestThrowChannelCallbackException() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);
        List<ItemFeesDTO> fees = new Fixture<ItemFeesDTO>()
                .listFromFile("static/tributes_valid.json", ItemFeesDTO.class);
        InvoiceDTO invoice = new Fixture<InvoiceDTO>().fromFile("static/invoice_valid.json", InvoiceDTO.class);
        ChannelCallbackException exception = new ChannelCallbackException("", new RuntimeException(), "ERROR_BODY");

        when(itemFeesService.getFeesForItems(order.getItens())).thenReturn(fees);
        when(invoiceService.authorizeOrder(order, fees)).thenReturn(invoice);
        doThrow(exception).when(callbackChannelService).communicateSuccessfulOrder(invoice);

        assertThrows(ChannelCallbackException.class, () -> processorService.process(order));

        verify(persistenceService, times(1)).save(saleCaptor.capture());

        SaleOrderEntity entity = saleCaptor.getValue();
        assertError(entity, order);
    }

    @Test
    @DisplayName("must process order and throw invoice processing exception")
    void processTestThrowInvoiceProcessingException() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);
        List<ItemFeesDTO> fees = new Fixture<ItemFeesDTO>()
                .listFromFile("static/tributes_valid.json", ItemFeesDTO.class);
        InvoiceProcessingException exception = new InvoiceProcessingException("", new RuntimeException(), "ERROR_BODY");

        when(itemFeesService.getFeesForItems(order.getItens())).thenReturn(fees);
        when(invoiceService.authorizeOrder(order, fees)).thenThrow(exception);

        assertThrows(InvoiceProcessingException.class, () -> processorService.process(order));

        verify(persistenceService, times(1)).save(saleCaptor.capture());

        SaleOrderEntity entity = saleCaptor.getValue();
        assertError(entity, order);
    }

    @Test
    @DisplayName("must process order and throw fee processing exception")
    void processTestThrowFeeProcessingException() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);
        FeeProcessingException exception = new FeeProcessingException("", new RuntimeException(), "ERROR_BODY");

        when(itemFeesService.getFeesForItems(order.getItens())).thenThrow(exception);

        assertThrows(FeeProcessingException.class, () -> processorService.process(order));

        verify(persistenceService, times(1)).save(saleCaptor.capture());

        SaleOrderEntity entity = saleCaptor.getValue();
        assertError(entity, order);
    }

    @Test
    @DisplayName("must process order and throw runtime exception")
    void processTestThrowRuntimeException() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);

        when(itemFeesService.getFeesForItems(order.getItens())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> processorService.process(order));

        verify(persistenceService, times(1)).save(saleCaptor.capture());

        SaleOrderEntity entity = saleCaptor.getValue();
        assertGenericError(entity, order);
        assertTrue(entity.getMotivo().contains(RuntimeException.class.getSimpleName()));
    }

    private void assertSuccess(SaleOrderEntity entity, OrderDTO order, InvoiceDTO invoice) {
        assertEquals(OrderStatusEnum.PROCESSADO, entity.getSituacao());
        assertEquals(order.getCanal(), entity.getCanal());
        assertEquals(Integer.parseInt(order.getEmpresa()), entity.getCodigoEmpresa());
        assertEquals(Integer.parseInt(order.getLoja()), entity.getCodigoLoja());
        assertEquals(order.getPdv(), entity.getNumeroPdv());
        assertEquals(order.getOrdemPedido().getNumeroPedido(), entity.getNumeroPedido());
        assertEquals(order.getOrdemPedido().getNumeroOrdemExterno(), entity.getNumeroOrdemExterno());
        assertEquals(Util.parseAndDivideByHundred(order.getTotalItens()), entity.getValorTotal());
        assertEquals(BigInteger.valueOf(order.getQuantidadeItens()), entity.getQtdItem());
        assertEquals(Util.parseToJson(order), entity.getVendaRequest());
        assertEquals(order.getOrdemPedido().getDataAutorizacao(), entity.getDataRequisicao());
        assertEquals(invoice.getChaveNFE(), entity.getChaveNfe());
        assertEquals(new BigInteger(invoice.getNumeroNota()), entity.getNumeroNota());
        assertEquals(invoice.getDataEmissao(), entity.getDataEmissao());
        assertEquals(invoice.getPdf(), entity.getPdf());
        assertNull(entity.getMotivo());
    }

    private void assertError(SaleOrderEntity entity, OrderDTO order) {
        assertEquals(OrderStatusEnum.ERRO, entity.getSituacao());
        assertEquals(order.getCanal(), entity.getCanal());
        assertEquals(Integer.parseInt(order.getEmpresa()), entity.getCodigoEmpresa());
        assertEquals(Integer.parseInt(order.getLoja()), entity.getCodigoLoja());
        assertEquals(order.getPdv(), entity.getNumeroPdv());
        assertEquals(order.getOrdemPedido().getNumeroPedido(), entity.getNumeroPedido());
        assertEquals(order.getOrdemPedido().getNumeroOrdemExterno(), entity.getNumeroOrdemExterno());
        assertEquals(Util.parseAndDivideByHundred(order.getTotalItens()), entity.getValorTotal());
        assertEquals(BigInteger.valueOf(order.getQuantidadeItens()), entity.getQtdItem());
        assertEquals(Util.parseToJson(order), entity.getVendaRequest());
        assertEquals(order.getOrdemPedido().getDataAutorizacao(), entity.getDataRequisicao());
        assertNull(entity.getChaveNfe());
        assertNull(entity.getNumeroNota());
        assertNull(entity.getDataEmissao());
        assertNull(entity.getPdf());
        assertTrue(entity.getMotivo().contains("ERROR_BODY"));
    }

    private void assertGenericError(SaleOrderEntity entity, OrderDTO order) {
        assertEquals(OrderStatusEnum.ERRO, entity.getSituacao());
        assertEquals(order.getCanal(), entity.getCanal());
        assertEquals(Integer.parseInt(order.getEmpresa()), entity.getCodigoEmpresa());
        assertEquals(Integer.parseInt(order.getLoja()), entity.getCodigoLoja());
        assertEquals(order.getPdv(), entity.getNumeroPdv());
        assertEquals(order.getOrdemPedido().getNumeroPedido(), entity.getNumeroPedido());
        assertEquals(order.getOrdemPedido().getNumeroOrdemExterno(), entity.getNumeroOrdemExterno());
        assertEquals(Util.parseAndDivideByHundred(order.getTotalItens()), entity.getValorTotal());
        assertEquals(BigInteger.valueOf(order.getQuantidadeItens()), entity.getQtdItem());
        assertEquals(Util.parseToJson(order), entity.getVendaRequest());
        assertEquals(order.getOrdemPedido().getDataAutorizacao(), entity.getDataRequisicao());
        assertNull(entity.getChaveNfe());
        assertNull(entity.getNumeroNota());
        assertNull(entity.getDataEmissao());
        assertNull(entity.getPdf());
    }
}