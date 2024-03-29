package com.alioth.server.domain.board.repository;

import com.alioth.server.domain.board.domain.Board;
import com.alioth.server.domain.board.domain.BoardType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT b FROM Board b WHERE b.boardType != :boardType AND b.boardDel_YN != :delYN")
    List<Board> findByBoardList(@Param("boardType") BoardType boardType, @Param("delYN") String delYN);

    @Query(value = "SELECT b FROM Board b " +
            "LEFT JOIN b.salesMembers sm " +
            "LEFT JOIN sm.team t " +
            "WHERE t.id = :teamId " +
            "AND b.boardType = :boardType " +
            "AND b.boardDel_YN = :delYN")
    List<Board> findSuggestionsByTeam(@Param("teamId") Long teamId, @Param("boardType") BoardType boardType, @Param("delYN") String delYN);
}
