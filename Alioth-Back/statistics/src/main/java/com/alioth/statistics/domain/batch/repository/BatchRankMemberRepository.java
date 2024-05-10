package com.alioth.statistics.domain.batch.repository;

import com.alioth.statistics.domain.batch.BatchRankMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BatchRankMemberRepository extends JpaRepository<BatchRankMember, Long> {

    List<BatchRankMember> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
