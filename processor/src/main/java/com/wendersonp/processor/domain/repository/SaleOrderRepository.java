package com.wendersonp.processor.domain.repository;

import com.wendersonp.processor.domain.models.SaleOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrderEntity, BigInteger> {

}
