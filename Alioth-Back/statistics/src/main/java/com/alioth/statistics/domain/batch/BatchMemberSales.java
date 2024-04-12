package com.alioth.statistics.domain.batch;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchMemberSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long salesMemberCode;

    @Column(nullable = false)
    private String salesMemberName;

    @Column(nullable = false)
    private String contractPrice;

    @Column(nullable = false)
    private String contractCount;

    @Column(nullable = false)
    private String cancelPrice;

    @Column(nullable = false)
    private String cancelCount;

    @Column(nullable = false)
    private LocalDateTime createdTime;


}
