package com.keduit.dadog.controller;

import javax.validation.Valid;
import com.keduit.dadog.dto.MemberDTO;
import com.keduit.dadog.entity.Member;
import com.keduit.dadog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dadoc/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/MemberForm";
    }


    @PostMapping("/new")
    public String memberForm(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/MemberForm";
        }

        try {
            Member member = Member.createMember(memberDTO, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/"; // 가입 후 메인 페이지로 리다이렉트
    }

    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm"; // 경로 수정
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요.");
        return "member/memberLoginForm"; // 경로 수정
    }
}
