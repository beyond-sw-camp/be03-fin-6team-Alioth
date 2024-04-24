package com.alioth.statistics.domain.batch.repository;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import com.alioth.statistics.domain.batch.BatchTeamSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchTeamSalesRepository extends JpaRepository<BatchTeamSales, Long> {

    List<BatchTeamSales> findByCreatedTimeBetween(LocalDateTime start, LocalDateTime end);

}
