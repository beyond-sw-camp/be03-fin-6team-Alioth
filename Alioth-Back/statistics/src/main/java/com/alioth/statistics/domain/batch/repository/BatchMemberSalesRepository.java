package com.alioth.statistics.domain.batch.repository;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface BatchMemberSalesRepository extends JpaRepository<BatchMemberSales, Long> {

    List<BatchMemberSales> findByCreatedTimeBetween(LocalDateTime start, LocalDateTime end);
    List<BatchMemberSales> findBySalesMemberCodeAndCreatedTimeBetween(Long memberCode, LocalDateTime start, LocalDateTime end);

}
