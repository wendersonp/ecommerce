package com.wendersonp.processor.domain.service;

import com.wendersonp.processor.domain.dto.InvoiceDTO;

public interface OrderCallbackChannelService {

    void communicateSuccessfulOrder(InvoiceDTO invoice);
}
