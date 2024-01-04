package com.wendersonp.processor.domain.service;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;

import java.util.List;

public interface InvoiceService {

    InvoiceDTO authorizeOrder(OrderDTO order, List<ItemFeesDTO> itemFees);
}
