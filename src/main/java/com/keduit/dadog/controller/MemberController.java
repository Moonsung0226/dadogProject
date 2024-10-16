package com.keduit.dadog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.service.KakaoService;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

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

        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
        if (model.containsAttribute("errorMessage")) {
            String errorMessage = (String) model.getAttribute("errorMessage");
            model.addAttribute("errorMessage", errorMessage);
        }
        return "member/sign-in"; // 로그인 페이지로 이동
    }

    // 아이디 중복 체크
    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam("id") String id) {
        boolean exists = userService.isIdDuplicate(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 이메일 중복 체크
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.isEmailDuplicate(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "성공적으로 로그아웃되었습니다.");
        return "redirect:/dadog/main";
    }

    @GetMapping("/login/error")
    public String loginError(Model model, @RequestParam(required = false) String error, RedirectAttributes redirectAttributes) {
        // 로그인 오류 처리
        String errorMessage = "아이디 또는 비밀번호를 확인해 주세요."; // 기본 오류 메시지

        if (error != null) {
            // error 매개변수에 따라 오류 메시지 설정
            if ("탈퇴한 회원입니다.".equals(error)) {
                errorMessage = "탈퇴한 회원입니다."; // 탈퇴 메시지 설정
            }
        }

        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/dadog/members/login"; // 로그인 페이지로 이동
    }

    // 이용약관동의
    @GetMapping("/UseAgree")
    public String UseAgree(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "member/UseAgree"; // 회원가입 폼
    }
}
