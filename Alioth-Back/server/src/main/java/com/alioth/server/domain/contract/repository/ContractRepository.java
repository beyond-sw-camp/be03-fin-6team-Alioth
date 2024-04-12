package com.alioth.server.domain.contract.repository;


import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.member.domain.SalesMembers;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllBySalesMembersId(Long memberId);
  
    @Query("SELECT c FROM Contract c WHERE c.contractDate BETWEEN :startDate AND :endDate")
    List<Contract> findAllByPeriod(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c FROM Contract c WHERE c.salesMembers.Id = :id AND c.contractDate BETWEEN :startDate AND :endDate")
    List<Contract> findAllByPeriodAndSalesMembersId(@Param("id") Long id, LocalDateTime startDate, LocalDateTime endDate);
}
