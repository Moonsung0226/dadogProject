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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String memberForm(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/MemberForm";
        }

        try {
            Member member = Member.createMember(memberDTO, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/MemberForm";
        }

        redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/";
    }




//    @PostMapping("/login")
//    public String loginMember(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "member/memberLoginForm"; // 로그인 폼으로 다시 돌아감
//        }
//
//        try {
//            // 로그인 로직 구현 - 아이디와 비밀번호를 사용해 인증 시도
//            // 해당 로직은 MemberService를 사용하여 처리할 수 있습니다.
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
//            return "member/memberLoginForm"; // 오류 시 다시 로그인 폼으로 이동
//        }
//
//        return "redirect:/"; // 성공 시 메인 페이지로 리다이렉트
//    }


    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요.");
        return "member/memberLoginForm"; // 경로 수정
    }
}
