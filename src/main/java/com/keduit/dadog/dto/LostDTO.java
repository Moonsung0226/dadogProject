package com.keduit.dadog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LostDTO {

    private Long lostNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lostDate;

    private String lostDetail;

    private String lostKind;

    private String lostPlace;

    private String imgUrl;

    private String lostTel;

    private String lostWriter;
}
