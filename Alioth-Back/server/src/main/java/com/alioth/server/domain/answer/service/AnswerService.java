package com.alioth.server.domain.answer.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.answer.domain.Answer;
import com.alioth.server.domain.answer.dto.req.AnswerReqDto;
import com.alioth.server.domain.answer.dto.res.AnswerResDto;
import com.alioth.server.domain.answer.repository.AnswerRepository;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.service.BoardService;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final SalesMemberService salesMemberService;
    private final BoardService boardService;
    private final TypeChange typeChange;

    public Answer findById(Long answerId){
        return answerRepository.findById(answerId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 답변입니다."));
    }

    public void boardCheck(Answer answer, Long sm_code){
        SalesMembers manager = salesMemberService.findBySalesMemberCode(sm_code);
        if(!Objects.equals(manager.getRank().toString(), "HQ")) {
            if (!Objects.equals(answer.getSalesMembers().getSalesMemberCode(), sm_code)) {
                throw new AccessDeniedException("답글을 작성한 사람이 아닙니다.");
            }
        }
    }

    public AnswerResDto save(AnswerReqDto answerReqDto, Long boardId, Long sm_code) {
        Board board = boardService.findById(boardId);
        SalesMembers manager = salesMemberService.findBySalesMemberCode(sm_code);
        SalesMembers members = salesMemberService.findBySalesMemberCode(board.getSalesMembers().getSalesMemberCode());

        if(!Objects.equals(manager.getRank().toString(), "HQ")){
            if(!Objects.equals(members.getTeam().getId(), manager.getTeam().getId())){
                throw new AccessDeniedException("건의사항을 작성한 사원의 팀장이 아닙니다.");
            }
        }

        return typeChange.AnswerToAnswerResDto(
                answerRepository.save(
                        typeChange.AnswerReqToAnswer(answerReqDto, manager, board)
                )
        );
    }

    public AnswerResDto update(AnswerReqDto answerReqDto, Long answerId, Long sm_code) {
        Answer answer = this.findById(answerId);
        boardCheck(answer, sm_code);
        SalesMembers manager = salesMemberService.findBySalesMemberCode(sm_code);
        answer.update(answerReqDto, manager);
        return typeChange.AnswerToAnswerResDto(answer);
    }

    public AnswerResDto delete(Long answerId, Long sm_code) {
        Answer answer = this.findById(answerId);
        boardCheck(answer, sm_code);
        answer.delete();
        return typeChange.AnswerToAnswerResDto(answer);
    }

    public AnswerResDto detail(Long sm_code, Long answerId) {
        Answer answer = this.findById(answerId);
        if(!(Objects.equals(answer.getBoard().getSalesMembers().getSalesMemberCode(), sm_code) ||
                Objects.equals(answer.getSalesMembers().getSalesMemberCode(), sm_code))
        ){
            throw new AccessDeniedException("건의사항을 작성한 사람이나 해당 건의사항에 권한이 있는 팀장이 아닙니다.");
        }
        return typeChange.AnswerToAnswerResDto(answer);
    }

    public List<AnswerResDto> findAllAnswersByBoardId(Long boardId) {
        List<Answer> answers = answerRepository.findAllByBoardId(boardId);
        return answers.stream().map(typeChange::AnswerToAnswerResDto).collect(Collectors.toList());
    }
}
