package com.keduit.dadog.controller;

import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.UserRepository;
import com.keduit.dadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dadog")
public class MyPageController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MyPageController(UserService userService, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(HttpServletRequest request, Model model) {
        return "myPage/myPage";
    }

    @GetMapping("/myPage/myWriting")
    public String myWriting(Model model) {
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts);
        return "myPage/myWriting";
    }

    // 회원 정보 가져오기
    @GetMapping("/myPage/myMemberForm")
    public String myMemberForm(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserDTO userDTO = new UserDTO().createUserDTO(user);
        model.addAttribute("userDTO", userDTO);
        return "myPage/myMemberForm";
    }

    // 회원 정보 수정
    @PostMapping("/myPage/myMemberForm")
    public String updateMember(UserDTO userDTO, Model model) {
        userService.updateUser(userDTO);
        model.addAttribute("successMessage", "회원 정보가 성공적으로 변경되었습니다.");
        model.addAttribute("userDTO", userDTO);
        return "myPage/myMemberForm";
    }

    @GetMapping("/myPage/{no}/edit")
    public String edit(@PathVariable Long no, Model model) {
        User userEntity = userRepository.findById(no).orElse(null);
        model.addAttribute("user", userEntity);
        return "myPage/edit";
    }

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

    @GetMapping("/myPage/myLost")
    public String myLost(Model model) {
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts);
        return "myPage/myLost";
    }

    @GetMapping("/myPage/myAdopt")
    public String myAdopt(Model model) {
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts);
        return "myPage/myAdopt";
    }

    @GetMapping("/myPage/myProtect")
    public String myProtecting(Model model) {
        List<Object> posts = new ArrayList<>();
        model.addAttribute("posts", posts);
        return "myPage/myProtect";
    }


    // 비밀번호 변경 페이지
    @GetMapping("/myPage/myPwd")
    public String showChangePasswordPage(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        UserDTO userDTO = new UserDTO().createUserDTO(user);
        userDTO.setPassword(""); // 보안을 위해 비밀번호 필드를 비움
        model.addAttribute("userDTO", userDTO);
        return "myPage/myPwd";
    }

    // 비밀번호 변경 처리
    @PostMapping("/myPage/myPwd")
    public String updatePwd(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getUser(principal.getName());

        if (userDTO.getCurrentPassword() == null || userDTO.getCurrentPassword().isEmpty()) {
            result.rejectValue("currentPassword", "error.userDTO", "현재 비밀번호를 입력해주세요.");
            return "myPage/myPwd";
        }

        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), currentUser.getUserPwd())) {
            result.rejectValue("currentPassword", "error.userDTO", "현재 비밀번호가 일치하지 않습니다.");
            return "myPage/myPwd";
        }

        if (userDTO.getNewPassword() == null || userDTO.getNewPassword().isEmpty()) {
            result.rejectValue("newPassword", "error.userDTO", "새 비밀번호를 입력해주세요.");
            return "myPage/myPwd";
        }

        if (userDTO.getNewPassword().equals(userDTO.getCurrentPassword())) {
            result.rejectValue("newPassword", "error.userDTO", "새 비밀번호는 현재 비밀번호와 다르게 설정해주세요.");
            return "myPage/myPwd";
        }

        if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userDTO", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "myPage/myPwd";
        }

        if (userDTO.getNewPassword().length() < 8) {
            result.rejectValue("newPassword", "error.userDTO", "비밀번호는 최소 8자 이상이어야 합니다.");
            return "myPage/myPwd";
        }

        try {
            userService.changePassword(principal.getName(), userDTO.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/dadog/myPage/myPwd";
        } catch (Exception e) {
            result.rejectValue("newPassword", "error.userDTO", "비밀번호 변경 중 오류가 발생했습니다.");
            return "myPage/myPwd";
        }
    }
}