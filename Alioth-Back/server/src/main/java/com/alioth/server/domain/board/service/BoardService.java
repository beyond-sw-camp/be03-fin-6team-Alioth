package com.alioth.server.domain.board.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.firebase.domain.FcmSendDto;
import com.alioth.server.common.firebase.service.FcmService;
import com.alioth.server.common.redis.RedisService;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.board.repository.BoardRepository;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.notification.domain.Notification;
import com.alioth.server.domain.notification.domain.ReadStatus;
import com.alioth.server.domain.notification.repository.NotificationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
    private final NotificationRepository notificationRepository;


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
        SalesMembers author = salesMemberService.findBySalesMemberCode(sm_code);
        Board board = typeChange.BoardCreateDtoToBoard(boardCreateDto, author);

        boardRepository.save(board);

        String eventId = UUID.randomUUID().toString();

        if (board.getBoardType() == BoardType.SUGGESTION) {
            if (author.getTeam() != null && author.getRank() != SalesMemberType.MANAGER) {
                // 저자가 FP이고 매니저가 있다면 해당 팀 매니저에게 알림
                SalesMembers teamManager = salesMemberService.findTeamManagerByTeamId(author.getTeam().getId());
                sendNotification(eventId, teamManager, board);
            } else if (author.getRank() == SalesMemberType.HQ) {
                // 저자가 HQ일 경우 다른 HQ 멤버들에게 알림
                List<SalesMembers> allHQMembers = salesMemberService.findAllHQMembers();
                for (SalesMembers hqMember : allHQMembers) {
                    if (!hqMember.equals(author)) {
                        sendNotification(eventId, hqMember, board);
                    }
                }
            }
        }
        return typeChange.BoardToBoardResDto(board);
    }


    private void sendNotification(String eventId, SalesMembers recipient, Board board) throws IOException {
        if (!notificationRepository.existsByMessageId(eventId)) {
            Notification notification = Notification.builder()
                    .salesMember(recipient)
                    .title("새 건의사항")
                    .message("새로운 건의사항이 등록되었습니다: " + board.getTitle())
                    .readStatus(ReadStatus.Unread)
                    .messageId(eventId)
                    .build();
            notificationRepository.save(notification);

            // FCM 메시지 발송
            String fcmToken = redisService.getFcmToken(recipient.getSalesMemberCode());
            if (fcmToken != null) {
                FcmSendDto fcmSendDto = FcmSendDto.builder()
                        .token(fcmToken)
                        .title("새 건의사항")
                        .body("새로운 건의사항이 등록되었습니다: " + board.getTitle())
                        .url("/BoardList")
                        .build();
                fcmService.sendMessageTo(fcmSendDto);
            }
        }
    }


            // 각 팀 멤버에 대해 알림을 생성합니다.
//            for (SalesMembers member : teamMembers) {
//                if (!notificationRepository.existsByMessageId(eventId)) {
//                    Notification notification = Notification.builder()
//                            .salesMember(member)
//                            .title("새 건의사항")
//                            .message("새로운 건의사항이 등록되었습니다: " + board.getTitle())
//                            .readStatus(ReadStatus.Unread)
//                            .messageId(eventId) // 모든 알림에 동일한 이벤트 ID 사용
//                            .build();
//                    notificationRepository.save(notification);
//
//                    // FCM 메시지 발송
//                    String fcmToken = redisService.getFcmToken(member.getSalesMemberCode());
//                    if (fcmToken != null) {
//                        FcmSendDto fcmSendDto = FcmSendDto.builder()
//                                .token(fcmToken)
//                                .title("새 건의사항")
//                                .body("새로운 건의사항이 등록되었습니다: " + board.getTitle())
//                                .url("/BoardList")
//                                .build();
//                        fcmService.sendMessageTo(fcmSendDto);
//                    }
//                }
//            }
//        }
//        return typeChange.BoardToBoardResDto(board);
//    }


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
        // 직급에 따라 접근이 제한된 SUGGESTION 게시글 조회
        SalesMembers member = salesMemberService.findBySalesMemberCode(sm_code);
        List<Board> suggestions;
        if (member.getRank() == SalesMemberType.HQ) {
            suggestions = boardRepository.findByBoardType(BoardType.SUGGESTION); // HQ는 모든 SUGGESTION 조회
        } else if (member.getRank() == SalesMemberType.MANAGER) {
            if(member.getTeam() != null){
                suggestions = boardRepository.findSuggestionsByTeam(member.getTeam().getId(), BoardType.SUGGESTION, "N");
            }else{
                suggestions = boardRepository.findMyBoards(sm_code, BoardType.SUGGESTION);
            }
        } else {
            suggestions = boardRepository.findMyBoards(sm_code, BoardType.SUGGESTION); // FP는 자신의 SUGGESTION만 조회
        }
        return suggestions.stream()
                .map(typeChange::BoardToBoardResDto)
                .collect(Collectors.toList());
    }

    public BoardResDto detail(Long sm_code, Long boardId) {
        Board board = this.findByBoardIdAndBoardDel_YN(boardId, "N");

        // 상세 정보 접근 권한 체크
        if (BoardType.SUGGESTION.equals(board.getBoardType())) {
            // 관리자 또는 글 작성자만 접근 가능
            SalesMembers member = salesMemberService.findBySalesMemberCode(sm_code);
            boolean isManagerOrHigher = member.getRank().equals(SalesMemberType.MANAGER) || member.getRank().equals(SalesMemberType.HQ);
            boolean isAuthor = Objects.equals(board.getSalesMembers().getSalesMemberCode(), sm_code);
            if (!isManagerOrHigher && !isAuthor) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
        }

        return typeChange.BoardToBoardResDto(board);
    }
}
