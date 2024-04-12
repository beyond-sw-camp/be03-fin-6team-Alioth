package com.alioth.server.domain.board.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.firebase.domain.FcmSendDto;
import com.alioth.server.common.firebase.service.FcmService;
import com.alioth.server.common.firebase.service.FcmServiceImpl;
import com.alioth.server.common.redis.RedisService;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private final FcmService fcmService;
    private final RedisService redisService;


    public Board findById(Long BoardId){
        return boardRepository.findById(BoardId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 글입니다."));
    }

    public Board findByBoardIdAndBoardDel_YN(Long boardId, String del_yn) {
        return boardRepository.findByBoardIdAndBoardDel_YN(boardId, del_yn).orElseThrow(()->new EntityNotFoundException("삭제된 글입니다."));
    }

    public void boardSM_id(Board board, SalesMembers salesMembers){
        if(!Objects.equals(board.getSalesMembers(), salesMembers)){
            throw new IllegalArgumentException("글의 작성자가 아닙니다.");
        }
    }

    public BoardResDto save(BoardCreateDto boardCreateDto, Long sm_code) throws IOException {
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        Board board = typeChange.BoardCreateDtoToBoard(boardCreateDto, salesMembers);
        boardRepository.save(board);

        if (board.getBoardType() == BoardType.SUGGESTION) {
            String fcmToken = redisService.getFcmToken(sm_code);
            if (fcmToken != null) {
                FcmSendDto fcmSendDto = FcmSendDto.builder()
                        .token(fcmToken)
                        .title("새 건의사항")
                        .body("새로운 건의사항이 등록되었습니다: " + board.getTitle())
                        .url("/BoardList")
                        .build();

                fcmService.sendMessageTo(fcmSendDto);
            } else {
                log.error("유효한 FCM 토큰이 없습니다.");
                throw new IllegalArgumentException("유효한 토큰이 없습니다.");
            }
        }
        return typeChange.BoardToBoardResDto(board);
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

    public BoardResDto detail(Long sm_code, Long boardId) {
        Board board = this.findByBoardIdAndBoardDel_YN(boardId, "N");
        if(!Objects.equals(board.getSalesMembers().getSalesMemberCode(), sm_code)){
            throw new AccessDeniedException("게시글의 작성한 사원이 아닙니다.");
        }
        return typeChange.BoardToBoardResDto(board);
    }
}
