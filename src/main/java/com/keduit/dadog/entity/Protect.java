package com.keduit.dadog.entity;


import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.dto.ProtectDTO;
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

    //제목
    @Column(name = "pro_title")
    private String proTitle;

    //특이사항
    @Column(name = "pro_detail")
    private String proDetail;

    //발견날짜
    @Column(name = "pro_date")
    private LocalDate proDate;

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

    public static Protect createProtect(ProtectDTO protectDTO, String userName, User user) {
        Protect protect = new Protect();
        protect.setProDate(protectDTO.getProDate());
        protect.setProWriter(userName);
        protect.setProTitle(protectDTO.getProTitle());
        protect.setProTel(protectDTO.getProTel());
        protect.setProKind(protectDTO.getProKind());
        protect.setProPlace(protectDTO.getProPlace());
        protect.setProDetail(protectDTO.getProDetail());
        protect.setUser(user);
        return protect;
    }
    public void updateProtect(ProtectDTO protectDTO){
        this.proTitle = protectDTO.getProTitle();
        this.proTel = protectDTO.getProTel();
        this.proDate = protectDTO.getProDate();
        this.proPlace = protectDTO.getProPlace();
        this.proDetail = protectDTO.getProDetail();
        this.proKind = protectDTO.getProKind();
    }

    public void updateImg(String lostOriName, String lostImgUrl, String lostFileName) {
        this.proImgUrl = lostImgUrl;
        this.proFileName = lostFileName;
        this.proOriName = lostOriName;
    }
}
