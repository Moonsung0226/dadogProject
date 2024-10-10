package com.keduit.dadog.dto;

import com.keduit.dadog.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ToString
public class BoardDTO {

    private Long boardNo; // 게시물 번호

    @NotEmpty(message = "작성자는 필수 입력입니다.")
    private String boardWriter; // 게시물 작성자

    @NotEmpty(message = "제목은 필수 입력입니다.")
    private String boardTitle; // 게시물 제목

    @NotEmpty(message = "내용은 필수 입력입니다.")
    private String boardContent; // 게시물 내용


    private Long boardViews; // 조회수



}
