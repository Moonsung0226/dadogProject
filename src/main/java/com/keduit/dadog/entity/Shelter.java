package com.keduit.dadog.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shel_no")
    private Long shelNo;

    //보호소 이름
    @Column(name = "shel_nm")
    private String shelNm;

    //보호소 주소
    @Column(name="shel_addr")
    private String shelAddr;

    //시도명
    @Column(name = "shel_city")
    private String shelCity;
}
