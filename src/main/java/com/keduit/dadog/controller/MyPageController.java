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
    public String memberForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        KakaoMember kakaoMember = (KakaoMember) session.getAttribute("member");

        if (kakaoMember != null) {
            // 세션에 있는 KakaoMember 정보를 모델에 추가
            model.addAttribute("username", kakaoMember.getUsername());
            model.addAttribute("email", kakaoMember.getEmail());
            session.setAttribute("isLogin", true);
        } else {
            // 로그인 정보가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 유저 정보를 넘길 DTO 추가
        model.addAttribute("userDTO", new UserDTO());

        // mypage 템플릿으로 이동
        return "mypage/mypage";
    }

}
//    @GetMapping("/dadog/myPage")
//    public String doHandle(HttpServletRequest request, Model model) throws ServletException {
//        HttpSession session = request.getSession();
//        KakaoMember kakaoMember = (KakaoMember) session.getAttribute("member");
//
//        if (kakaoMember != null) {
//            model.addAttribute("username", kakaoMember.getUsername());
//            model.addAttribute("email", kakaoMember.getEmail());
//            session.setAttribute("isLogin", true);
//        } else {
//            return "redirect:/login"; // 로그인 페이지로 리다이렉트
//        }
//
//        return "mypage/mypage"; // 수정된 경로
//    }


