package com.keduit.dadog.controller;

import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.dto.UserDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.entity.Reply;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.BoardRepository;
import com.keduit.dadog.repository.ReplyRepository;
import com.keduit.dadog.repository.UserRepository;
import com.keduit.dadog.service.ReplyService;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List; // List 인터페이스 임포트
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dadog/boards/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 댓글 서비스 주입
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 목록 조회
    @GetMapping("/{boardNo}")
    public String getReply(@PathVariable Long boardNo, Model model, Principal principal) {
        List<ReplyDTO> replies = replyService.getReplyByBoardId(boardNo);
        model.addAttribute("replies", replies);
        model.addAttribute("userNo", principal.getName()); // 또는 적절한 사용자 ID 설정

        return "redirect:/dadog/boards/" + boardNo;
    }

    // 댓글 추가
    @PostMapping("/{boardNo}/add")
    @ResponseBody
    public ResponseEntity<ReplyDTO> addReply(@PathVariable Long boardNo, @RequestBody ReplyDTO replyDTO, Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 인증되지 않은 경우
        }


        replyDTO.setReplyWriter(principal.getName());

        replyDTO.setBoardNo(boardNo);


        ReplyDTO replyDTO1 = replyService.addReply(replyDTO);

        return ResponseEntity.ok(replyDTO1);
    }

    // 댓글 수정
    @PostMapping("/{boardNo}/update/{replyNo}")
    @ResponseBody
    public ResponseEntity<Void> updateReply(@PathVariable Long boardNo,
                                            @PathVariable Long replyNo,
                                            @RequestBody ReplyDTO replyDTO,
                                            Principal principal) {
        replyService.updateReply(replyNo, replyDTO, principal.getName());
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @PostMapping("/{boardNo}/delete/{replyNo}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long boardNo, @PathVariable Long replyNo, Principal principal) {
        replyService.deleteReply(replyNo, principal.getName());
        return ResponseEntity.ok().build();
    }
}
