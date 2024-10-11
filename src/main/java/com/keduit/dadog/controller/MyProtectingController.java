package com.keduit.dadog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyProtectingController {

    @GetMapping("/dadog/myProtecting")
    public String myprotecting(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 게시글 리스트를 빈 리스트로 초기화
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts); // 빈 리스트 추가

        return "mypage/myProtecting"; // Thymeleaf 템플릿 경로
    }
}
