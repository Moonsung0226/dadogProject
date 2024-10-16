package com.keduit.dadog.controller;

import com.keduit.dadog.dto.AdoptDTO;
import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.dto.WishDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Wish;
import com.keduit.dadog.service.AdoptService;
import com.keduit.dadog.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dadog/adopt")
public class AdoptController {


    private final AdoptService adoptService;
    private final WishService wishService;

    @GetMapping({"/list/{page}","/list"})
    public String adoptList(AdoptSearchDTO adoptSearchDTO,
                            @PathVariable("page") Optional<Integer> page,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 30);
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

    @GetMapping("/{adoptNo}")
    public String adoptDtl(@PathVariable("adoptNo") Long adopt_no, Model model, Principal principal) {
        if(principal != null){
            model.addAttribute("userId", principal.getName());
        }else {
            model.addAttribute("userId", "none");
        }
        Adopt adopt = adoptService.getAdopt(adopt_no);
        model.addAttribute("adopt", adopt);
        return "adopt/adoptDtl";
    }

    @PostMapping("/addWish")
    public @ResponseBody ResponseEntity addWish(@RequestBody WishDTO wishDTO, Principal principal) {
        String userName = principal.getName();
        Long wishNo;
        wishNo = wishService.addWish(wishDTO, userName);
        if(wishNo == 0L) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(wishNo, HttpStatus.OK);
    }
}
