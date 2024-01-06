package com.wendersonp.processor.domain.service.impl;

import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.repository.SaleOrderRepository;
import com.wendersonp.processor.domain.service.SaleOrderPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleOrderPersistenceServiceImpl implements SaleOrderPersistenceService {

    private final SaleOrderRepository repository;
    @Override
    public void save(SaleOrderEntity saleOrder) {
        repository.save(saleOrder);
    }
}
