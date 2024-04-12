package com.alioth.server.domain.batch;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BatchTeamSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String teamCode;

    @Column(nullable = false)
    private String teamName ;

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
