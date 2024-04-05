package com.alioth.server.domain.target.team.repository;

import com.alioth.server.domain.target.team.domain.TeamTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamTargetRepository extends JpaRepository<TeamTarget, Long> {
}
