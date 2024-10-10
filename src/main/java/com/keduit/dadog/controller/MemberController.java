package com.keduit.dadog.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dadog/members")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "member/MemberForm";
    }

    @PostMapping("/new")
    public String memberForm(@Valid UserDTO userDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/MemberForm";
        }

        try {
            // 회원 등록 메서드 호출
            userService.registerMember(userDTO); // 회원 등록
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/MemberForm";
        }

        redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/";
    }



    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션 무효화
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "성공적으로 로그아웃되었습니다.");
        return "redirect:/";
    }


    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요.");
        return "member/memberLoginForm"; // 경로 수정
    }

    @PostMapping("/members/new")
    public String registerUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult, Model model) {
        // 비밀번호와 비밀번호 확인 검사
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userDTO", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 길이 검사 (최소 8자)
        if (userDTO.getPassword().length() < 8) {
            bindingResult.rejectValue("password", "error.userDTO", "비밀번호는 최소 8자 이상이어야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            return "members/new"; // 에러가 있을 경우 원래 폼으로 돌아가기
        }

        // 사용자 등록 로직
        userService.registerMember(userDTO); // 메소드 이름 변경
        return "redirect:/login"; // 성공적으로 등록된 경우 로그인 페이지로 리다이렉트
    }


}
