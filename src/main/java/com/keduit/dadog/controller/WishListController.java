package com.keduit.dadog.controller;

import com.keduit.dadog.entity.User;
import com.keduit.dadog.entity.Wish;
import com.keduit.dadog.service.WishService;
import com.keduit.dadog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dadog")
public class WishListController {

    @Autowired
    private WishService wishService;

    @Autowired
    private UserService userService;

    @GetMapping("/wishList")
    public String getWishList(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        try {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);

            if (user == null) {
                model.addAttribute("message", "사용자 정보를 찾을 수 없습니다.");
                return "wishList/wishList";
            }

            List<Wish> wishList = wishService.getWishListByUser(user);
            model.addAttribute("wishList", wishList);

        } catch (Exception e) {
            model.addAttribute("message", "찜 목록을 불러오는 중 오류가 발생했습니다.");
        }

        return "wishList/wishList";
    }
}