package com.keduit.dadog.service;

import com.keduit.dadog.dto.BoardDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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
        return boardRepository.save(board);
    }

    public void deleteBoard(Long boardNo){

        if (!boardRepository.existsById(boardNo)){
            throw new IllegalStateException("게시물이 존재하지 않습니다.");
        }
        boardRepository.deleteById(boardNo);
    }


    public List<Board> findTop6ByOrderByCreateTimeDesc() {
        return boardRepository.findTop6ByOrderByCreateTimeDesc();
    }

    public Board findByBoardNo(Long boardNo) {
        return boardRepository.findById(boardNo).orElseThrow(() -> new EntityNotFoundException("Board not found with boardNo : " + boardNo));
    }
}
