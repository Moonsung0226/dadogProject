package com.keduit.dadog.dto;

import com.keduit.dadog.constant.Current;
import com.keduit.dadog.entity.Adopt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

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

    public AdoptDTO mainAdopt(Adopt adopt){
        AdoptDTO adoptDTO = new AdoptDTO();
        adoptDTO.setAdopt_no(adopt.getAdoptNo());
        adoptDTO.setAdopt_imgurl(adopt.getAdoptImgUrl());
        adoptDTO.setAdopt_kind(adopt.getAdoptKind());
        adoptDTO.setAdopt_edt(adopt.getAdoptEdt());
        adoptDTO.setAdopt_carenm(adopt.getAdoptCareNm());
        return adoptDTO;
    }
}
