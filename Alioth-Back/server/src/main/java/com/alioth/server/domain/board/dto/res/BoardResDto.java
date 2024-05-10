package com.alioth.server.domain.board.dto.res;

import com.alioth.server.domain.board.domain.BoardType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardResDto(
        Long boardId,
        String title,
        String content,
        BoardType boardType,
        String salesMemberName,
        String writerName,
        Long salesMemberCode,
        LocalDateTime created_at,
        LocalDateTime updated_at

) {}
