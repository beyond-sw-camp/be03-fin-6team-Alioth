package com.alioth.statistics.domain.target.team.repository;

import com.alioth.statistics.domain.target.team.domain.TeamTarget;
import com.alioth.statistics.domain.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamTargetRepository extends JpaRepository<TeamTarget, Long> {

    List<TeamTarget> findByTeam(Team team);

}
