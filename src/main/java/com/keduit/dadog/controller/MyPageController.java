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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dadog")
public class MyPageController {

    private final UserService userService; // UserService 필드

    public MyPageController(UserService userService) {
        this.userService = userService; // 생성자 주입
    }


    // 마이페이지
    @GetMapping("/myPage") // 경로 앞에 '/' 추가
    public String myPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        return "myPage/myPage";
    }


    @GetMapping("/myPage/myWriting")
    public String myWriting(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 게시글 리스트를 빈 리스트로 초기화
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts); // 빈 리스트 추가

        return "myPage/myWriting"; // Thymeleaf 템플릿 경로
    }

    // GET 요청 처리: 회원 정보 가져오기
    @GetMapping("/myPage/myMemberForm")
    public String myMemberForm(HttpServletRequest request, Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserDTO userDTO = new UserDTO();
        userDTO.createUserDTO(user);
        model.addAttribute("userDTO", userDTO); // 세션에서 가져온 사용자 정보를 모델에 추가

        return "myPage/myMemberForm"; // Thymeleaf 템플릿 경로
    }

    // POST 요청 처리: 회원 정보 수정
    @PostMapping("/myPage/myMemberForm")
    public String updateMember(UserDTO userDTO, Model model) {
        // 서비스 호출하여 회원 정보 수정
        userService.updateUser(userDTO); // 사용자 정보를 수정하는 로직

        model.addAttribute("successMessage", "회원 정보가 성공적으로 변경되었습니다.");
        model.addAttribute("userDTO", userDTO); // 수정된 사용자 정보 모델에 추가

        return "myPage/myMemberForm"; // 변경 후 같은 페이지로 리다이렉션
    }


    @GetMapping("/myPage/myLost")
    public String myLost(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 게시글 리스트를 빈 리스트로 초기화
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts); // 빈 리스트 추가

        return "myPage/myLost"; // Thymeleaf 템플릿 경로
    }

    @GetMapping("/myPage/myAdopt")
    public String myAdopt(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 게시글 리스트를 빈 리스트로 초기화
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts); // 빈 리스트 추가

        return "myPage/myAdopt"; // Thymeleaf 템플릿 경로
    }


    @GetMapping("/myPage/myProtect")
    public String myProtecting(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 게시글 리스트를 빈 리스트로 초기화
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts); // 빈 리스트 추가

        return "myPage/myProtect"; // Thymeleaf 템플릿 경로
    }

}