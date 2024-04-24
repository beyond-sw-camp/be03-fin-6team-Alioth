package com.alioth.server.domain.answer.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.answer.dto.req.AnswerReqDto;
import com.alioth.server.domain.answer.dto.res.AnswerResDto;
import com.alioth.server.domain.answer.service.AnswerService;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.google.api.Http;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/{boardId}/create")
    public ResponseEntity<CommonResponse> createAnswer(
            @RequestBody @Valid AnswerReqDto answerReqDto,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long boardId
    ){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "답변이 추가되었습니다.",
                answerService.save(answerReqDto, boardId, Long.parseLong(userDetails.getUsername()))
        );
    }

    @PatchMapping("/update/{answerId}")
    public ResponseEntity<CommonResponse> updateAnswer(
            @RequestBody @Valid AnswerReqDto answerReqDto,
            @PathVariable Long answerId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "답변이 수정되었습니다.",
                answerService.update(answerReqDto, answerId,Long.parseLong(userDetails.getUsername()))
        );
    }

    @DeleteMapping("/delete/{answerId}")
    public ResponseEntity<CommonResponse> deleteAnswer(
            @PathVariable Long answerId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "답변이 삭제되었습니다.",
                answerService.delete(answerId, Long.parseLong(userDetails.getUsername()))
        );
    }

    @GetMapping("/detail/{answerId}")
    public ResponseEntity<CommonResponse> detailAnswer(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long answerId){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "게시글 상세정보",
                answerService.detail(Long.parseLong(userDetails.getUsername()), answerId)
        );
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<CommonResponse> getAnswersByBoardId(@PathVariable Long boardId) {
        List<AnswerResDto> answers = answerService.findAllAnswersByBoardId(boardId);
        if (answers.isEmpty()) {
            throw new EntityNotFoundException("답변이 없습니다.");
        }
        return CommonResponse.responseMessage(HttpStatus.OK, "답변 목록 조회 성공", answers);
    }
}
