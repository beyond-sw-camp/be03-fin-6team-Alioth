package com.alioth.server.domain.member.repository;

import com.alioth.server.domain.member.domain.SalesMembers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

import java.util.Optional;


@Repository
public interface SalesMemberRepository extends JpaRepository<SalesMembers, Long> {

    SalesMembers findFirstByOrderByIdDesc();

    List<SalesMembers> findAllByTeamId(Long teamId);

    Optional<SalesMembers> findBySalesMemberCode(Long salesMemberCode);

    boolean existsBySalesMemberCode(Long salesMemberCode);

}
