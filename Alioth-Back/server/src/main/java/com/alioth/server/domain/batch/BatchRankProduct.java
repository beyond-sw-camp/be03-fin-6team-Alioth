package com.alioth.server.domain.batch;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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


}
