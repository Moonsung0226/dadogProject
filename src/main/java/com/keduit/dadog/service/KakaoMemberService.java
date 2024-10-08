package com.keduit.dadog.service;


import com.keduit.dadog.repository.KakaoMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 카카오 로그인
// 맴버서비스는 맴버리파지토리를 의존성 주입.

@RequiredArgsConstructor //의존성 주입
@Service
public class KakaoMemberService {



    private final KakaoMemberRepository kakaoMemberRepository;
}
