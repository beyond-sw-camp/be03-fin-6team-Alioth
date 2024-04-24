package com.alioth.statistics.domain.batch.repository;

import com.alioth.statistics.domain.batch.BatchRankProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchRankProductRepository extends JpaRepository<BatchRankProduct, Long> {

    List<BatchRankProduct> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
