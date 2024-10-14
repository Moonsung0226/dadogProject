package com.keduit.dadog.controller;

import com.keduit.dadog.service.AdoptApiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdoptApiService adoptApiService;

    @GetMapping("/dadog/admin/adopt/api")
    public String adoptApi() {
        adoptApiService.fetchAndSaveAdoptData();
        return "redirect:/dadog/main";
    }

    @GetMapping("/dadog/admin/adopt/updateApi")
    public String updateApi() {
        adoptApiService.fetchAndUpdateAdoptData();
        return "redirect:/dadog/main";
    }
}
