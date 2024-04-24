package com.alioth.statistics.domain.batch;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BatchRankProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String productName; // 상품 이름

    private String productCode; // 상품 코드

    private String productCategory; // 상품 카테고리

    private LocalDateTime createdDate; // 날짜

    private String contractPrice; // 상품별 가격

    private String contractCount; // 상품별 건수

    private LocalDateTime createdTime; // 입사 날짜

}
