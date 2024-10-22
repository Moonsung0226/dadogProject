package com.keduit.dadog.controller;

import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 카카오 로그인 처리
    @PostMapping("/kakao/login")
    public User kakaoLogin(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = userService.kakaoLogin(userDTO);
        System.out.println("카카오 로그인 ------>" + user.getRole());
        // 로그인 성공 후 사용자 정보를 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        return user; // 로그인 처리된 사용자 반환
    }

    // 로그인할때 유효성 검사
    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pwd, RedirectAttributes redirectAttributes) {
        // UserDTO 객체를 빌더 패턴으로 생성
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .password(pwd)
                .build();

        // 사용자 검증 로직
        boolean isValidUser = userService.isValidUser(userDTO);

        if (!isValidUser) {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 로그인 성공 로직
        return "redirect:/main"; // 로그인 성공 후 리다이렉트
    }
}