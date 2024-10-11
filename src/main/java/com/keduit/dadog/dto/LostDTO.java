package com.keduit.dadog.dto;

import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Lost;
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

    private String lostTitle;

    private String lostPlace;

    private String imgUrl;

    private String lostTel;

    private String lostWriter;

    public LostDTO createLostDTO(Lost lost) {
        LostDTO lostDTO = new LostDTO();
        this.lostNo = lost.getLostNo();
        this.lostDate = lost.getLostDate();
        this.lostDetail = lost.getLostDetail();
        this.lostKind = lost.getLostKind();
        this.lostTitle = lost.getLostTitle();
        this.lostPlace = lost.getLostPlace();
        this.imgUrl = lost.getLostImgUrl();
        this.lostTel = lost.getLostTel();
        this.lostWriter = lost.getLostWriter();
        return lostDTO;
    }

    public LostDTO mainLost(Lost lost) {
        LostDTO lostDTO = new LostDTO();
        lostDTO.setLostNo(lost.getLostNo());
        lostDTO.setLostDate(lost.getLostDate());
        lostDTO.setLostKind(lost.getLostKind());
        lostDTO.setLostPlace(lost.getLostPlace());
        lostDTO.setImgUrl(lost.getLostImgUrl());
        return lostDTO;
    }
}
