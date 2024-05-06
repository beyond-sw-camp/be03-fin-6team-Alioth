package com.alioth.server.domain.answer.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.answer.dto.req.AnswerReqDto;
import com.alioth.server.domain.board.domain.Board;
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
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private String AnswerDel_YN = "N";

    @ManyToOne
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMembers;

    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public void delete(){
        this.AnswerDel_YN = "Y";
    }

    public void update(AnswerReqDto answerReqDto){
        if(!answerReqDto.content().isEmpty()){
            this.content = answerReqDto.content();
        }
    }
}
