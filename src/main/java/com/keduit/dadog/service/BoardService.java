package com.keduit.dadog.service;

import com.keduit.dadog.dto.BoardDTO;
import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.dto.UpdateBoardDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyService replyService;

    // 게시물 추가
    public Board addBoard(BoardDTO boardDTO, String username) {
        // 새로운 Board 엔티티 생성 후 DTO의 데이터를 Board에 매핑
        Board board = new Board();
        board.setBoardWriter(username); // 작성자 설정
        board.setBoardTitle(boardDTO.getBoardTitle()); // 제목 설정
        board.setBoardContent(boardDTO.getBoardContent()); // 내용 설정
        board.setBoardViews(0L); // 기본 조회수를 0으로 설정
        board.setCreateTime(LocalDateTime.now()); // 생성 시간 설정
        return boardRepository.save(board); // Board 엔티티를 데이터베이스에 저장
    }

    // 모든 게시물 목록 조회
    public List<Board> findAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards; // 모든 게시물 조회
    }

    // 게시물 ID로 게시물 조회
    public Optional<Board> findBoardById(Long boardNo) {
        return boardRepository.findById(boardNo); // ID로 게시물 조회
    }


    public void updateBoard(Long boardNo, UpdateBoardDTO boardDTO) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        board.setBoardTitle(boardDTO.getBoardTitle());
        board.setBoardContent(boardDTO.getBoardContent());

        // 추가적인 속성이 있다면 여기에 포함
        boardRepository.save(board); // 변경된 게시물 저장
    }

    // 게시물 삭제
    public void deleteBoard(Long boardNo) {
        // 삭제하려는 게시물이 존재하는지 확인
        if (!boardRepository.existsById(boardNo)) {
            throw new IllegalStateException("게시물이 존재하지 않습니다.");
        }
        boardRepository.deleteById(boardNo); // 게시물 삭제
    }

    // 페이징 처리된 게시물 목록 조회
    public Page<BoardDTO> paging(Pageable pageable) {
        // 한 페이지당 표시할 게시물 수 설정
        int pageLimit = pageable.getPageSize();

        // 게시물 번호 기준 내림차순으로 정렬하여 페이지 생성
        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageLimit, Sort.by(Sort.Direction.DESC, "boardNo"));

        // 페이징된 게시물 조회
        Page<Board> boardPages = boardRepository.findAll(paging);

        // Board 엔티티를 BoardDTO로 변환 후 반환
        return boardPages.map(this::changeDTO);
    }

    // 제목으로 게시물 검색
    public Page<BoardDTO> searchByTitle(String keyword, Pageable pageable) {
        // 페이지 크기 설정
        int pageLimit = pageable.getPageSize();

        // 제목에 keyword가 포함된 게시물 목록을 페이징하여 조회
        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageLimit, Sort.by(Sort.Direction.DESC, "boardNo"));
        Page<Board> boardPages = boardRepository.findByBoardTitleContaining(keyword, paging);

        // Board 엔티티를 BoardDTO로 변환 후 반환
        return boardPages.map(this::changeDTO);
    }

    // 게시물 내용을 기반으로 검색
    public Page<BoardDTO> searchByContent(String keyword, Pageable pageable) {
        // 내용에 keyword가 포함된 게시물 목록을 페이징하여 조회
        Page<Board> boardPage = boardRepository.findByBoardContentContaining(keyword, pageable);
        return boardPage.map(this::changeDTO);
    }

    // 작성자로 게시물 검색
    public Page<BoardDTO> searchByWriter(String keyword, Pageable pageable) {
        // 작성자에 keyword가 포함된 게시물 목록을 페이징하여 조회
        Page<Board> boardPage = boardRepository.findByBoardWriterContaining(keyword, pageable);
        return boardPage.map(this::changeDTO);
    }

    // Board 엔티티를 BoardDTO로 변환하는 메서드
    private BoardDTO changeDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNo(board.getBoardNo()); // 게시물 번호 설정
        boardDTO.setBoardWriter(board.getBoardWriter()); // 작성자 설정
        boardDTO.setBoardTitle(board.getBoardTitle()); // 제목 설정
        boardDTO.setBoardContent(board.getBoardContent()); // 내용 설정
        boardDTO.setBoardViews(board.getBoardViews()); // 조회수 설정

        boardDTO.setUpdateTime(board.getUpdateTime()); // 수정 시간 설정
        return boardDTO; // DTO 반환
    }

    // static 메서드로 Board 엔티티를 BoardDTO로 변환
    public static BoardDTO changeEntity(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNo(board.getBoardNo()); // 게시물 번호 설정
        boardDTO.setBoardWriter(board.getBoardWriter()); // 작성자 설정
        boardDTO.setBoardTitle(board.getBoardTitle()); // 제목 설정
        boardDTO.setBoardContent(board.getBoardContent()); // 내용 설정
        boardDTO.setBoardViews(board.getBoardViews()); // 조회수 설정

        boardDTO.setCreateTime(board.getCreateTime()); // 생성 시간 설정
        boardDTO.setUpdateTime(board.getUpdateTime()); // 수정 시간 설정
        return boardDTO; // DTO 반환
    }
    public Board viewBoard(Long boardNo) {
        Board board = boardRepository.findById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        // board.getBoardViews()가 null인 경우, null + 1은 NullPointerException을 발생
        Long views = board.getBoardViews() != null ? board.getBoardViews() : 0L;

        board.setBoardViews(views + 1);

//        Long nowViews = board.getBoardViews();
//
//        if (nowViews == null){
//            nowViews = 0L;
//        }
//        board.setBoardViews(nowViews + 1);
        return boardRepository.save(board);
    }
    public BoardDTO getBoardWithReply(Long boardNo){
        Board board = boardRepository.findById(boardNo).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        BoardDTO boardDTO = changeDTO(board);
        List<ReplyDTO> replies =replyService.getReplyByBoardId(boardNo);

        boardDTO.setReplies(replies);
        return boardDTO;
    }
}
