package com.keduit.dadog.repository;

import com.keduit.dadog.domain.KakaoMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 카카오 로그인
// 회원정보를 저장하는 인터페이스.
@Repository
public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long> {
// JPA확장한 인터페이스.
    KakaoMember save(KakaoMember kakaoMember); //회원정보 저장. JPA에서 제공하는 메서드 사용.데이터 베이스 저장하는 기능.
    // save() 메서드는 JpaRepository에서 이미 제공되므로 별도로 선언할 필요가 없음
}
