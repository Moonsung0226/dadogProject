package com.keduit.dadog.controller;

import com.keduit.dadog.entity.User;
import com.keduit.dadog.entity.Wish;
import com.keduit.dadog.service.ApplicationService;
import com.keduit.dadog.service.WishService;
import com.keduit.dadog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dadog")
public class WishListController {

    private final ApplicationService applicationService;
    private final WishService wishService;
    private final UserService userService;

    @Autowired
    public WishListController(ApplicationService applicationService, WishService wishService, UserService userService) {
        this.applicationService = applicationService;
        this.wishService = wishService;
        this.userService = userService;
    }

    // 찜 목록 페이지를 보여주는 메서드
    @GetMapping("/wishList")
    public String getWishList(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);

            if (user == null) {
                model.addAttribute("message", "사용자 정보를 찾을 수 없습니다.");
                return "wishList/wishList";
            }

            List<Wish> wishList = wishService.getWishListByUser(user);
            model.addAttribute("wishList", wishList);

        } catch (Exception e) {
            model.addAttribute("message", "찜 목록을 불러오는 중 오류가 발생했습니다.");
        }

        return "wishList/wishList";
    }

    // 찜 목록에서 항목을 삭제하는 메서드
    @DeleteMapping("/wish/{wishNo}")
    public @ResponseBody ResponseEntity deleteWish(@PathVariable("wishNo") Long wishNo, Principal principal) {
        if (!wishService.wishValidation(principal.getName(), wishNo)) {
            return new ResponseEntity<String>("게시글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        wishService.deleteWish(wishNo);
        return new ResponseEntity(wishNo, HttpStatus.OK);
    }

    // 찜목록에서 입양 신청을 처리하는 메서드
    @PostMapping("/adopt/{adoptNo}")
    @ResponseBody
    public ResponseEntity<Long> applyForAdoption(@PathVariable Long adoptNo, Principal principal) {
        if (principal == null) {  // 인증된 사용자가 없는 경우, 401 Unauthorized 상태를 반환.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userName = principal.getName(); // Principal 객체에서 사용자 이름을 가져오기.
        Long applicationId = applicationService.applyForAdoption(adoptNo, userName);  // 입양 신청을 처리하는 서비스 메서드를 호출하고, 결과로 신청 ID를 가져옴.

        return ResponseEntity.ok(applicationId);
    }
}