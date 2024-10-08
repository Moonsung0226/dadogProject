package com.keduit.dadog.dto;

import com.keduit.dadog.constant.Current;
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
}
