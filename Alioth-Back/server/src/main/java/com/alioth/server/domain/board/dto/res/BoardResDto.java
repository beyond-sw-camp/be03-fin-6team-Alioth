package com.alioth.server.domain.board.dto.res;

import com.alioth.server.domain.board.domain.BoardType;
import lombok.Builder;

@Builder
public record BoardResDto(
        Long boardId,
        String title,
        String content,
        BoardType boardType,
        Long memberId
) {}
