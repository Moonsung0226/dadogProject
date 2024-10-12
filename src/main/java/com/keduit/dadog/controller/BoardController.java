package com.keduit.dadog.controller;

import com.keduit.dadog.dto.BoardDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dadog/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String getAllBoards(Model model, Principal principal,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "searchType", required = false) String searchType,
                               Pageable pageable) {

        Page<BoardDTO> boardPage;

        if (keyword != null && !keyword.isEmpty()) {
            switch (searchType) {
                case "title":
                    boardPage = boardService.searchByTitle(keyword, pageable);
                    break;
                case "content":
                    boardPage = boardService.searchByContent(keyword, pageable);
                    break;
                case "writer":
                    boardPage = boardService.searchByWriter(keyword, pageable);
                    break;
                default:
                    boardPage = boardService.paging(pageable);
                    break;
            }
        } else {
            boardPage = boardService.paging(pageable);
        }

        model.addAttribute("userId", (principal != null) ? principal.getName() : "Anonymous");
        model.addAttribute("boards", boardPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);

        return "board/list";
    }


    @GetMapping("/new")
    public String addBoard(Model model, Principal principal) {
        BoardDTO boardDTO = new BoardDTO();
        System.out.println("------------------------" + principal.getName());
        boardDTO.setBoardWriter(principal.getName()); // 작성자 설정
        model.addAttribute("boardDTO", boardDTO); // 수정: 생성한 boardDTO를 모델에 추가
        model.addAttribute("userId", principal.getName());
        return "board/add";
    }

    @PostMapping("/new")
    public String addBoard(@Valid @ModelAttribute("boardDTO") BoardDTO boardDTO,
                           BindingResult bindingResult, Model model, Principal principal) {

        String username = principal.getName();

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", "입력값에 오류가 있습니다.");
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "board/add";
        }

        try {

            boardService.addBoard(boardDTO, username);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "board/add";
        }
        return "redirect:/dadog/boards";
    }

    @GetMapping("/{boardNo}")
    public String getBoardByNo(@PathVariable Long boardNo, Model model,Principal principal){
        Board board = boardService.findBoardById(boardNo).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        model.addAttribute("board", board);
        model.addAttribute("userId", principal.getName());
        return "board/get";
    }

    @GetMapping("/update/{boardNo}")
    public String updateBoard(@PathVariable Long boardNo, Model model, Principal principal){
        Board board = boardService.findBoardById(boardNo).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다.") );

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardNo(board.getBoardNo());
        boardDTO.setBoardWriter(board.getBoardWriter());
        boardDTO.setBoardTitle(board.getBoardTitle());
        boardDTO.setBoardContent(board.getBoardContent());
        boardDTO.setBoardViews(board.getBoardViews());

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("userId", (principal != null) ? principal.getName() : "Anonymous");
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
