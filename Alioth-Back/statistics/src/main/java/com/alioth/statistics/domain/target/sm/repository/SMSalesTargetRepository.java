package com.alioth.statistics.domain.target.sm.repository;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.target.sm.domain.SMSalesTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SMSalesTargetRepository extends JpaRepository<SMSalesTarget, Long> {

    List<SMSalesTarget> findBySalesMembers(SalesMembers salesMembers);

}
