package com.keduit.dadog.controller;


import com.keduit.dadog.dto.KakaoDTO;
import com.keduit.dadog.entity.KakaoMsgEntity;
import com.keduit.dadog.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

// 카카오 로그인
// 카카오 서비스를 통해 상호작용하여 카카오 DTO를 받아옴
@RestController // RESTFul 웹 서비스의 컨트롤러
@RequiredArgsConstructor // final로 선언된 필드를 가지고 생성자를 자동으로 생성
@RequestMapping("kakao") // 해당 컨트롤러의 모든 메서드에 대한 기본 경로를 /kakao 로 지정.
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback") // -> /kakao/callback 경로로 들어오면 카카오 서비스를 통해 카카오 API 상호 작용하여 카카오DTO를 받아오게됨.
    public ResponseEntity<KakaoMsgEntity> callback(HttpServletRequest request) throws Exception {
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code")); // 카카오 서비스에서 카카오 인포를 호출하여서 카카오API로 부터 정보를 얻어옴.

        return ResponseEntity.ok() // 얻은 정보를 msg엔티티에 담아서 리스폰스 엔티티로 반환을 함.
                //// 종합적으로 카카오컨트롤러는 카카오API콜백을 처리하는 엔드포인트를 제공하고, 사용자의 카카오 인증 코드를 이용하여 카카오API정보를 얻어오고 이를 반환하는 역할을 함.
                .body(new KakaoMsgEntity("Success", kakaoInfo));
        //HTTP 응답을 생성, OK 응답을 반환하며, 본문(body)으로 MsgEntity 객체를 담고 있음.
    }

}
