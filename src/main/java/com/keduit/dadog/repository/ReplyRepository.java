package com.keduit.dadog.repository;

import com.keduit.dadog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 특정 게시글에 속한 모든 댓글 조회
    List<Reply> findByBoard_BoardNo(Long boardNo);

    // 특정 작성자의 모든 댓글 조회
    List<Reply> findByUser_UserNo(Long userNo);
}
