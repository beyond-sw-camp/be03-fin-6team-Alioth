package com.alioth.statistics.domain.member.domain;

import com.alioth.statistics.common.domain.BaseEntity;
import com.alioth.statistics.domain.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SalesMembers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    private Long salesMemberCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 19)
    private String phone;

    @Column(nullable = false, length = 18)
    private String name;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false)
    private String birthDay;

    @Column(nullable = false, length = 200)
    private String address;

    @Column
    private String profileImage;

    @Column
    private String officeAddress;

    @Column
    private String extensionNumber;

    @Builder.Default
    @Column(nullable = false)
    private String quit = "N";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SalesMemberType rank;

    @Builder.Default
    @Column(nullable = false)
    private String performanceReview = "C";

    @ManyToOne(fetch = FetchType.EAGER)
    private Team team;





}
