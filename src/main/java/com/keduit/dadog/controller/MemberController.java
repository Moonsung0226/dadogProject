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
            User user = User.createUser(userDTO, passwordEncoder);
            userService.saveUser(user);
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

}
