package com.keduit.dadog.controller;

import com.keduit.dadog.dto.AdoptDTO;
import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.dto.ProtectDTO;
import com.keduit.dadog.service.AdoptService;
import com.keduit.dadog.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/dadog/main")
    public String mainPage(Model model) {
        List<AdoptDTO> adoptDTOList = mainService.getMainAdopts();
        List<LostDTO> lostDTOList = mainService.getMainLosts();
        List<ProtectDTO> protectDTOList = mainService.getMainProtects();
        model.addAttribute("adoptDTOList", adoptDTOList);
        model.addAttribute("lostDTOList", lostDTOList);
        model.addAttribute("protectDTOList", protectDTOList);
        return "main";
    }


    @GetMapping("/dadog/sign")
    public String signInPage() {
        return "member/sign-in";
    }

    @GetMapping("/")
    public String main() {
        return "main"; // 메인 페이지의 Thymeleaf 템플릿 이름을 반환
    }

    }



