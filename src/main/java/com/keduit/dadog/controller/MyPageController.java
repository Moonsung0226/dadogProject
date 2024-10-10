package com.keduit.dadog.controller;

import com.keduit.dadog.domain.KakaoMember;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.repository.KakaoMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {


    @GetMapping("/dadog/myPage") // 경로 앞에 '/' 추가
    public String mypage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        return "mypage/mypage";
    }
}


