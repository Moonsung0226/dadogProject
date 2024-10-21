package com.keduit.dadog.dto;

import com.keduit.dadog.entity.Reply;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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

    private List<ReplyDTO> replies;

    private Long boardViews; // 조회수

    private LocalDate createTime;
    private LocalDate updateTime;

    public BoardDTO(Long boardNo, String boardWriter, String boardTitle, String boardContent, Long boardViews, LocalDate createTime, LocalDate updateTime) {
        this.boardNo = boardNo;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardViews = boardViews;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // 디폴트 생성자 (기본 생성자)
    // 주로 객체 생성 시 초기값 없이 객체를 만들 때 사용
    // 예를 들어, 스프링 프레임워크가 JSON 또는 Form 데이터를 매핑할 때, 값들을 나중에 설정하는 경우에도 디폴트 생성자를 사용
    // 또한, 다른 방식으로 초기화를 하거나, 필요에 따라 일부 필드만 채우는 경우에 유용
    public BoardDTO() {}

    public void updateItem(String boardTitle, String boardContent){

    }
}
