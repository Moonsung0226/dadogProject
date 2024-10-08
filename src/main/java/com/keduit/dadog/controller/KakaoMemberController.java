package com.keduit.dadog.controller;

import com.keduit.dadog.domain.KakaoMember;
import com.keduit.dadog.repository.KakaoMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

// 카카오 로그인
//간단한 마이페이지와 회원정보를 보여주기 위함.

@Controller
public class KakaoMemberController {

    @Autowired // 의존성 주입
    private KakaoMemberRepository kakaoMemberRepository;

    @GetMapping("/myPage") // 간단한 마이페이지 코드 구현해봤음.
    public void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8"); // 응답 콘텐츠 타입을 text/html로 설정하여 UTF-8 문자 인코딩을 사용.
        PrintWriter out = response.getWriter(); // 출력하기 위한 출력스트림을 얻기 위해서 PrintWriter 사용.

        //세션에서 사용자 정보를 검색 => 멤버에 해당하는 값을 멤버 객체로 얻어왔음.
        HttpSession session = request.getSession();
        KakaoMember kakaoMember = (KakaoMember) session.getAttribute("member");

        String user_name = "", user_email = "";

        // 사용자 정보가 null이 아니면 속성 저장
        if (kakaoMember != null) {
            user_name = kakaoMember.getUsername();
            user_email = kakaoMember.getEmail();

            session.setAttribute("isLogin", true); // 로그인 상태를 나타내고 true로 설정
            session.setAttribute("login.name", user_name);
            session.setAttribute("login.email", user_email);
        }

        // html형태로 출력을 해줌.
        out.print("<html><body>");
        out.print("안녕하세요 " + user_name + "님!!<br>");
        out.print("<a href='show'>회원정보보기</a>");
        out.print("</body></html>");
        out.close(); // PrintWriter 닫기
    }

    @GetMapping("/show")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        String name = "", email = "";

        HttpSession session = request.getSession(); //HttpSession을 통해 세션을 얻어옴

        // 세션에서 "login.name"과 "login.email" 속성을 얻어옴.
        name = (String) session.getAttribute("login.name");
        email = (String) session.getAttribute("login.email");

        // html형식으로 출력해즘.
        out.print("<html><body>");
        out.print("이름 :" + name + "<br>");
        out.print("이메일:" + email + "<br>");
        out.print("</body></html>");
        out.close();

        // show로 이동이 되서 아래 코드가 구현됨. 네임과 이메일 가져오도록 하였음.
    }
}
