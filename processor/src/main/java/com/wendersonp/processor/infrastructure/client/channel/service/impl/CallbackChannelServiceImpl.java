package com.wendersonp.processor.infrastructure.client.channel.service.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.exception.ChannelCallbackException;
import com.wendersonp.processor.domain.service.OrderCallbackChannelService;
import com.wendersonp.processor.infrastructure.client.channel.ChannelClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackChannelServiceImpl implements OrderCallbackChannelService {

    private final ChannelClient channelClient;
    @Override
    public void communicateSuccessfulOrder(InvoiceDTO invoice) {
        try {
            channelClient.communicateInvoice(invoice);
        } catch (FeignException exception) {
            log.error("Erro ao comunicar invoice com o número de pedido {}", invoice.getNumeroPedido(), exception);
            throw new ChannelCallbackException(exception.getMessage(), exception.getCause());
        }
    }
}
