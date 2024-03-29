package com.alioth.server.domain.dummy.domain;

import com.alioth.server.common.domain.BaseEntity;
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
public class Custom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false)
    private String customerCode;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String customerPhoneNumber;
    @Column(nullable = false)
    private String customerAddress;
    @Column(nullable = false)
    private String customerGrade;
}
