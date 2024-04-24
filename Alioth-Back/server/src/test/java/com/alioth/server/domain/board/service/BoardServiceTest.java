package com.alioth.server.domain.board.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.board.repository.BoardRepository;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamReqDto;
import com.alioth.server.domain.team.dto.TeamResDto;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Rollback
@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private TypeChange typeChange;
    @Autowired
    private SalesMemberService salesMemberService;
    @Autowired
    private SalesMemberRepository salesMemberRepository;
    @Autowired
    private TeamService teamService;

    private SalesMembers salesMembers;
    private Board board1;
    private Board board2;

    @BeforeEach
    void setUp() throws IOException {
        BoardCreateDto boardCreateDto1 = BoardCreateDto.builder()
                .title("Test Title")
                .content("Test Content")
                .boardType(BoardType.ANNOUNCEMENT)
                .build();

        BoardCreateDto boardCreateDto2 = BoardCreateDto.builder()
                .title("Test Title")
                .content("Test Content")
                .boardType(BoardType.SUGGESTION)
                .build();

        SalesMemberCreateReqDto salesMemberCreateReqDto = SalesMemberCreateReqDto.builder()
                .email("jangdg2@naver.com") // 마스킹
                .phone("010-1234-1244") // 끝 4자리 마스킹
                .name("김민재2")
                .password("a1234567!")
                .birthDay("990123") // 마스킹
                .address("축신") // 마스킹
                .rank(SalesMemberType.FP)
                .build();

        SalesMembers salesMembers1 = salesMemberService.create(salesMemberCreateReqDto);
        this.salesMembers = salesMemberRepository.findBySalesMemberCode(salesMembers1.getSalesMemberCode())
                .orElseThrow(() -> new EntityNotFoundException("Saved salesMember not found"));

        TeamReqDto teamReqDto = TeamReqDto.builder()
                .teamManagerCode(salesMembers.getSalesMemberCode())
                .teamName("test team")
                .build();

        Team team = teamService.createTeam(teamReqDto,salesMembers);
        salesMemberService.updateTeam(salesMembers.getId(),team);

        BoardResDto boardResDto1 = boardService.save(boardCreateDto1,salesMembers.getSalesMemberCode());
        this.board1 = boardRepository.findById(boardResDto1.boardId())
                .orElseThrow(() -> new EntityNotFoundException("Saved board1 not found"));

        BoardResDto boardResDto2 = boardService.save(boardCreateDto2,salesMembers.getSalesMemberCode());
        this.board2 = boardRepository.findById(boardResDto2.boardId())
                .orElseThrow(() -> new EntityNotFoundException("Saved board2 not found"));


    }
    @Test
    void save() {
        assertNotNull(board1.getBoardId());
        assertNotNull(board2.getBoardId());
    }

    @Test
    void update() {
        BoardUpdateDto boardUpdateDto = BoardUpdateDto.builder()
                .title(board1.getTitle())
                .content("Update Test Content")
                .build();
        BoardResDto boardResDto = boardService.update(boardUpdateDto, board1.getBoardId(),salesMembers.getSalesMemberCode());
        assertEquals("Update Test Content", boardResDto.content());
    }

    @Test
    void delete() {
        boardService.delete(board1.getBoardId(), salesMembers.getSalesMemberCode());
        Board board = boardService.findById(board1.getBoardId());
        assertEquals("Y", board.getBoardDel_YN());
    }

    @Test
    void list() {
        List<BoardResDto> boardResDtoList = boardService.list();
        assertFalse(boardResDtoList.isEmpty());
    }

    @Test
    void suggestionsList() {
        List<BoardResDto> boardResDtoList = boardService.suggestionsList(salesMembers.getSalesMemberCode());
        assertFalse(boardResDtoList.isEmpty());
    }

    @Test
    void detail() {
        Board board = boardService.findByBoardIdAndBoardDel_YN(board1.getBoardId(), "N");
        assertEquals(board1, board);
    }
}