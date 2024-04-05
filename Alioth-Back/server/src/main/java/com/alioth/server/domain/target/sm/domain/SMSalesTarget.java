package com.alioth.server.domain.target.sm.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
public class SMSalesTarget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "salesmember_id")
    private SalesMembers salesMembers;

    @Column(nullable = false)
    private BigInteger targetPrice;

    @Column(nullable = false)
    private Long targetCount;

    @Column(nullable = false)
    private LocalDateTime targetStartTime;

    @Column(nullable = false)
    private LocalDateTime targetEndTime;

}
