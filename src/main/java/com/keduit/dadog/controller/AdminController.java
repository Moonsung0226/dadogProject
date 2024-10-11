package com.keduit.dadog.controller;

import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.*;
import com.keduit.dadog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdoptApiService adoptApiService;
    private final LostService lostService;
    private final UserService userService;
    private final AdoptService adoptService;
    private final BoardService boardService;
    private final ProtectService protectService;


    @GetMapping("/dadog/admin/adopt/api")
    public String adoptApi() {
        adoptApiService.fetchAndSaveAdoptData();
        return "/main";
    }


    @GetMapping("/dadog/admin/main")
    public String mainPage(Model model) {
        List<Lost> lostList = lostService.findAllLost(); // Lost 엔티티 목록
        List<Adopt> adoptList = adoptService.findAllAdopt(); // Adopt 엔티티 목록
        List<Board> boardList = boardService.findAllBoards(); // Board 엔티티 목록
        List<Protect> protectList = protectService.findAllProtects(); // Protect 엔티티 목록
        List<User> userList = userService.findAllUsers(); // User 엔티티 목록

        model.addAttribute("lostList", lostList);
        model.addAttribute("adoptList", adoptList);
        model.addAttribute("boardList", boardList);
        model.addAttribute("protectList", protectList);
        model.addAttribute("userList", userList);

        return "admin/adminMain"; // 적절한 템플릿 이름
    }

}
