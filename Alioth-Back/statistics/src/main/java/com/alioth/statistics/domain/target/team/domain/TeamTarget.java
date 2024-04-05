package com.alioth.statistics.domain.target.team.domain;

import com.alioth.statistics.common.domain.BaseEntity;
import com.alioth.statistics.domain.team.domain.Team;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Entity
public class TeamTarget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private BigInteger targetPrice;

    @Column(nullable = false)
    private Long targetCount;

    @Column(nullable = false)
    private LocalDateTime targetStartTime;

    @Column(nullable = false)
    private LocalDateTime targetEndTime;

}
