package com.alioth.server.domain.contract.domain;

import com.alioth.server.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Renewal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long renewalId;
    @Column(nullable = false)
    private LocalDateTime renewalDate;
    @Column(nullable = false)
    private Long renewalCount;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
