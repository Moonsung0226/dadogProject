package com.keduit.dadog.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Wish extends BaseTimeEntity{
    // 유저가 찜 목록에 추가하면 해당 유기견의 번호를 받아와야함
    // 해당 유기견의 상태가 APPROVED, REJECTED 일 경우 표출하면 안됨

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_no")
    private Long wishNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_no")
    private Adopt adopt;
}
