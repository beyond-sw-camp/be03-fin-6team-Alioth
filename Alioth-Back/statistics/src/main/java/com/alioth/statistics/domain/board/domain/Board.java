package com.alioth.statistics.domain.board.domain;


import com.alioth.statistics.domain.member.domain.SalesMembers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BoardType boardType;

    @Builder.Default
    private String boardDel_YN = "N";

    @ManyToOne
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMembers;

}
