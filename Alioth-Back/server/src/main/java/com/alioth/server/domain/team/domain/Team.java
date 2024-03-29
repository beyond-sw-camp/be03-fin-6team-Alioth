package com.alioth.server.domain.team.domain;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.team.dto.TeamUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    public void update(TeamUpdateDto dto){
        this.teamName= dto.teamName();
        this.teamManagerCode=dto.teamManagerCode();
    }


}
