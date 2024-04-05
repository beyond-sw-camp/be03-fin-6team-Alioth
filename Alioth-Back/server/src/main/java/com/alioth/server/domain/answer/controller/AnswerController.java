package com.alioth.server.domain.answer.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.answer.dto.req.AnswerReqDto;
import com.alioth.server.domain.answer.service.AnswerService;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
}
