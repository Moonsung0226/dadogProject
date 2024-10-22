package com.keduit.dadog.controller;

import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.dto.ApplicationDTO;
import com.keduit.dadog.dto.BoardDTO;
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

import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdoptApiService adoptApiService;
    private final LostService lostService;
    private final UserService userService;
    private final AdoptService adoptService;
    private final BoardService boardService;
    private final ProtectService protectService;
    private final ApplicationService applicationService;
    private final ShelterService shelterService;


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

    // 메인
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

        // PENDING 상태의 입양 신청 갯수 추가
        long pendingCount = applicationService.countPendingApplications();

        model.addAttribute("lostList", recentLostList);
        model.addAttribute("adoptList", recentAdoptList);
        model.addAttribute("boardList", recentBoardList);
        model.addAttribute("protectList", recentProtectList);
        model.addAttribute("userList", recentUserList);
        model.addAttribute("pendingCount", pendingCount); // PENDING 상태 갯수 전달

        return "admin/adminMain";
    }

    // 입양하기 페이지
    @GetMapping({"/dadog/admin/adopt/list/{page}","/dadog/admin/adopt/list"})
    public String adoptList(AdoptSearchDTO adoptSearchDTO,
                            @PathVariable("page") Optional<Integer> page,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 12);
        Page<Adopt> adoptList =adoptService.getAdoptList(adoptSearchDTO, pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adoptList",adoptList);
        System.out.println(adoptList);
        return "admin/adminAdopt";
    }

    // 입양하기 상세페이지
    @GetMapping("/dadog/admin/adopt/{adoptNo}")
    public ResponseEntity<Adopt> getAdoptDetail(@PathVariable Long adoptNo) {
        Adopt adopt = adoptService.findByAdoptNo(adoptNo);
        if (adopt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adopt);
    }

    // 입양하기 삭제
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

    // 실종신고 페이지
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

    // 실종신고 상세페이지
    @GetMapping("/dadog/admin/lost/{lostNo}")
    public ResponseEntity<Lost> getLostDetail(@PathVariable Long lostNo) {
        Lost lost = lostService.findByLostNo(lostNo);
        if (lost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lost);
    }

    // 실종신고 삭제
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

    // 보호중 페이지
    @GetMapping({"/dadog/admin/protect/list/{page}","/dadog/admin/protect/list"})
    public String protectList(SearchDTO searchDTO,
                              @PathVariable("page") Optional<Integer> page,
                              Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<Protect> protectList = protectService.getProtectList(searchDTO, pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("protectList", protectList);
        return "admin/adminProtect";
    }

    // 보호중 상세페이지
    @GetMapping("/dadog/admin/protect/{protectNo}")
    public ResponseEntity<Protect> getProtectDetail(@PathVariable Long protectNo) {
        Protect protect = protectService.findByProtectNo(protectNo);
        if (protect == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(protect);
    }

    // 보호중 삭제
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

    // 게시판 페이지
    @GetMapping({"/dadog/admin/board/list/{page}","/dadog/admin/board/list"})
    public String boardList(@PathVariable("page") Optional<Integer> page,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<Board> boardList = boardService.getBoardList(pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("boardList", boardList);
        return "admin/adminBoard";
    }

    // 게시판 상세페이지
    @GetMapping("/dadog/admin/board/{boardNo}")
    public ResponseEntity<Board> getBoardDetail(@PathVariable Long boardNo) {
        Board board = boardService.findByBoardNo(boardNo);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
    }

    // 게시판 삭제
    @DeleteMapping("/dadog/admin/board/delete/{boardNo}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardNo) {
        Board board = boardService.findBoardById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        try {
            boardService.deleteBoard(boardNo);
            System.out.println("게시물 삭제 성공: " + boardNo);
            return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("게시물 삭제 중 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제 중 오류가 발생했습니다.");
        }
    }

    // 입양신청 페이지
    @GetMapping("/dadog/admin/application/list")
    public String applicationList(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "status", required = false) String status,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, 18);
        Page<ApplicationDTO> applicationList;

        if (status != null) {
            AdoptWait adoptWaitStatus = AdoptWait.valueOf(status.toUpperCase()); // status를 AdoptWait enum으로 변환
            applicationList = applicationService.getApplicationListByStatus(adoptWaitStatus, pageable);
        } else {
            applicationList = applicationService.getApplicationList(pageable);
        }

        model.addAttribute("maxPage", 10);
        model.addAttribute("applicationList", applicationList);
        return "admin/adminApplication";
    }

    // 입양신청 상세페이지
    @GetMapping("/dadog/admin/application/{appNo}")
    public ResponseEntity<ApplicationDTO> getApplicationDetail(@PathVariable Long appNo) {
        ApplicationDTO applicationDTO = applicationService.findApplicationDTOByAppNo(appNo); // DTO 반환
        if (applicationDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(applicationDTO);
    }

    // 입양신청 승인/미승인 업데이트
    @PostMapping("/dadog/admin/application/update/{appNo}")
    public ResponseEntity<Void> updateAdoptWaitStatus(@PathVariable Long appNo, @RequestBody Map<String, String> request) {
        String status = request.get("status");

        // 상태 업데이트 로직
        applicationService.updateAdoptWaitSt(appNo, status);

        return ResponseEntity.ok().build();
    }

    // 회원관리 페이지
    @GetMapping({"/dadog/admin/user/list/{page}","/dadog/admin/user/list"})
    public String userList(@PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<User> userList = userService.getUserList(pageable);
        model.addAttribute("maxPage", 10);
        model.addAttribute("userList", userList);
        return "admin/adminUser";
    }

    // 회원 권한 업데이트
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

    // api 다운 페이지
    @GetMapping("/dadog/admin/api")
    public String apiPage(Model model) {
        return "admin/adminApi";
    }

    // 보호소 api
    @GetMapping("/dadog/admin/api/fetchShelters")
    @ResponseBody
    public ResponseEntity<Map<String, String>> fetchShelters() {
        shelterService.fetchAndSaveShelterData();

        // 리다이렉트할 URL을 응답으로 보내기
        Map<String, String> response = new HashMap<>();
        response.put("message", "공고 상태가 업데이트되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 입양 api
    @GetMapping("/dadog/admin/api/fetchAdopts")
    @ResponseBody
    public ResponseEntity<Map<String, String>> fetchAdopts() {
        adoptApiService.fetchAndUpdateAdoptData();

        // 리다이렉트할 URL을 응답으로 보내기
        Map<String, String> response = new HashMap<>();
        response.put("message", "공고 상태가 업데이트되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 공고 상태 업데이트
    @GetMapping("/dadog/admin/api/fetchCurrent")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateAdoptCurrentStatus() {
        // 공고 상태 업데이트 로직 호출
        applicationService.updateAdoptCurrentStatus();

        // 업데이트 성공 메시지를 JSON 형태로 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "공고 상태가 업데이트되었습니다.");
        return ResponseEntity.ok(response);
    }
}