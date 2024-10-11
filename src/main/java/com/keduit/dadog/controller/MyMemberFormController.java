package com.keduit.dadog.controller;

import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/dadog")
public class MyMemberFormController {

    private final UserService userService; // UserService 필드

    public MyMemberFormController(UserService userService) {
        this.userService = userService; // 생성자 주입
    }

    // GET 요청 처리: 회원 정보 가져오기
    @GetMapping("/myMemberForm")
    public String myMemberForm(HttpServletRequest request, Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserDTO userDTO = new UserDTO();
        userDTO.createUserDTO(user);
        model.addAttribute("userDTO", userDTO); // 세션에서 가져온 사용자 정보를 모델에 추가

        return "mypage/myMemberForm"; // Thymeleaf 템플릿 경로
    }

    // POST 요청 처리: 회원 정보 수정
    @PostMapping("/myMemberForm")
    public String updateMember(UserDTO userDTO, Model model) {
        // 서비스 호출하여 회원 정보 수정
        userService.updateUser(userDTO); // 사용자 정보를 수정하는 로직

        model.addAttribute("successMessage", "회원 정보가 성공적으로 변경되었습니다.");
        model.addAttribute("userDTO", userDTO); // 수정된 사용자 정보 모델에 추가

        return "mypage/myMemberForm"; // 변경 후 같은 페이지로 리다이렉션
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(UserDTO userDTO, HttpServletRequest request) {
        // 로그인 로직 (예: 사용자 인증)
        if (userService.isValidUser(userDTO)) { // UserService의 메서드를 사용하여 사용자의 유효성을 검사
            HttpSession session = request.getSession(); // 현재 세션을 가져옴
            session.setAttribute("userDTO", userDTO); // UserDTO를 세션에 저장
            System.out.println("UserDTO saved to session: " + userDTO); // 디버깅용 로그
            return "redirect:/dadog/myMemberForm"; // 로그인 후 이동할 페이지
        } else {
            // 로그인 실패 시 처리
            return "redirect:/login"; // 실패 시 로그인 페이지로 리다이렉션
        }
    }




}
