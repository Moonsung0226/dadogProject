package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 제목으로 검색
    Page<Board> findByBoardTitleContaining(String title, Pageable pageable);

    // 내용으로 검색
    Page<Board> findByBoardContentContaining(String content, Pageable pageable);

    // 작성자로 검색
    Page<Board> findByBoardWriterContaining(String writer, Pageable pageable);
}
