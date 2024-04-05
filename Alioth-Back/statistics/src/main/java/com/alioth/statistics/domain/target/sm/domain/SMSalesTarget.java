package com.alioth.statistics.domain.target.sm.domain;

import com.alioth.statistics.common.domain.BaseEntity;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
public class SMSalesTarget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "salesmember_id")
    private SalesMembers salesMembers;

    @Column(nullable = false)
    private Long targetPrice;

    @Column(nullable = false)
    private Long targetCount;

    @Column(nullable = false)
    private LocalDateTime targetStartTime;

    @Column(nullable = false)
    private LocalDateTime targetEndTime;

}
