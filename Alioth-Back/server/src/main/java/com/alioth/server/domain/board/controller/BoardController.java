package com.alioth.server.domain.board.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.board.dto.req.BoardCreateDto;
import com.alioth.server.domain.board.dto.req.BoardUpdateDto;
import com.alioth.server.domain.board.service.BoardService;
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
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createBoard(
            @RequestBody @Valid BoardCreateDto boardCreateDto,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "추가되었습니다.",
                boardService.save(boardCreateDto,Long.parseLong(userDetails.getUsername()))
        );
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> listBoard(@AuthenticationPrincipal UserDetails userDetails){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "게시글 리스트",
                boardService.list()
        );
    }

    @GetMapping("/suggestions-list")
    public ResponseEntity<CommonResponse> suggestionsListBoard(@AuthenticationPrincipal UserDetails userDetails){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "건의사항 리스트",
                boardService.suggestionsList(Long.parseLong(userDetails.getUsername()))
        );
    }

    @PatchMapping("/update/{boardId}")
    public ResponseEntity<CommonResponse> updateBoard(
            @RequestBody @Valid BoardUpdateDto boardUpdateDto,
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "글이 수정되었습니다.",
                boardService.update(boardUpdateDto,boardId,Long.parseLong(userDetails.getUsername()))
        );
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<CommonResponse> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "글이 삭제되었습니다.",
                boardService.delete(boardId,Long.parseLong(userDetails.getUsername()))
        );
    }
}
