package com.keduit.dadog.controller;

import com.keduit.dadog.dto.LostDTO;
import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/dadog/lost")
@RequiredArgsConstructor
public class LostController {

    private final LostService lostService;

    @GetMapping({"/list","/list/{page}"})
    public String LostList(SearchDTO searchDTO,
                           @PathVariable("page") Optional<Integer> page,
                           Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 20);
        Page<Lost> lostList = lostService.getLostList(searchDTO, pageable);
        System.out.println("---------------------------------");
        System.out.println(searchDTO.getSearchBy());
        System.out.println(searchDTO.getSearchQuery());
        System.out.println(lostList.getTotalElements());
        model.addAttribute("lostSearchDTO", searchDTO);
        model.addAttribute("maxPage",5);
        model.addAttribute("lostList", lostList);
        System.out.println(lostList);
        return "lost/lostList";
    }

    @GetMapping("/add")
    public String addLost(Principal principal, Model model) {
        System.out.println(principal.getName() + "-------------------------");
        if(principal.getName() != null){
            model.addAttribute("userId", principal.getName());
        }else {
            model.addAttribute("userId", "none");
        }
        model.addAttribute("lostDTO", new LostDTO());
        return "lost/lostForm";
    }

    @PostMapping("/add")
    public String addLost(LostDTO lostDTO, Principal principal, MultipartFile lostImg, Model model, BindingResult bindingResult){
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
        return "main";
    }

    @GetMapping("/{lostNo}")
    public String getLost(Principal principal, @PathVariable("lostNo") Long lostNo, Model model) {
       Lost lost =  lostService.getLost(lostNo);
       model.addAttribute("lost", lost);
       if(principal != null){
           model.addAttribute("userId", principal.getName());
       }else {
           model.addAttribute("userId", "none");
       }
       return "lost/lostDtl";
    }

    @GetMapping("/update/{lostNo}")
    public String updateLost( @PathVariable("lostNo") Long lostNo, Principal principal, Model model) {
        System.out.println("--------------------- updateLost");
        if(!lostService.lostValidation(principal.getName(), lostNo)){
            System.out.println("---------------------Val 오류");
            model.addAttribute("errorMsg", "글 작성자가 아닙니다.");
            return "lost/lostDtl";
        }
       Lost lost = lostService.getLost(lostNo);
        LostDTO lostDTO = new LostDTO();
        lostDTO.createLostDTO(lost);
        model.addAttribute("lostDTO", lostDTO);
        if(principal.getName() != null){
            model.addAttribute("userId", principal.getName());
        }else {
            model.addAttribute("userId", "none");
        }
        return "lost/lostForm";
    }

    @PostMapping("/{lostNo}")
    public String updateLost( @RequestParam(value = "lostImg", required = false) MultipartFile lostImg, Principal principal, @PathVariable("lostNo") Long lostNo, LostDTO lostDTO, Model model) {
        System.out.println("boardDTO ----------------->  : " + lostDTO.toString());
        System.out.println("imgFile -------------------> : " + lostImg.isEmpty());

        if(lostImg.isEmpty()){
            System.out.println("------------ 사진이 없으므로 withOutImg 작동");
            try{
                lostService.updateLostWithOutImg(lostDTO);
            }catch (Exception e){
                System.out.println("------- controller 에러");
                throw new RuntimeException(e);
            }
            return "redirect:/dadog/lost/list";
        }
        try {
            System.out.println("------------ 사진이 있어서 withImg 작동");
            lostService.updateLostWithImg(lostDTO, lostImg);
        } catch (Exception e) {
            System.out.println("----------- Controller 2번 에러");
            throw new RuntimeException(e);
        }
        return "redirect:/dadog/lost/list";
    }

    @DeleteMapping("/{lostNo}")
    public @ResponseBody ResponseEntity deleteLost(@PathVariable("lostNo") Long lostNo, Principal principal) {
        if(!lostService.lostValidation(principal.getName(), lostNo)){
            return new ResponseEntity<String>("게시글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
            lostService.deleteLost(lostNo);
            return new ResponseEntity(lostNo,HttpStatus.OK);
    }
}
