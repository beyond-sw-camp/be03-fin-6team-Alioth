package com.alioth.server.domain.dummy.repository;

import com.alioth.server.domain.dummy.domain.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {
}
