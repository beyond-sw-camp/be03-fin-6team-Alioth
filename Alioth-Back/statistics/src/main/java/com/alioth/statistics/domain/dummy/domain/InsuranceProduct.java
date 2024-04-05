package com.alioth.statistics.domain.dummy.domain;

import com.alioth.statistics.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;
    @Column(nullable = false)
    private String insuranceName;
    @Column(nullable = false)
    private String insuranceCategory;
    @Column(nullable = false)
    private String insuranceSubCode;
    @Column(nullable = false)
    private String insuranceMainCode;
}
