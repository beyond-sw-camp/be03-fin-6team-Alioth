package com.alioth.server.domain.answer.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.answer.domain.Answer;
import com.alioth.server.domain.answer.dto.req.AnswerReqDto;
import com.alioth.server.domain.answer.dto.res.AnswerResDto;
import com.alioth.server.domain.answer.repository.AnswerRepository;
import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.board.repository.BoardRepository;
import com.alioth.server.domain.board.service.BoardService;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamReqDto;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;
@Rollback
@Transactional
@SpringBootTest
class AnswerServiceTest {

    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TypeChange typeChange;
    @Autowired
    private SalesMemberService salesMemberService;
    @Autowired
    private SalesMemberRepository salesMemberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private TeamService teamService;

    private SalesMembers salesMembers;
    private Board board;
    private Answer answer;
    @BeforeEach
    void setUp() {
        AnswerReqDto answerReqDto = AnswerReqDto.builder()
                .title("Test Title")
                .content("Test Content")
                .build();

        BoardCreateDto boardCreateDto = BoardCreateDto.builder()
                .title("Test Title")
                .content("Test Content")
                .boardType(BoardType.ANNOUNCEMENT)
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


        BoardResDto boardResDto = boardService.save(boardCreateDto,salesMembers.getSalesMemberCode());
        this.board = boardRepository.findById(boardResDto.boardId())
                .orElseThrow(() -> new EntityNotFoundException("Saved board not found"));

        AnswerResDto answerResDto = answerService.save(answerReqDto, this.board.getBoardId(), this.salesMembers.getSalesMemberCode());
        this.answer = answerRepository.findById(answerResDto.answer_id())
                .orElseThrow(() -> new EntityNotFoundException("Saved answer not found"));
    }

    @Test
    void save() {
        assertNotNull(answer.getAnswerId());
    }

    @Test
    void update() {
        AnswerReqDto answerReqDto = AnswerReqDto.builder()
                .title("Test Title2")
                .content("Update Test Content2")
                .build();
        AnswerResDto answerUpdateDto = answerService.update(answerReqDto,answer.getAnswerId(),salesMembers.getSalesMemberCode());
        assertEquals("Update Test Content2", answerUpdateDto.content());
    }

    @Test
    void delete() {
        answerService.delete(answer.getAnswerId(),salesMembers.getSalesMemberCode());
        Answer answer1 = answerService.findById(answer.getAnswerId());
        assertEquals("Y", answer1.getAnswerDel_YN());
    }

    @Test
    void detail() {
        Answer answer1 = answerService.findById(answer.getAnswerId());
        assertEquals(answer, answer1);
    }
}