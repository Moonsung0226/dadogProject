package com.keduit.dadog.controller;

import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.service.AdoptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdoptController {

    @Autowired
    private AdoptService adoptService;

    @GetMapping("/dadog/adopt")
    public String adopt(Model model) {
        List<Adopt> adoptList =adoptService.getAdoptList();
        model.addAttribute("adoptList",adoptList);
        System.out.println(adoptList);
        return "adopt/adoptList";
    }

}
