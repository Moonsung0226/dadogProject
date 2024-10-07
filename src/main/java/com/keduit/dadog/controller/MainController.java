package com.keduit.dadog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/dadoc/main")
    public String mainPage() {
        return "main";
    }


    @GetMapping("/dadoc/sign")
    public String signInPage() {
        return "sign-in";
    }

//    @GetMapping("/dadoc/login")
//    public String loginPage() {
//        return "sign-in";
//    }

}


