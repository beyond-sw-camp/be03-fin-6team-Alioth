package com.alioth.statistics.domain.batch.repository;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


public interface BatchMemberSalesRepository extends JpaRepository<BatchMemberSales, Long> {

    List<BatchMemberSales> findByCreatedTimeBetween(LocalDateTime start, LocalDateTime end);

}
