package com.keduit.dadog.controller;

import com.keduit.dadog.entity.Wish;
import com.keduit.dadog.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/dadog")
public class WishListController {

    @Autowired
    private WishService wishService; // WishService 주입

    @GetMapping("/wishList")
    public String myPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");

        // 로그 추가 - 사용자 이름 확인
        System.out.println("UserName from session: " + userName);

        // 찜 목록 가져오기
        List<Wish> wishList = wishService.getWishList(userName)
        model.addAttribute("wishList", wishList); // 모델에 찜 목록 추가

        return "wishList/wishList";
    }
}