package com.alioth.statistics.domain.contract.domain;

import com.alioth.statistics.common.domain.BaseEntity;
import com.alioth.statistics.domain.dummy.domain.*;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;
    @Column(nullable = false)
    private String contractCode;
    @Column(nullable = false)
    private LocalDateTime contractDate;
    @Column(nullable = false)
    private LocalDateTime contractExpireDate;
    @Column(nullable = false)
    private String contractPeriod;
    @Column(nullable = false)
    private String contractTotalPrice;
    @Column(nullable = false)
    private String contractPaymentAmount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentFrequency contractPaymentFrequency;
    @Column(nullable = false)
    private Long contractPaymentMaturityInstallment;
    @Column(nullable = false)
    private Long contractCount;
    @Column(nullable = false)
    private String contractPaymentMethod;
    @Column(nullable = false)
    private String contractPayer;
    @Column(nullable = false)
    private String contractConsultation;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    @Enumerated(EnumType.STRING)
    private InsuranceProduct insuranceProduct;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Custom custom;

    @ManyToOne
    @JoinColumn(name = "CM_id")
    @Enumerated(EnumType.STRING)
    private ContractMembers contractMembers;

    @ManyToOne
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMembers;

}
