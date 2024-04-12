package com.alioth.statistics.domain.contract.repository;


import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.dummy.domain.InsuranceProduct;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findBySalesMembers(SalesMembers salesMembers);

    List<Contract> findByInsuranceProduct(InsuranceProduct insuranceProduct);

    List<Contract> findBySalesMembersAndContractStatus(SalesMembers salesMembers, ContractStatus contractStatus);

    List<Contract> findBySalesMembersAndContractDateBetween(SalesMembers salesMembers, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Contract> findByContractDateBetween(LocalDateTime startDate, LocalDateTime endDateTime);
}
