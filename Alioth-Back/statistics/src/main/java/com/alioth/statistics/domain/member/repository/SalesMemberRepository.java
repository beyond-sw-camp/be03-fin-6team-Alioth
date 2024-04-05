package com.alioth.statistics.domain.member.repository;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SalesMemberRepository extends JpaRepository<SalesMembers, Long> {

    SalesMembers findFirstByOrderByIdDesc();

    List<SalesMembers> findAllByTeamId(Long teamId);

    Optional<SalesMembers> findBySalesMemberCode(Long salesMemberCode);

}
