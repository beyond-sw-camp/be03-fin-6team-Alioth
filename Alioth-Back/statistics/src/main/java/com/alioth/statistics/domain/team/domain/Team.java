package com.alioth.statistics.domain.team.domain;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String teamName;
    @Column(nullable = false)
    private String teamCode;
    @Column(nullable = false)
    private Long teamManagerCode;
    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL)
    @Builder.Default
    private List<SalesMembers> teamMembers = new ArrayList<>();


}
