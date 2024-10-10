package com.keduit.dadog.controller;

import com.keduit.dadog.dto.BoardDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/dadog/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String getAllBoards(Model model) {
        List<Board> boards = boardService.findAllBoards();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/new")
    public String addBoard(Model model) {
        model.addAttribute("boardDTO", new BoardDTO());
        return "board/add";
    }
    @PostMapping("/new")
    public String addBoard(@Valid @ModelAttribute("boardDTO") BoardDTO boardDTO,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(" *******  BR has ERRORS : " + bindingResult.getAllErrors());
            model.addAttribute("errorMsg", "입력값에 오류가 있습니다.");
            return "board/add";
        }

        try {
            boardService.addBoard(boardDTO);
        } catch (Exception e) {
            System.out.println(" ******* Exception Occurred : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "board/add";
        }
        return "redirect:/dadog/boards";
    }

    @GetMapping("/{boardNo}")
    public String getBoardByNo(@PathVariable Long boardNo, Model model){
        Board board = boardService.findBoardById(boardNo).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        model.addAttribute("board", board);
        return "board/get";
    }

    @GetMapping("/update/{boardNo}")
    public String updateBoard(@PathVariable Long boardNo, Model model){
        Board board = boardService.findBoardById(boardNo).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다.") );
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNo(board.getBoardNo());
        boardDTO.setBoardWriter(board.getBoardWriter());
        boardDTO.setBoardTitle(board.getBoardTitle());
        boardDTO.setBoardContent(board.getBoardContent());
        boardDTO.setBoardViews(board.getBoardViews());
        return "board/update";
    }

    @PostMapping("/update/{boardNo}")
    public String updateBoard(@PathVariable Long boardNo,
                              @Valid @ModelAttribute("boardDTO") BoardDTO boardDTO,
                              BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "board/update";
        }

        try {
            boardService.updateBoard(boardNo, boardDTO);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "board/update";
        }
        return "redirect:/dadog/boards";
    }
    @PostMapping("/delete/{boardNo}")
    public String deleteBoard(@PathVariable Long boardNo){
        boardService.deleteBoard(boardNo);
        return "redirect:/dadog/boards";
    }
}
