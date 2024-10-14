package com.keduit.dadog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keduit.dadog.dto.LostDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
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

    //제목
    @Column(name = "lost_title")
    private String lostTitle;

    //견종
    @Column(name = "lost_kind")
    private String lostKind;

    //실종날짜
    @Column(name = "lost_date")
    private LocalDate lostDate;

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
    @JsonIgnore // 순환 참조 방지
    private User user;

    public static Lost createLost(LostDTO lostDTO, String userName, User user) {
        Lost lost = new Lost();
        lost.setLostDate(lostDTO.getLostDate());
        lost.setLostWriter(userName);
        lost.setLostTitle(lostDTO.getLostTitle());
        lost.setLostTel(lostDTO.getLostTel());
        lost.setLostKind(lostDTO.getLostKind());
        lost.setLostPlace(lostDTO.getLostPlace());
        lost.setLostDetail(lostDTO.getLostDetail());
        lost.setUser(user);
        return lost;
    }

    public void updateLost(LostDTO lostDTO){
        this.lostTitle = lostDTO.getLostTitle();
        this.lostTel = lostDTO.getLostTel();
        this.lostDate = lostDTO.getLostDate();
        this.lostPlace = lostDTO.getLostPlace();
        this.lostDetail = lostDTO.getLostDetail();
        this.lostKind = lostDTO.getLostKind();
    }

    public void updateImg(String lostOriName, String lostImgUrl, String lostFileName) {
        this.lostImgUrl = lostImgUrl;
        this.lostFileName = lostFileName;
        this.lostOriName = lostOriName;
    }
}
