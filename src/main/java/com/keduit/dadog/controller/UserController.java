package com.keduit.dadog.controller;

import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

        // 로그인 성공 후 사용자 정보를 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        return user; // 로그인 처리된 사용자 반환
    }
}
