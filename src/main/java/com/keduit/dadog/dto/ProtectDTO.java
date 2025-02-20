package com.keduit.dadog.dto;

import com.keduit.dadog.entity.Protect;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProtectDTO {

    private Long proNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate proDate;

    private String proDetail;

    private String proKind;

    private String proTitle;

    private String proPlace;

    private String imgUrl;

    private String proTel;

    private String proWriter;

    private LocalDate protectCreateDate;


    public ProtectDTO createProtectDTO(Protect protect) {
        ProtectDTO protectDTO = new ProtectDTO();
        this.proNo = protect.getProNo();
        this.proDate = protect.getProDate();
        this.proDetail = protect.getProDetail();
        this.proKind = protect.getProKind();
        this.proTitle = protect.getProTitle();
        this.proPlace = protect.getProPlace();
        this.imgUrl = protect.getProImgUrl();
        this.proTel = protect.getProTel();
        this.proWriter = protect.getProWriter();
        return protectDTO;
    }

    public ProtectDTO mainProtect(Protect protect) {
        ProtectDTO protectDTO = new ProtectDTO();
        protectDTO.setProNo(protect.getProNo());
        protectDTO.setProDate(protect.getProDate());
        protectDTO.setProPlace(protect.getProPlace());
        protectDTO.setImgUrl(protect.getProImgUrl());
        return protectDTO;
    }

    public ProtectDTO myProtect(Protect protect) {
        ProtectDTO protectDTO = new ProtectDTO();
        protectDTO.setProNo(protect.getProNo());
        protectDTO.setProDate(protect.getProDate());
        protectDTO.setProKind(protect.getProKind());
        protectDTO.setProTitle(protect.getProTitle());
        protectDTO.setProPlace(protect.getProPlace());
        protectDTO.setProtectCreateDate(protect.getCreateTime());
        return protectDTO;
    }
//     public LostDTO myLost(Lost lost) {
//        LostDTO lostDTO = new LostDTO();
//        lostDTO.setLostNo(lost.getLostNo());
//        lostDTO.setLostDate(lost.getLostDate());
//        lostDTO.setLostKind(lost.getLostKind());
//        lostDTO.setLostPlace(lost.getLostPlace());
//        lostDTO.setLostTitle(lost.getLostTitle());
//        lostDTO.setLostCreateDate(lost.getCreateTime());
//        return lostDTO;
//    }
}
