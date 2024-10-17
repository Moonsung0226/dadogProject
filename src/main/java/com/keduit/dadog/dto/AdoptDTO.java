package com.keduit.dadog.dto;

import com.keduit.dadog.constant.Current;
import com.keduit.dadog.entity.Adopt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdoptDTO {
    private Long adopt_no;
    private String adopt_age;
    private String adopt_careaddr;
    private String adopt_carenm;
    private String adopt_caretel;
    private String adopt_edt;
    private String adopt_imgurl;
    private String adopt_kind;
    private String adopt_special;
    private String adopt_weight;
    private Current current;

    private String adoptWaitStatus; // 대기 상태 필드
    private Long id;              // 입양 ID
    private String title;         // 입양 제목
    private String description;   // 입양 설명
    private String userEmail;     // 사용자 이메일 추가




    // Adopt 객체를 이용하여 AdoptDTO를 생성하는 메서드
    public AdoptDTO mainAdopt(Adopt adopt) {
        AdoptDTO adoptDTO = new AdoptDTO();
        adoptDTO.setAdopt_no(adopt.getAdoptNo());
        adoptDTO.setAdopt_imgurl(adopt.getAdoptImgUrl());
        adoptDTO.setAdopt_kind(adopt.getAdoptKind());
        adoptDTO.setAdopt_edt(adopt.getAdoptEdt());
        adoptDTO.setAdopt_carenm(adopt.getAdoptCareNm());

        // adoptWaitStatus를 "PENDING"으로 설정
        adoptDTO.setAdoptWaitStatus("PENDING");

        return adoptDTO;
    }
}
