package com.alioth.statistics.domain.dummy.repository;

import com.alioth.statistics.domain.dummy.domain.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {
    List<InsuranceProduct> findByInsuranceCategory(String insuranceCategory);
}
