package com.keduit.dadog.entity;

import com.keduit.dadog.constant.Current;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Adopt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_no")
    private Long AdoptNo;

    //유기견 사진
    @Column(name= "adopt_imgurl")
    private String adoptImgUrl;

    //견종
    @Column(name = "adopt_kind")
    private String adoptKind;

    //나이
    @Column(name = "adopt_age")
    private String adoptAge;

    //공고 종료일
    @Column(name = "adopt_edt")
    private String adoptEdt;

    //보호소 이름
    @Column(name = "adopt_carenm")
    private String adoptCareNm;

    //보호소 주소
    @Column(name = "adopt_careaddr")
    private String adoptCareAddr;

    //보호소 전화번호
    @Column(name = "adopt_caretel")
    private String adoptCareTel;

    //체중
    @Column(name = "adopt_weight")
    private String adoptWeight;

    //특이사항
    @Column(name = "adopt_special")
    private String adoptSpecial;

    //현황
    @Column(name = "current")
    @Enumerated(EnumType.STRING)
    private Current current;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_no")
    private List<Application> applicationList = new ArrayList<>(); // adopt 가 삭제되면 해당하는 adoptApplication 이 다 지워질 것

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_no")
    private List<Wish> wishList = new ArrayList<>();
}
