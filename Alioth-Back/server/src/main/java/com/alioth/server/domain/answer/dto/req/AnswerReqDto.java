package com.alioth.server.domain.answer.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AnswerReqDto (
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {}
