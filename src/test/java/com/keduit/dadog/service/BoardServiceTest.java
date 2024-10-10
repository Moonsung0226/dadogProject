package com.keduit.dadog.service;

import com.keduit.dadog.dto.BoardDTO;
import com.keduit.dadog.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    @Rollback(false) // 이 테스트는 롤백되지 않음
    public void testAddBoards() {
        // 여러 개의 게시물 추가
        for (int i = 1; i <= 5; i++) {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setBoardWriter("Writer" + i);
            boardDTO.setBoardTitle("Title" + i);
            boardDTO.setBoardContent("Content" + i);
//            boardService.addBoard(boardDTO);
        }

        // 추가된 게시물 수 확인
        List<Board> boards = boardService.findAllBoards();
        assertEquals(5, boards.size());

        // 게시물 출력
        boards.forEach(System.out::println);
    }
}