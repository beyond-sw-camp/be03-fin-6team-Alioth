package com.alioth.server.domain.board.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.board.repository.BoardRepository;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final TypeChange typeChange;
    private final SalesMemberService salesMemberService;

    public Board findById(Long BoardId){
        return boardRepository.findById(BoardId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 글입니다."));
    }

    public void boardSM_id(Board board, SalesMembers salesMembers){
        if(!Objects.equals(board.getSalesMembers(), salesMembers)){
            throw new IllegalArgumentException("글의 작성자가 아닙니다.");
        }
    }

    public BoardResDto save(BoardCreateDto boardCreateDto, Long sm_code) {
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        return typeChange.BoardToBoardResDto(
                boardRepository.save(
                        typeChange.BoardCreateDtoToBoard(boardCreateDto, salesMembers)
                )
        );
    }

    public BoardResDto update(BoardUpdateDto boardUpdateDto, Long boardId, Long sm_code) {
        Board board = this.findById(boardId);
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        boardSM_id(board,salesMembers);
        board.update(boardUpdateDto);
        return typeChange.BoardToBoardResDto(board);
    }

    public BoardResDto delete(Long boardId, Long sm_code) {
        Board board = this.findById(boardId);
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        boardSM_id(board,salesMembers);
        board.delete();
        return typeChange.BoardToBoardResDto(board);
    }

    public List<BoardResDto> list() {
        return boardRepository.findByBoardList(BoardType.SUGGESTION, "Y")
                .stream()
                .map(typeChange::BoardToBoardResDto)
                .collect(Collectors.toList());
    }

    public List<BoardResDto> suggestionsList(Long sm_code) {
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        Long teamId = salesMembers.getTeam().getId();
        List<Board> suggestions = boardRepository.findSuggestionsByTeam(teamId,BoardType.SUGGESTION,"N");
        return suggestions.stream()
                .map(typeChange::BoardToBoardResDto)
                .collect(Collectors.toList());
    }
}
