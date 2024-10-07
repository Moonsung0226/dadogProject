package com.keduit.dadog.controller;

import com.keduit.dadog.entity.Shelter;
import com.keduit.dadog.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShelterController {

    @Autowired
    private ShelterRepository shelterRepository;

    @GetMapping("/dadoc/shelter")
    public String getShelters(Model model,
                              @RequestParam(required = false, defaultValue = "all") String filter,
                              @RequestParam(required = false) String keyword) {
        List<Shelter> shelters;

        // 검색어가 있는 경우 필터에 따라 검색
        if ("name".equals(filter) && keyword != null && !keyword.trim().isEmpty()) {
            shelters = shelterRepository.findByShelNmContaining(keyword.trim());
        } else if ("city".equals(filter) && keyword != null && !keyword.trim().isEmpty()) {
            shelters = shelterRepository.findByShelCityContaining(keyword.trim());
        } else if ("all".equals(filter) && keyword != null && !keyword.trim().isEmpty()) {
            shelters = shelterRepository.findByShelNmAndShelCityContaining(keyword.trim());
        }
        else {
            shelters = shelterRepository.findAll(); // 기본적으로 모든 보호소 표시
        }

        // 모델에 보호소 정보 추가
        model.addAttribute("shelters", shelters);
        model.addAttribute("keyword", keyword); // 검색어를 다시 폼에 표시

        return "shelter/shelter";
    }
}
