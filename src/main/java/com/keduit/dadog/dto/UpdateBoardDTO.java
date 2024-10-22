package com.keduit.dadog.dto;

import lombok.*;

@Getter
@Setter
@ToString
//@NoArgsConstructor  // 기본생성자
//@RequiredArgsConstructor    // this.있는놈
public class UpdateBoardDTO {

    private String boardTitle;

    private String boardContent;
}
