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
public class ContractMembers extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CM_Id;
    @Column(nullable = false)
    private String CM_code;
    @Column(nullable = false)
    private String CM_name;
}
