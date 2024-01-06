package com.wendersonp.processor.domain.service.impl;

import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.repository.SaleOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleOrderPersistenceServiceImplTest {

    @Mock
    private SaleOrderRepository repository;

    @InjectMocks
    private SaleOrderPersistenceServiceImpl service;
    @Test
    void saveTest() {
        var saleOrderEntity = mock(SaleOrderEntity.class);
        service.save(saleOrderEntity);
        verify(repository, times(1)).save(saleOrderEntity);
    }
}