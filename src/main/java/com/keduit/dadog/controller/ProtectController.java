package com.keduit.dadog.controller;

import com.keduit.dadog.dto.ProtectDTO;
import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Protect;
import com.keduit.dadog.service.ProtectService;
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
@RequestMapping("/dadog/protect")
@RequiredArgsConstructor
public class ProtectController {

    private final ProtectService protectService;

    @GetMapping({"/list","/list/{page}"})
    public String ProtectList(SearchDTO searchDTO,
                           @PathVariable("page") Optional<Integer> page,
                           Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 20);
        Page<Protect> protectList = protectService.getProtectList(searchDTO, pageable);
        System.out.println("---------------------------------");
        System.out.println(searchDTO.getSearchBy());
        System.out.println(searchDTO.getSearchQuery());
        System.out.println(protectList.getTotalElements());
        model.addAttribute("protectSearchDTO", searchDTO);
        model.addAttribute("maxPage",5);
        model.addAttribute("protectList", protectList);
        System.out.println(protectList);
        return "protect/protectList";
    }

    @GetMapping("/add")
    public String addProtect(Principal principal, Model model) {
        System.out.println("------------------------->" + principal.getName());
        if(principal.getName() != null){
            model.addAttribute("userId", principal.getName());
        }else {
            model.addAttribute("userId", "none");
        }
        model.addAttribute("protectDTO", new ProtectDTO());
        return "protect/protectForm";
    }

    @PostMapping("/add")
    public String addProtect(ProtectDTO protectDTO, Principal principal, MultipartFile protectImg, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/dadog/protect/add";
        }
        if(protectImg.isEmpty()) {
            model.addAttribute("errorMessage", "이미지를 필수로 등록하여주세요");
            return "redirect:/dadog/protect/add";
        }
        try {
            protectService.addProtect(protectDTO, principal.getName(), protectImg);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/dadog/protect/add";
        }
        return "main";
    }

    @GetMapping("/{proNo}")
    public String getProtect(Principal principal, @PathVariable("proNo") Long proNo, Model model) {
        Protect protect =  protectService.getProtect(proNo);
        model.addAttribute("protect", protect);
        if(principal != null){
            model.addAttribute("userId", principal.getName());
        }else {
            model.addAttribute("userId", "none");
        }
        return "protect/protectDtl";
    }

    @GetMapping("/update/{proNo}")
    public String updateProtect( @PathVariable("proNo") Long proNo, Principal principal, Model model) {
        System.out.println("--------------------- updateProtect");
        if(!protectService.protectValidation(principal.getName(), proNo)){
            System.out.println("---------------------Val 오류");
            model.addAttribute("errorMsg", "글 작성자가 아닙니다.");
            return "protect/protectDtl";
        }
        Protect protect = protectService.getProtect(proNo);
        ProtectDTO protectDTO = new ProtectDTO();
        protectDTO.createProtectDTO(protect);
        model.addAttribute("protectDTO", protectDTO);
        if(principal.getName() != null){
            model.addAttribute("userId", principal.getName());
        }else {
            model.addAttribute("userId", "none");
        }
        return "protect/protectForm";
    }

    @PostMapping("/{proNo}")
    public String updateProtect( @RequestParam(value = "protectImg", required = false) MultipartFile protectImg, Principal principal, @PathVariable("proNo") Long proNo, ProtectDTO protectDTO, Model model) {
        System.out.println("protectDTO ----------------->  : " + protectDTO.toString());
        System.out.println("imgFile -------------------> : " + protectImg.isEmpty());

        if(protectImg.isEmpty()){
            System.out.println("------------ 사진이 없으므로 withOutImg 작동");
            try{
                protectService.updateProtectWithOutImg(protectDTO);
            }catch (Exception e){
                System.out.println("------- controller 에러");
                throw new RuntimeException(e);
            }
            return "redirect:/dadog/protect/list";
        }
        try {
            System.out.println("------------ 사진이 있어서 withImg 작동");
            protectService.updateProtectWithImg(protectDTO, protectImg);
        } catch (Exception e) {
            System.out.println("----------- Controller 2번 에러");
            throw new RuntimeException(e);
        }
        return "redirect:/dadog/protect/list";
    }

    @DeleteMapping("/{proNo}")
    public @ResponseBody ResponseEntity deleteProtect(@PathVariable("proNo") Long proNo, Principal principal) {
        if(!protectService.protectValidation(principal.getName(), proNo)){
            return new ResponseEntity<String>("게시글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        protectService.deleteProtect(proNo);
        return new ResponseEntity(proNo,HttpStatus.OK);
    }
}
