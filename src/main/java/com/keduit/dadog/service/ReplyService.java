package com.keduit.dadog.service;

import com.keduit.dadog.constant.Role;
import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.entity.Reply;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.BoardRepository;
import com.keduit.dadog.repository.ReplyRepository;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 필수 필드를 위한 생성자 자동 생성
public class ReplyService {

    private final ReplyRepository replyRepository; // 댓글 관리를 위한 레포지토리
    private final BoardRepository boardRepository; // 게시물 관리를 위한 레포지토리
    private final UserRepository userRepository; // 사용자 관리를 위한 레포지토리

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<ReplyDTO> getReplyByBoardId(Long boardNo) {
        List<Reply> replies = replyRepository.findByBoard_BoardNo(boardNo);
        System.out.println("조회된 댓글 수: " + replies.size()); // 댓글 수 확인

        return replies.stream().map(this::entityToDto).collect(Collectors.toList()); // DTO로 변환
    }

    // 새 댓글 추가
    @Transactional
    public ReplyDTO addReply(ReplyDTO replyDTO) {
        // 게시물 조회
        Board board = boardRepository.findById(replyDTO.getBoardNo())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        User user = getUserFromPrincipal();

        // 로그인 방식에 따라 replyWriter 결정 (카카오 로그인: 이메일, 일반 회원: userId)
        String replyWriter;
        if (isKakaoUser(user)) {  // 카카오 사용자인지 여부를 확인하는 메서드
            replyWriter = user.getUserEmail();  // 카카오 사용자: 이메일
        } else {
            replyWriter = user.getUserId();  // 일반 사용자: userId
        }
        System.out.println("Reply Writer: " + replyWriter);  // 최종 결정된 replyWriter 값 출력

        // 댓글 작성자 설정
        replyDTO.setReplyWriter(replyWriter);
        replyDTO.setUserNo(user.getUserNo());

        // Reply 엔티티 생성 및 저장
        Reply reply = Reply.builder()
                .replyContent(replyDTO.getReplyContent())
                .replyWriter(replyWriter)
                .board(board)
                .user(user)
                .createTime(LocalDateTime.now())
                .build();


        return entityToDto(replyRepository.save(reply));
//        // 로그인된 사용자 정보 조회
//        String loggedInUserId = getLoggedInUser();
//
//        User user;
//
//        // 로그인 방식에 따라 적절한 사용자 조회
//        if (loggedInUserId.contains("@")) { // 카카오 사용자 확인 (이메일이 @ 포함)
//            user = userRepository.findByUserEmail(loggedInUserId);
//        } else { // 일반 홈페이지 사용자
//            user = userRepository.findByUserId(loggedInUserId);
//        }
//
//        if (user == null) {
//            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
//        }
//
//        // 댓글 작성자 설정
//        replyDTO.setReplyWriter(user.getUserId());
//        replyDTO.setUserNo(user.getUserNo());
//
//        // Reply 엔티티 생성 및 저장
//        Reply reply = Reply.builder()
//                .replyContent(replyDTO.getReplyContent())
//                .replyWriter(replyDTO.getReplyWriter())
//                .board(board)
//                .user(user)
//                .createTime(LocalDateTime.now())
//                .build();
//
//        return entityToDto(replyRepository.save(reply));
    }
    // 카카오 사용자 여부를 확인하는 메서드
    private boolean isKakaoUser(User user) {
        // 카카오 로그인 사용자는 이메일에 "kakao"라는 단어가 포함될 가능성이 높음
        boolean isKakao = user.getUserEmail() != null && user.getUserEmail().contains("kakao");
        System.out.println("Is Kakao User: " + isKakao + " for email: " + user.getUserEmail());
        return isKakao;
    }
    @Transactional
    // 댓글 수정
    public void updateReply(Long replyNo, ReplyDTO replyDTO, Long userNo) {
        Reply reply = replyRepository.findById(replyNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 현재 사용자가 댓글 작성자인지 확인
        if (!reply.getUser().getUserNo().equals(userNo)) {
            throw new SecurityException("댓글을 수정할 권한이 없습니다.");
        }

        reply.setReplyContent(replyDTO.getReplyContent());
        reply.setUpdateTime(LocalDateTime.now());
        replyRepository.save(reply);
    }

    @Transactional
    // 댓글 삭제
    public void deleteReply(Long replyNo, Long userNo) {
        Reply reply = replyRepository.findById(replyNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 현재 사용자가 댓글 작성자인지 확인
        if (!reply.getUser().getUserNo().equals(userNo)) {
            throw new SecurityException("댓글을 삭제할 권한이 없습니다.");
        }

        replyRepository.delete(reply);
    }

    // 로그인된 사용자 정보 조회
    private String getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            // 일반 회원가입한 사용자의 경우
            return ((UserDetails) principal).getUsername();
        } else {
            // OAuth2 로그인의 경우 (예: Kakao)
            return principal.toString(); // 이메일을 반환할 가능성이 높음
        }
    }

    // Reply 엔티티를 ReplyDTO로 변환
    private ReplyDTO entityToDto(Reply reply) {
        return ReplyDTO.builder()
                .replyNo(reply.getReplyNo()) // 댓글 번호 설정
                .boardNo(reply.getBoard().getBoardNo()) // 관련 게시물 번호 설정
                .replyWriter(reply.getReplyWriter()) // 댓글 작성자 (사용자 ID) 설정
                .replyContent(reply.getReplyContent()) // 댓글 내용 설정
                .updateTime(reply.getUpdateTime()) // 수정 시간 설정
                .createTime(reply.getCreateTime()) // 생성 시간 설정
                .userNo(reply.getUser().getUserNo()) // 사용자 번호 설정
                .build(); // ReplyDTO 생성 및 반환
    }

    private User getUserFromPrincipal() {
        String loggedInUserId = getLoggedInUser();
        System.out.println("Logged in User ID: " + loggedInUserId);  // 로그인된 사용자 ID 출력

        User user = loggedInUserId.contains("@") ?
                userRepository.findByUserEmail(loggedInUserId) :
                userRepository.findByUserId(loggedInUserId);

        return user;
    }
}