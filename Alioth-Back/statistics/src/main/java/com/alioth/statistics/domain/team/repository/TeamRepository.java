package com.alioth.statistics.domain.team.repository;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByTeamCode(String teamCode);
    Team findFirstByOrderByIdDesc();

    @Query("SELECT tm.teamMembers FROM Team tm WHERE tm.id = :teamId")
    List<SalesMembers> findSalesMembersByTeamId(@Param("teamId") Long teamId);
}
