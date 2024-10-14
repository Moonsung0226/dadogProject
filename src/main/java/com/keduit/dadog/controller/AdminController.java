package com.keduit.dadog.controller;

import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.*;
import com.keduit.dadog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        return "redirect:/dadog/main";
    }

    @GetMapping("/dadog/admin/adopt/updateApi")
    public String updateApi() {
        adoptApiService.fetchAndUpdateAdoptData();
        return "redirect:/dadog/main";
    }

    @GetMapping("/dadog/admin/main")
    public String mainPage(Model model) {
        List<Lost> recentLostList = lostService
                .findTop6ByOrderByLostDateDesc(); // Lost 6개
        List<Adopt> recentAdoptList = adoptService
                .findTop6ByOrderByAdoptEdtDesc(); // Adopt 6개
        List<Board> recentBoardList = boardService
                .findTop6ByOrderByCreateTimeDesc(); // Board 6개
        List<Protect> recentProtectList = protectService
                .findTop6ByOrderByCreateTimeDesc(); // Protect 6개
        List<User> recentUserList = userService
                .findTop6ByOrderByCreateTimeDesc(); // User 6개

        model.addAttribute("lostList", recentLostList);
        model.addAttribute("adoptList", recentAdoptList);
        model.addAttribute("boardList", recentBoardList);
        model.addAttribute("protectList", recentProtectList);
        model.addAttribute("userList", recentUserList);

        return "admin/adminMain";
    }

    @GetMapping({"/dadog/admin/adopt/list/{page}","/dadog/admin/adopt/list"})
    public String adoptList(AdoptSearchDTO adoptSearchDTO,
                            @PathVariable("page") Optional<Integer> page,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 30);
        Page<Adopt> adoptList =adoptService.getAdoptList(adoptSearchDTO, pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adoptList",adoptList);
        System.out.println(adoptList);
        return "admin/adminAdopt";
    }

    @GetMapping("/dadog/admin/adopt/{adoptNo}")
    public ResponseEntity<Adopt> getAdoptDetail(@PathVariable Long adoptNo) {
        Adopt adopt = adoptService.findByAdoptNo(adoptNo);
        if (adopt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adopt);
    }

    @DeleteMapping("/dadog/admin/adopt/delete/{adoptNo}")
    public ResponseEntity<Void> deleteAdopt(@PathVariable Long adoptNo) {
        try {
            adoptService.deleteAdopt(adoptNo);
            return ResponseEntity.ok().build(); // 성공적으로 삭제한 경우
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // 해당 adoptNo가 없을 경우
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 삭제 중 오류 발생
        }
    }

    @GetMapping({"/dadog/admin/lost/list/{page}","/dadog/admin/lost/list"})
    public String lostList(SearchDTO searchDTO,
                           @PathVariable("page") Optional<Integer> page,
                           Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9);
        Page<Lost> lostList = lostService.getLostList(searchDTO, pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("lostList", lostList);
        return "admin/adminLost";
    }

    @GetMapping("/dadog/admin/lost/{lostNo}")
    public ResponseEntity<Lost> getLostDetail(@PathVariable Long lostNo) {
        Lost lost = lostService.findByLostNo(lostNo);
        if (lost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lost);
    }

    @DeleteMapping("/dadog/admin/lost/delete/{lostNo}")
    public ResponseEntity<Void> deleteLost(@PathVariable Long lostNo) {
        try {
            lostService.deleteLost(lostNo);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping({"/dadog/admin/protect/list/{page}","/dadog/admin/protect/list"})
    public String protectList(SearchDTO searchDTO,
                              @PathVariable("page") Optional<Integer> page,
                              Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
        Page<Protect> protectList = protectService.getProtectList(searchDTO, pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("protectList", protectList);
        return "admin/adminProtect";
    }

    @GetMapping("/dadog/admin/protect/{protectNo}")
    public ResponseEntity<Protect> getProtectDetail(@PathVariable Long protectNo) {
        Protect protect = protectService.findByProtectNo(protectNo);
        if (protect == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(protect);
    }

    @DeleteMapping("/dadog/admin/protect/delete/{protectNo}")
    public ResponseEntity<Void> deleteProtect(@PathVariable Long protectNo) {
        try {
            protectService.deleteProtect(protectNo);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Board 관련 API
    @GetMapping({"/dadog/admin/board/list/{page}","/dadog/admin/board/list"})
    public String boardList(@PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
//        Page<Board> boardList = boardService.getBoardList(pageable);
//        model.addAttribute("maxPage", 10);
//        model.addAttribute("boardList", boardList);
        return "admin/adminBoard";
    }

    @GetMapping("/dadog/admin/board/{boardNo}")
    public ResponseEntity<Board> getBoardDetail(@PathVariable Long boardNo) {
        Board board = boardService.findByBoardNo(boardNo);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/dadog/admin/board/delete/{boardNo}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardNo) {
        try {
            boardService.deleteBoard(boardNo);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dadog/admin/user")
    public String userPage(Model model) {
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userList", userList);
        return "admin/adminUser";
    }

    @PostMapping("/dadog/admin/user/update/{userNo}/{role}")
    public ResponseEntity<String> updateUserRole(@PathVariable("userNo") Long userId,
                                                 @PathVariable("role") String role) {
        // role 문자열을 Role enum으로 변환
        Role newRole = Role.valueOf(role.toUpperCase());

        // 사용자 역할 업데이트
        userService.updateUserRole(userId, newRole);

        // 성공 메시지 반환
        return ResponseEntity.ok("사용자 역할이 성공적으로 업데이트되었습니다.");
    }

}