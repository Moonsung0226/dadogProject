package com.keduit.dadog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.Principal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class ReplyDTO {

    private Long replyNo;   // 댓글 번호

    private Long boardNo;   // 게시글 번호

    private String replyWriter; // 댓글 작성자

    private String replyContent;    // 댓글 내용

    private LocalDateTime updateTime;   // 댓글 수정일

    private LocalDateTime createTime;   // 댓글 생성일

    private Long userNo;          // 작성자 사용자 ID


    public ReplyDTO(){}

    public ReplyDTO(Long replyNo, Long boardNo, String replyWriter, String replyContent, LocalDateTime updateReply, LocalDateTime createReply, Long userNo) {
        this.replyNo = replyNo;
        this.boardNo = boardNo;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.userNo = userNo;
    }
}
