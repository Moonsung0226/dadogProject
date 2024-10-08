package com.keduit.dadog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LostDTO {

    private Long lostId;

    private LocalDateTime lostDate;

    private String lostDetail;

    private String lostKind;

    private String lostPlace;

    private String lostTel;

    private String lostWriter;

    private String lostFileName;
    private String lostImgUrl;
    private String lostOriImgName;


}
