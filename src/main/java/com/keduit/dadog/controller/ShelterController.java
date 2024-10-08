package com.keduit.dadog.controller;

import com.keduit.dadog.entity.Shelter;
import com.keduit.dadog.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ShelterController {

    @Autowired
    private ShelterRepository shelterRepository;

    @GetMapping("/dadog/shelter")
    public String getShelters(Model model,
                              @RequestParam(required = false, defaultValue = "all") String filter,
                              @RequestParam(required = false) String keyword) {
        List<Shelter> shelters = new ArrayList<>();

        // 검색어가 있는 경우 필터에 따라 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("name".equals(filter)) {
                shelters = shelterRepository.findByShelNmContaining(keyword.trim());
            } else if ("city".equals(filter)) {
                shelters = shelterRepository.findByShelCityContaining(keyword.trim());
            } else if ("all".equals(filter)) {
                shelters.addAll(shelterRepository.findByShelNmContaining(keyword.trim()));
                List<Shelter> cityShelters = shelterRepository.findByShelCityContaining(keyword.trim());
                shelters.addAll(cityShelters);
                shelters = shelters.stream().distinct().collect(Collectors.toList()); // 중복 제거
            }
        } else {
            shelters = shelterRepository.findAll(); // 기본적으로 모든 보호소 표시
        }

        // 모델에 보호소 정보 추가
        model.addAttribute("shelters", shelters);
        model.addAttribute("filter", filter); // 선택된 필터
        model.addAttribute("keyword", keyword != null ? keyword.trim() : ""); // 검색어를 다시 폼에 표시

        return "shelter/shelter";
    }
}
