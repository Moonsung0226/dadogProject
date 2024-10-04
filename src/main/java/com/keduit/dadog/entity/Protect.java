package com.keduit.dadog.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Protect extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_no")
    private Long proNo;

    //작성자
    @Column(name = "pro_writer")
    private String proWriter;

    //연락처
    @Column(name = "pro_tel")
    private String proTel;

    //견종
    @Column(name = "pro_kind")
    private String proKind;

    //발견날짜
    @Column(name = "pro_date")
    private LocalDateTime proDate;

    //특징
    @Column(name = "pro_place")
    private String proPlace;

    //사진Url
    @Column(name = "pro_imgurl")
    private String proImgUrl;

    //사진 ori
    @Column(name = "pro_oriname")
    private String proOriName;

    //사진 fileName
    @Column(name = "pro_filename")
    private String proFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;
}
