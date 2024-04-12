package com.alioth.server.domain.team.repository;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.team.domain.Team;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamCode(String teamCode);
    Team findFirstByOrderByIdDesc();

    @Query("SELECT tm.teamMembers FROM Team tm WHERE tm.id = :teamId")
    List<SalesMembers> findSalesMembersByTeamId(@Param("teamId") Long teamId);
}
