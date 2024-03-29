package com.wendersonp.processor.domain.service.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import com.wendersonp.processor.domain.factory.SaleOrderEntityFactory;
import com.wendersonp.processor.domain.factory.impl.FailedSaleOrderFactoryImpl;
import com.wendersonp.processor.domain.factory.impl.SuccessfulSaleOrderFactoryImpl;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.service.InvoiceService;
import com.wendersonp.processor.domain.service.ItemFeesService;
import com.wendersonp.processor.domain.service.OrderCallbackChannelService;
import com.wendersonp.processor.domain.service.OrderProcessorService;
import com.wendersonp.processor.domain.service.SaleOrderPersistenceService;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessorServiceImpl implements OrderProcessorService {

    private final ItemFeesService itemFeesService;

    private final InvoiceService invoiceService;

    private final OrderCallbackChannelService orderCallbackChannelService;

    private final SaleOrderPersistenceService persistenceService;

    private final Clock clock;

    @Override
    public void process(OrderDTO order) {
        try {
            var feesFromItems = itemFeesService.getFeesForItems(order.getItens());
            var invoice = invoiceService.authorizeOrder(order, feesFromItems);

            invoice.setStatus(OrderStatusEnum.PROCESSADO);

            orderCallbackChannelService.communicateSuccessfulOrder(invoice);

            persistSuccessfulOrder(order, invoice);
            log.info("situacao: {}; pedido: {}", invoice.getStatus(), order.getOrdemPedido().getNumeroPedido());
        } catch (RuntimeException exception) {
            persistFailedOrder(order, exception);
            throw exception;
        }
    }

    private void persistSuccessfulOrder(OrderDTO order, InvoiceDTO invoice) {
        SaleOrderEntityFactory factory = new SuccessfulSaleOrderFactoryImpl(order, invoice, clock);
        SaleOrderEntity entity = factory.create();
        persistenceService.save(entity);
    }

    private void persistFailedOrder(OrderDTO order, RuntimeException exception) {
        SaleOrderEntityFactory factory = new FailedSaleOrderFactoryImpl(order, exception, clock);
        SaleOrderEntity entity = factory.create();
        persistenceService.save(entity);
    }
}
