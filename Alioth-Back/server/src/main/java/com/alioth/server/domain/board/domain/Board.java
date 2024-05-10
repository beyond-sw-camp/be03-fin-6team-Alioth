package com.alioth.server.domain.board.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.answer.domain.Answer;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.member.domain.SalesMembers;
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
public class Board extends BaseEntity {

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

    public void delete(){
        this.boardDel_YN = "Y";
    }

    public void update(BoardUpdateDto boardUpdateDto) {
        if(!boardUpdateDto.title().isEmpty()){
            this.title = boardUpdateDto.title();
        }
        if(!boardUpdateDto.content().isEmpty()){
            this.content = boardUpdateDto.content();
        }
    }
}