package com.keduit.dadog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// 카카오 로그인
// 데이터의 특정 테이블과 매핑된 엔티티로 사용.

@Entity // JPA 클래스를 사용.
@Getter
@Setter
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 생성 -> 엔티티 클래스에 기본 생성자가 있어야 하기 때문에 사용.
public class KakaoMember {

    @Id // 주키 활용하기위해.
    @GeneratedValue // 해당 필드값 자동 생성및증가 시키기위해.
    @Column(name="member_id")
    private Long memberId;

    private String username;
    private String email;
}
