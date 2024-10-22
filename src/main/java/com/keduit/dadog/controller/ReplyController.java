package com.keduit.dadog.controller;

import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.UserRepository;
import com.keduit.dadog.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dadog/boards/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 댓글 서비스 주입
    private final UserRepository userRepository; // 사용자 리포지토리 주입

    // 댓글 목록 조회
    @GetMapping("/{boardNo}")
    public String getReply(@PathVariable Long boardNo, Model model, Principal principal) {
        List<ReplyDTO> replies = replyService.getReplyByBoardId(boardNo);
        model.addAttribute("replies", replies);

        // 로그인된 사용자의 ID를 설정
        if (principal != null) {
            User user = principal.getName().contains("@") ?
                    userRepository.findByUserEmail(principal.getName()) :
                    userRepository.findByUserId(principal.getName());
            model.addAttribute("loggedInUserId",user.getUserId());
        } else {
            model.addAttribute("loggedInUserId", null); // 비로그인 사용자는 null로 설정
        }

        model.addAttribute("userId", principal != null ? principal.getName() : null);

        return "redirect:/dadog/boards/" + boardNo; // 게시판으로 리디렉션
    }

    // 댓글 추가
    @PostMapping("/{boardNo}/add")
    @ResponseBody
    public ResponseEntity<?> addReply(@PathVariable Long boardNo, @RequestBody ReplyDTO replyDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // principal에서 사용자 ID 가져오기
        User user;
        String userId = principal.getName();

        if (userId.contains("@")) { // 카카오 로그인 사용자
            user = userRepository.findByUserEmail(userId);
        } else { // 일반 웹 사용자
            user = userRepository.findByUserId(userId);
        }

        if (user != null) {
            replyDTO.setReplyWriter(user.getUserId());
            replyDTO.setUserNo(user.getUserNo());
            replyDTO.setBoardNo(boardNo);

            try {
                ReplyDTO savedReply = replyService.addReply(replyDTO);
                return ResponseEntity.ok(savedReply);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 추가 중 오류가 발생했습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
        }
    }


    // 댓글 수정
    @PostMapping("/{boardNo}/update/{replyNo}")
    @ResponseBody
    public ResponseEntity<Void> updateReply(@PathVariable Long boardNo,
                                            @PathVariable Long replyNo,
                                            @RequestBody ReplyDTO replyDTO,
                                            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 현재 로그인한 사용자 정보 조회
        User user = principal.getName().contains("@") ?
                userRepository.findByUserEmail(principal.getName()) :
                userRepository.findByUserId(principal.getName());

        Long userNo = user.getUserNo();

        try {
            replyService.updateReply(replyNo, replyDTO, userNo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 댓글 삭제
    @PostMapping("/{boardNo}/delete/{replyNo}")
    @ResponseBody
    public ResponseEntity<Void> deleteReply(@PathVariable Long boardNo, @PathVariable Long replyNo, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 현재 로그인한 사용자 정보 조회
        User user = principal.getName().contains("@") ?
                userRepository.findByUserEmail(principal.getName()) :
                userRepository.findByUserId(principal.getName());

        Long userNo = user.getUserNo();

        try {
            replyService.deleteReply(replyNo, userNo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
