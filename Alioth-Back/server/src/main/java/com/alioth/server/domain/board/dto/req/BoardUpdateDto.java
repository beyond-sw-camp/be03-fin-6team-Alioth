package com.alioth.server.domain.board.dto.req;

import com.alioth.server.domain.board.domain.BoardType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BoardUpdateDto(
        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {}
