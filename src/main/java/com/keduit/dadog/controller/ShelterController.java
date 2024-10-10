package com.keduit.dadog.controller;

import com.keduit.dadog.entity.Shelter;
import com.keduit.dadog.repository.ShelterRepository;
import com.keduit.dadog.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShelterController {

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private ShelterService shelterService;

    private static final int PAGE_SIZE = 7; // 한 페이지에 보여줄 보호소 수
    private static final int PAGE_GROUP_SIZE = 5; // 페이지 그룹 크기 (1~5, 6~10)

    @GetMapping("/dadog/shelter/api")
    public String fetchAndSaveShelterData() {
        shelterService.fetchAndSaveShelterData(); // 서비스 메서드 호출
        return "shelter/shelter";
    }

    @GetMapping("/dadog/shelter")
    public String getShelters(Model model,
                              @RequestParam(required = false, defaultValue = "all") String filter,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false, defaultValue = "1") int page) {

        // 페이지 번호가 1보다 작으면 1로 설정
        if (page < 1) {
            page = 1;
        }

        Page<Shelter> paginatedShelters;
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("name".equals(filter)) {
                paginatedShelters = shelterRepository.findByShelNmContaining(keyword.trim(), pageRequest);
            } else if ("city".equals(filter)) {
                paginatedShelters = shelterRepository.findByShelCityContaining(keyword.trim(), pageRequest);
            } else if ("all".equals(filter)) {
                paginatedShelters = shelterRepository.findByShelNmContainingOrShelCityContaining(keyword.trim(), keyword.trim(), pageRequest);
            } else {
                paginatedShelters = shelterRepository.findAll(pageRequest);
            }
        } else {
            paginatedShelters = shelterRepository.findAll(pageRequest);
        }

        // 페이지네이션 범위 계산
        int currentGroupStart = ((page - 1) / PAGE_GROUP_SIZE) * PAGE_GROUP_SIZE + 1;
        int currentGroupEnd = Math.min(currentGroupStart + PAGE_GROUP_SIZE - 1, paginatedShelters.getTotalPages());

        model.addAttribute("shelters", paginatedShelters.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword != null ? keyword.trim() : "");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedShelters.getTotalPages());
        model.addAttribute("currentGroupStart", currentGroupStart);
        model.addAttribute("currentGroupEnd", currentGroupEnd);

        return "shelter/shelter";
    }

}
