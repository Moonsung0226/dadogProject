package com.keduit.dadog.service;

import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.entity.Reply;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.BoardRepository;
import com.keduit.dadog.repository.ReplyRepository;
import com.keduit.dadog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

        System.out.println(" *** 보드 넘버  : " + replyDTO.getBoardNo());
        System.out.println(" *** 유저 넘버  :  " + replyDTO.getUserNo());

        // 게시물 조회
        Board board = boardRepository.findById(replyDTO.getBoardNo())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다.")); // 게시물이 존재하지 않을 경우 예외 발생

        User user = userRepository.findById(replyDTO.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다.")); // 사용자가 존재하지 않을 경우 예외 발생

//        replyDTO.setReplyWriter(user.getUserId());

        Reply reply = Reply.builder()
                .replyContent(replyDTO.getReplyContent())
                .replyWriter(replyDTO.getReplyWriter())
                .board(board)
                .user(user)
                .build();

        return entityToDto(replyRepository.save(reply)); // DTO로 변환하여 반환
    }
    // 댓글 삭제
    public void deleteReply(Long replyNo, String username) {
        Reply reply = replyRepository.findById(replyNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다.")); // 댓글이 존재하지 않을 경우 예외 발생
        replyRepository.delete(reply); // 댓글 삭제
    }

    // 댓글 수정
    public void updateReply(Long replyNo, ReplyDTO replyDTO, String username) {
        Reply reply = replyRepository.findById(replyNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 현재 사용자가 댓글 작성자인지 확인
        if (!reply.getReplyWriter().equals(username)) {
            throw new SecurityException("댓글을 수정할 권한이 없습니다.");
        }
        reply.setReplyContent(replyDTO.getReplyContent());
        reply.setReplyContent(replyDTO.getReplyContent());
        reply.setUpdateTime(LocalDateTime.now());
        replyRepository.save(reply);
    }


    private void validateReplyDTO(ReplyDTO replyDTO) {
        if (replyDTO.getBoardNo() == null || replyDTO.getUserNo() == null || replyDTO.getReplyContent() == null) {
            throw new IllegalArgumentException("필수 필드가 누락되었습니다.");
        }
    }
    // ReplyDTO를 Reply 엔티티로 변환
    private Reply dtoToEntity(ReplyDTO replyDTO, Board board, User user) {
        return Reply.builder()
                .replyContent(replyDTO.getReplyContent()) // DTO에서 내용 설정
                .board(board) // 관련 게시물 설정
                .user(user) // 관련 사용자 설정
                .build(); // Reply 엔티티 생성 및 반환
    }

    // Reply 엔티티를 ReplyDTO로 변환
    private ReplyDTO entityToDto(Reply reply) {
        return ReplyDTO.builder()
                .replyNo(reply.getReplyNo()) // 댓글 번호 설정
                .boardNo(reply.getBoard().getBoardNo()) // 관련 게시물 번호 설정
                .replyWriter(reply.getUser().getUserId()) // 댓글 작성자 (사용자 ID) 설정
                .replyContent(reply.getReplyContent()) // 댓글 내용 설정
                .updateTime(reply.getUpdateTime()) // 수정 시간 설정
                .createTime(reply.getCreateTime()) // 생성 시간 설정
                .userNo(reply.getUser().getUserNo()) // 사용자 번호 설정
                .build(); // ReplyDTO 생성 및 반환
    }
}