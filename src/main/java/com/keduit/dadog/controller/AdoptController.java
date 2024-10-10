package com.keduit.dadog.controller;

import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.service.AdoptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdoptController {

    @Autowired
    private AdoptService adoptService;

    @GetMapping({"/dadog/adopt/list/{page}","/dadog/adopt/list"})
    public String adoptList(AdoptSearchDTO adoptSearchDTO,
                            @PathVariable("page") Optional<Integer> page,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 50);
        Page<Adopt> adoptList =adoptService.getAdoptList(adoptSearchDTO, pageable);
        System.out.println("-----------------------------------------------");
        System.out.println(adoptSearchDTO.getSearchBy());
        System.out.println(adoptSearchDTO.getSearchQuery());
        model.addAttribute("adoptSearchDTO", adoptSearchDTO);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adoptList",adoptList);
        System.out.println(adoptList);
        return "adopt/adoptList";
    }

    @GetMapping("/dadog/adopt/{adoptNo}")
    public String adoptDtl(@PathVariable("adoptNo") Long adopt_no, Model model) {
        Adopt adopt = adoptService.getAdopt(adopt_no);
        model.addAttribute("adopt", adopt);
        return "adopt/adoptDtl";
    }

}
