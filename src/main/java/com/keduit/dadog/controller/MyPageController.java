package com.keduit.dadog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {

    // 마이페이지
    @GetMapping("/dadog/myPage") // 경로 앞에 '/' 추가
    public String mypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        return "mypage/mypage";
    }

}