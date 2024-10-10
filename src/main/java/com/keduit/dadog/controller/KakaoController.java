package com.keduit.dadog.controller;


//import com.keduit.dadog.dto.KakaoDTO;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.KakaoMsgEntity;
import com.keduit.dadog.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

// 카카오 로그인
// 카카오 서비스를 통해 상호작용하여 카카오 DTO를 받아옴
@RestController // RESTFul 웹 서비스의 컨트롤러
@RequiredArgsConstructor // final로 선언된 필드를 가지고 생성자를 자동으로 생성
@RequestMapping("kakao") // 해당 컨트롤러의 모든 메서드에 대한 기본 경로를 /kakao 로 지정.
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserDTO kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));

        if (kakaoInfo != null) {
            // 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", kakaoInfo);
            session.setAttribute("isLoggedIn", true);

            // 사용자 인증 정보 생성
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    kakaoInfo.getEmail(), null, new ArrayList<>()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 메인 페이지로 리다이렉트
            response.sendRedirect("http://localhost:8082/dadog/main");
        } else {
            response.sendRedirect("http://localhost:8082/dadog/login?error=true");
        }
    }

}