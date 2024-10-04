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
public class Lost extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_no")
    private Long lostNo;

    //작성자
    @Column(name = "lost_writer")
    private String lostWriter;

    //연락처
    @Column(name = "lost_tel")
    private String lostTel;

    //견종
    @Column(name = "lost_kind")
    private String lostKind;

    //실종날짜
    @Column(name = "lost_date")
    private LocalDateTime lostDate;

    //분실장소
    @Column(name = "lost_place")
    private String lostPlace;

    //특징
    @Column(name = "lost_detail")
    private String lostDetail;

    //사진Url
    @Column(name = "lost_imgurl")
    private String lostImgUrl;

    //사진 ori
    @Column(name = "lost_oriname")
    private String lostOriName;

    //사진 fileName
    @Column(name = "lost_filename")
    private String lostFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

}
