package com.keduit.dadog.entity;

import com.keduit.dadog.constant.AdoptWait;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Application extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoptapp_no")
    private Long appNo; // 신청 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no") // 신청자
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_no") // 입양 신청한 유기견
    private Adopt adopt;

    @Enumerated(EnumType.STRING)
    @Column(name = "adopt_wait_status")
    private AdoptWait adoptWaitStatus = AdoptWait.PENDING;  // 기본 상태는 'PENDING'

    // adoptWaitStatus 설정 메서드
    public void setStatus(String status) {
        try {
            this.adoptWaitStatus = AdoptWait.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid adopt wait status: " + status);
        }
    }
}