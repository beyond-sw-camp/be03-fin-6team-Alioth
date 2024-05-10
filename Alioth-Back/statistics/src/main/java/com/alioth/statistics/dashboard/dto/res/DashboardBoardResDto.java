package com.alioth.statistics.dashboard.dto.res;


import com.alioth.statistics.domain.board.domain.Board;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record DashboardBoardResDto(
        String title,
        String memberName,
        String created
) {

    public static DashboardBoardResDto of(Board board) {

        return DashboardBoardResDto.builder()
                .title(board.getTitle())
                .memberName(board.getSalesMembers().getName())
                .created(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

}
