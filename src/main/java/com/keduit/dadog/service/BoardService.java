package com.keduit.dadog.service;

import com.keduit.dadog.dto.BoardDTO;
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

    public Board addBoard(BoardDTO boardDTO, String username){
        Board board = new Board();
        board.setBoardWriter(username);
        board.setBoardTitle(boardDTO.getBoardTitle());
        board.setBoardContent(boardDTO.getBoardContent());
        board.setBoardViews(0L); // 기본조회수 0으로 설정
        board.setCreateTime(LocalDateTime.now());
        return boardRepository.save(board);

    }

    public List<Board> findAllBoards(){
        return boardRepository.findAll();
    }

    public Optional<Board> findBoardById(Long boardNo){
        return boardRepository.findById(boardNo);
    }

    public Board updateBoard(Long boardNo, BoardDTO boardDTO){
        Board board = boardRepository.findById(boardNo).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

//        board.setBoardWriter(boardDTO.getBoardWriter()); // 작성자는 수정하지 않도록
        board.setBoardTitle(boardDTO.getBoardTitle());
        board.setBoardContent(boardDTO.getBoardContent());
        board.setBoardViews(boardDTO.getBoardViews());
        board.setUpdateTime(LocalDateTime.now());
        return boardRepository.save(board);
    }

    public void deleteBoard(Long boardNo){

        if (!boardRepository.existsById(boardNo)){
            throw new IllegalStateException("게시물이 존재하지 않습니다.");
        }
        boardRepository.deleteById(boardNo);
    }

    public Page<BoardDTO> paging(Pageable pageable){

        // 페이지 번호는 0부터 시작하기 때문에 1 페이지 요청 시 0으로 변환
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber()- 1);

        // 한 페이지에 표시할 게시물 수
        int pageLimit = pageable.getPageSize();


        // 한 페이지당 3개식 글을 보여주고 정렬 기준은 게시물번호 기준으로 내림차순
        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageLimit, Sort.by(Sort.Direction.DESC, "boardNo"));

        // 페이징된 Board entity 목록 가져옴
        Page<Board> boardPages = boardRepository.findAll(paging);

        // entity -> dto로 변환
        return boardPages.map(this::changeDTO);
    }
    public Page<BoardDTO> searchByTitle(String keyword, Pageable pageable) {
        // 페이지 번호는 0부터 시작하기 때문에 1 페이지 요청 시 0으로 변환
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);

        // 페이지 크기는 Pageable 객체에서 가져와서 동적으로 설정
        int pageLimit = pageable.getPageSize();

        // Pageable 객체 생성 (boardNo 기준 내림차순 정렬)
        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageLimit, Sort.by(Sort.Direction.DESC, "boardNo"));

        // 제목에 keyword가 포함된 게시물 목록을 페이징하여 조회
        Page<Board> boardPages = boardRepository.findByBoardTitleContaining(keyword, paging);

        // 엔티티를 DTO로 변환하여 반환
        return boardPages.map(this::changeDTO);
    }


    private BoardDTO changeDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNo(board.getBoardNo());
        boardDTO.setBoardWriter(board.getBoardWriter());
        boardDTO.setBoardTitle(board.getBoardTitle());
        boardDTO.setBoardContent(board.getBoardContent());
        boardDTO.setBoardViews(board.getBoardViews());
        boardDTO.setUpdateTime(board.getUpdateTime());
        return boardDTO;
    }

    // 내용으로 검색

    public Page<BoardDTO> searchByContent(String keyword, Pageable pageable) {
        Page<Board> boardPage = boardRepository.findByBoardContentContaining(keyword, pageable);
        return boardPage.map(this::changeDTO);
    }

    // 작성자로 검색

    public Page<BoardDTO> searchByWriter(String keyword, Pageable pageable) {
        Page<Board> boardPage = boardRepository.findByBoardWriterContaining(keyword, pageable);
        return boardPage.map(this::changeDTO);
    }

    public static BoardDTO changeEntity(Board board){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNo(board.getBoardNo());
        boardDTO.setBoardWriter(board.getBoardWriter());
        boardDTO.setBoardTitle(board.getBoardTitle());
        boardDTO.setBoardContent(board.getBoardContent());
        boardDTO.setBoardViews(board.getBoardViews());
        boardDTO.setCreateTime(board.getCreateTime());
        boardDTO.setUpdateTime(board.getUpdateTime());
        return boardDTO;
    }

}
