package com.alioth.statistics.dashboard.service;


import com.alioth.statistics.dashboard.dto.res.DashboardBoardResDto;
import com.alioth.statistics.domain.board.domain.Board;
import com.alioth.statistics.domain.board.domain.BoardType;
import com.alioth.statistics.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardBoardService {

    private final BoardRepository boardRepository;


    public List<DashboardBoardResDto> getBoardSug() {
        List<DashboardBoardResDto> sugBoard = boardRepository.findByBoardList(BoardType.SUGGESTION, "Y")
                .stream()
                .map(DashboardBoardResDto::of)
                .limit(5L)
                .toList();

        return sugBoard;
    }


    public List<DashboardBoardResDto> getBoardAnn() {
        List<DashboardBoardResDto> annBoard = boardRepository.findByBoardList(BoardType.ANNOUNCEMENT, "Y")
                .stream()
                .map(DashboardBoardResDto::of)
                .limit(5L)
                .toList();

        return annBoard;
    }


}
