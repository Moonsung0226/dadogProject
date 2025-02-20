package com.keduit.dadog.controller;


import com.keduit.dadog.constant.AdoptWait;
import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.*;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.Protect;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.service.*;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/dadog")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService; // UserService 필드
    private final LostService lostService;
    private final AdoptService adoptService;
    private final ProtectService protectService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationService applicationService;
    private final BoardService boardService;


    // 마이페이지
    @GetMapping("/myPage") // 경로 앞에 '/' 추가
    public String myPage(HttpServletRequest request, Model model, Principal principal) {
        HttpSession session = request.getSession();
        //user 를 가져옴
        User user = userService.getUser(principal.getName());
        Long userNo = user.getUserNo();
        Map<LocalDate,List<Object>> posts = userService.getUserPosts(user);
        model.addAttribute("userNo", userNo);
        model.addAttribute("posts", posts);
        return "myPage/myPage";
    }


    @GetMapping("/myPage/myWriting")
    public String myWriting(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        // 게시글 리스트를 빈 리스트로 초기화
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts); // 빈 리스트 추가

        return "myBoard"; // Thymeleaf 템플릿 경로
    }

    // 회원 정보 가져오기(카카오일 경우 이메일은 막기)
    @GetMapping("/myPage/myMemberForm")
    public String myMemberForm(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserDTO userDTO = new UserDTO().createUserDTO(user);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("isKakaoUser", user.getRole() == Role.KAKAO);
        return "myPage/myMemberForm";
    }

    // POST 요청 처리: 회원 정보 수정
    @PostMapping("/myPage/myMemberForm")
    public String updateMember(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, Principal principal) {
        User currentUser = userService.getUser(principal.getName());
        boolean isKakaoUser = currentUser.getRole() == Role.KAKAO;

        // 로그 추가: userDTO에서 받아온 데이터 확인
        System.out.println("Received userDTO: " + userDTO);

        // 로그 추가: 업데이트 전 currentUser 상태
        System.out.println("Before update - currentUser: " + currentUser);

        // 이름 업데이트 (모든 사용자)
        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            currentUser.setUserName(userDTO.getName());
        }

        // 주소와 전화번호 업데이트 (모든 사용자)
        currentUser.setUserAddr(userDTO.getAddress());
        currentUser.setUserTel(userDTO.getTel());

        if (!isKakaoUser) {
            // KAKAO 사용자가 아닌 경우에만 이메일 업데이트
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                currentUser.setUserEmail(userDTO.getEmail());
            }
        } else {
            // KAKAO 사용자의 경우 이메일 변경 방지
            userDTO.setEmail(currentUser.getUserEmail());
        }

        // 로그 추가: 업데이트 후 currentUser 상태
        System.out.println("After update - currentUser: " + currentUser);

        userRepository.save(currentUser);

        // 로그 추가: 저장 후 currentUser 상태
        System.out.println("After save - currentUser: " + currentUser);

        model.addAttribute("successMessage", "회원 정보가 성공적으로 변경되었습니다.");
        model.addAttribute("userDTO", new UserDTO().createUserDTO(currentUser));
        model.addAttribute("isKakaoUser", isKakaoUser);
        return "myPage/myMemberForm";
    }

    //회원정보 수정 페이지
    @GetMapping("/myPage/{no}/edit")
    public String edit(@PathVariable Long no, Model model) {
        User userEntity = userRepository.findById(no).orElse(null);
        model.addAttribute("user", userEntity);
        return "myPage/edit";
    }

    //회원정보 업데이트
    @PostMapping("/myPage/update")
    public String update(UserDTO userDTO) {
        User userEntity = userDTO.toEntity();
        Long userId = Long.valueOf(userEntity.getUserId());
        User target = userRepository.findById(userId).orElse(null);

        if (target != null) {
            userRepository.save(userEntity);
        }
        return "redirect:/dadog/myPage/myMemberForm";
    }

    // 입양현황(1)
    @GetMapping("/myPage/myAdopt")
    public String myAdopt(HttpServletRequest request, Model model,
                          @RequestParam(value = "userNo", required = false) Long userNo,
                          Principal principal,
                          @RequestParam(value = "status", required = false) String status,
                          @RequestParam(value = "page", defaultValue = "0") int page) {

        // userNo가 null인 경우, principal에서 userId를 가져와 userNo로 변환
        if (userNo == null) {
            String userId = principal.getName(); // 로그인한 사용자의 ID
            userNo = userService.getUserNoByUserId(userId); // userId로 userNo 조회
        }

        Pageable pageable = PageRequest.of(page, 18);
        Page<ApplicationDTO> applicationList;

        if (status != null) {
            AdoptWait adoptWaitStatus = AdoptWait.valueOf(status.toUpperCase()); // status를 AdoptWait enum으로 변환
            applicationList = applicationService.getApplicationListByStatus(adoptWaitStatus, pageable);
        } else {
            applicationList = applicationService.getApplicationUserId(userNo, pageable);
        }

        model.addAttribute("maxPage", 10);
        model.addAttribute("applicationList", applicationList);

        return "myPage/myAdopt"; // Thymeleaf 템플릿 경로
    }



    // 입양현황(2) 상세 조회
    @GetMapping("/myPage/myAdopt/{appNo}")
    @ResponseBody
    public ResponseEntity<?> getApplicationDetail(@PathVariable Long appNo) {
        // appNo를 사용하여 해당 입양 신청 정보를 찾음.
        AdoptDTO adoptDTO = adoptService.getApplicationByAppNo(appNo);

        if (adoptDTO != null) {
            return ResponseEntity.ok(adoptDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("입양 신청 정보를 찾을 수 없습니다.");
        }
    }

    //유저의 실종 신고글 조회
    @PostMapping({"/myPage/myLost","/myPage/myLost/{page}"})
    public String myLost(Model model, @RequestBody Map<String, Object> userNo, @PathVariable("page") Optional<Integer> page) {
        System.out.println("---------------- userDTO" + userNo);
        String userNoStr = (String) userNo.get("userNo");
        Long userNum = Long.valueOf(userNoStr);
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 10);
        model.addAttribute("maxPage", 5);
        Page<Lost> lostPage = lostService.getMyLostPage(userNum, pageable);
        model.addAttribute("lostPage", lostPage);
        return "myPage/myLost"; // Thymeleaf 템플릿 경로
    }

    //유저의 보호중 글 조회
    @PostMapping({"/myPage/myProtect", "/myPage/myProtect/{page}"})
    public String myProtecting(Model model, @RequestBody Map<String, Object> userNo, @PathVariable("page") Optional<Integer> page) {
        System.out.println("---------------- userDTO" + userNo);
        String userNoStr = (String) userNo.get("userNo");
        Long userNum = Long.valueOf(userNoStr);
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 10);
        model.addAttribute("maxPage", 5);
        Page<Protect> protectPage = protectService.getMyProtectPage(userNum, pageable);
        model.addAttribute("protectPage", protectPage);
        return "myPage/myProtect"; // Thymeleaf 템플릿 경로
    }

    @PostMapping("/myPage/myBoard")
    public String myBoard(Model model, @RequestBody Map<String, Object> userNo) {
        System.out.println("-------------- userDTO" + userNo);
        String userNoStr = (String) userNo.get("userNo");
        Long userNum = Long.valueOf(userNoStr);
        List<BoardDTO> boardDTOList = boardService.getUserBoard(userNum);
        model.addAttribute("boardDTOList", boardDTOList);
        return "myPage/myBoard";
    }

    // 비밀번호 변경 페이지
    @GetMapping("/myPage/myPwd")
    public String showChangePasswordPage(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserDTO userDTO = new UserDTO().createUserDTO(user);
        userDTO.setPassword(""); // 보안을 위해 비밀번호 필드를 비움
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userRole", user.getRole().toString());
        return "myPage/myPwd";
    }

    // 비밀번호 변경창의 카카오 유저와 일반 유저의 차별화.  카카오는 비밀번호 변경 불가.
    @PostMapping("/myPage/myPwd")
    public String updatePwd(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getUser(principal.getName());

        if (currentUser.getRole() == Role.KAKAO) {
            redirectAttributes.addFlashAttribute("errorMessage", "카카오 로그인 사용자는 비밀번호를 변경할 수 없습니다.");
            return "redirect:/dadog/myPage/myPwd";
        }
        if (userDTO.getCurrentPassword() == null || userDTO.getCurrentPassword().isEmpty()) {
            return redirectWithError(redirectAttributes, "기존 비밀번호를 입력해주세요.");
        }
        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), currentUser.getUserPwd())) {
            return redirectWithError(redirectAttributes, "기존 비밀번호가 일치하지 않습니다.");
        }
        if (userDTO.getNewPassword() == null || userDTO.getNewPassword().isEmpty()) {
            return redirectWithError(redirectAttributes, "새 비밀번호를 입력해주세요.");
        }
        if (userDTO.getNewPassword().equals(userDTO.getCurrentPassword())) {
            return redirectWithError(redirectAttributes, "새 비밀번호는 기존 비밀번호와 다르게 설정해주세요.");
        }
        if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
            return redirectWithError(redirectAttributes, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if (userDTO.getNewPassword().length() < 8) {
            return redirectWithError(redirectAttributes, "비밀번호는 최소 8자 이상이어야 합니다.");
        }
        try {
            userService.changePassword(principal.getName(), userDTO.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 변경에 실패하였습니다.");
        }
        return "redirect:/dadog/myPage/myPwd";
    }

    // 에러메세지와 함께 리다이렉트
    private String redirectWithError(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", message);
        return "redirect:/dadog/myPage/myPwd";
    }

    // 회원 탈퇴
    @GetMapping("/myPage/delete")
    public String deleteUser(@RequestParam Long userNo, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        userService.deleteUserByNo(userNo);

        // 로그아웃 처리
        SecurityContextHolder.clearContext(); // 보안 컨텍스트를 클리어하여 인증 정보를 제거
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        redirectAttributes.addFlashAttribute("message", "회원 탈퇴하였습니다.");
        return "redirect:/dadog/main";
    }
}
