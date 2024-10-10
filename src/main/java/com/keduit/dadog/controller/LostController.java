package com.keduit.dadog.controller;

import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/dadog/lost")
@RequiredArgsConstructor
public class LostController {

    private final LostService lostService;

    @GetMapping("/add")
    public String addLost(Principal principal, Model model) {
        System.out.println(principal.getName() + "-------------------------");
        model.addAttribute("userId", principal.getName());
        model.addAttribute("lostDTO", new LostDTO());
        return "lost/lostForm";
    }

    @PostMapping("/add")
    public String addLost(LostDTO lostDTO, Principal principal, MultipartFile lostImg, Model model, BindingResult bindingResult){
        System.out.println("boardDTO ------> " + lostDTO);
        System.out.println("img --------------> " + lostImg);
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/dadog/lost/add";
        }
        if(lostImg.isEmpty()) {
            model.addAttribute("errorMessage", "이미지를 필수로 등록하여주세요");
            return "redirect:/dadog/lost/add";
        }
        try {
            lostService.addLost(lostDTO, principal.getName(), lostImg);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/dadog/lost/add";
        }
        return "redirect:/dadog/main";
    }
}
