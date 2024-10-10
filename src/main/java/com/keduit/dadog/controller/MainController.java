package com.keduit.dadog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/dadog/main")
    public String mainPage() {
        return "main";
    }


    @GetMapping("/dadog/sign")
    public String signInPage() {
        return "sign-in";
    }

    @GetMapping("/")
    public String main() {
        return "main"; // 메인 페이지의 Thymeleaf 템플릿 이름을 반환
    }

    }



