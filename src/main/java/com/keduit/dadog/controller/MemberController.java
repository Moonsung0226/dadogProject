package com.keduit.dadog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.service.KakaoService;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dadog/members")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "member/MemberForm"; // 회원가입 폼
    }

    @PostMapping("/new")
    public String registerUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        // 비밀번호와 비밀번호 확인 검사
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userDTO", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 길이 검사 (최소 8자)
        if (userDTO.getPassword().length() < 8) {
            bindingResult.rejectValue("password", "error.userDTO", "비밀번호는 최소 8자 이상이어야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/dadog/main"; // 에러가 있을 경우 원래 폼으로 돌아가기
        }

        // 사용자 등록 로직
        try {
            userService.registerMember(userDTO);
            redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/dadog/main"; // 성공적으로 등록된 경우 로그인 페이지로 리다이렉트
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/MemberForm"; // 오류 발생 시 폼으로 돌아가기
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "member/sign-in"; // 로그인 페이지로 이동
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "성공적으로 로그아웃되었습니다.");
        return "redirect:/dadog/main";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("errorMessage", "아이디 또는 비밀번호를 확인해 주세요.");
        return "member/sign-in";
    }

    // 이용약관동의
    @GetMapping("/UseAgree")
    public String UseAgree(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "member/UseAgree"; // 회원가입 폼
    }
}
