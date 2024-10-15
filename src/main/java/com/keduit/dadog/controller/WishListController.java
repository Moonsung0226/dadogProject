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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/dadog")
public class WishListController {


    @Autowired
    private WishService wishService;

    @Autowired
    private UserService userService;

    @GetMapping("/wishList")
    public String getWishList(HttpServletRequest request, Model model) {

    }
}