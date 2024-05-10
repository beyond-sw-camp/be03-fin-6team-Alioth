package com.alioth.statistics.dashboard.service;

import com.alioth.statistics.domain.board.domain.Board;
import com.alioth.statistics.domain.board.domain.BoardType;
import com.alioth.statistics.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DashboardBoardServiceTest {

    @Autowired private BoardRepository boardRepository;

    @Test
    public void asd() {
        List<Board> all = boardRepository.findAll();
        List<Board> find1 = boardRepository.findByBoardList(BoardType.ANNOUNCEMENT, "Y");
        List<Board> find2 = boardRepository.findByBoardList(BoardType.SUGGESTION, "Y");

        System.out.println("find1 = " + find1);
        System.out.println("find2 = " + find2);

    }

}