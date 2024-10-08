package com.keduit.dadog.controller;

import com.keduit.dadog.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 카카오 로그인 html
// 홈컨트롤러는 로그인하고 인덱스.html을 렌더링.

@RequiredArgsConstructor
@Controller
public class KakaoHomeController {   // final로 설정하여 파이널로 선언된 필드를 이용하여 생성자를 자동으로 생성하기 위해서 어노테이션 붙여줌. 여기서는 kakaoservice를 주입.

    private final KakaoService kakaoService;

    @GetMapping("/dadog/login")
    public String login(Model model) { // 웹페이지로 사용자를 리다이렉트 하고, 해당 페이지에서는 kakao 로그인 url을 사용할수 있도록 모델에 추가
            model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());

        return "/member/sign-in";
    }


}
