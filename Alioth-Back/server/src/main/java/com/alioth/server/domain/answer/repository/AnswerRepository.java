package com.alioth.server.domain.answer.repository;

import com.alioth.server.domain.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE a.board.boardId = :boardId")
    List<Answer> findAllByBoardId(Long boardId);
}
