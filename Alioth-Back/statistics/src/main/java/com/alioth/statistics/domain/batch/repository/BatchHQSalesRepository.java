package com.alioth.statistics.domain.batch.repository;

import com.alioth.statistics.domain.batch.BatchHQSales;
import com.alioth.statistics.domain.batch.BatchTeamSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchHQSalesRepository extends JpaRepository<BatchHQSales, Long> {

    List<BatchHQSales> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
}
