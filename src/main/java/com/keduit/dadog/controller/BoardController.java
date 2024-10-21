package com.keduit.dadog.controller;

import com.keduit.dadog.dto.BoardDTO;
import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.dto.UpdateBoardDTO;
import com.keduit.dadog.entity.Board;
import com.keduit.dadog.entity.Reply;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.BoardRepository;
import com.keduit.dadog.repository.UserRepository;
import com.keduit.dadog.service.BoardService;
import com.keduit.dadog.service.ReplyService;
import com.keduit.dadog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

// @RestController : ResponseEntity<>
// @Controller = ex ) return "/board/get";


@Controller
@RequestMapping("/dadog/boards")    // 게시물 관련 요청을 처리하는 경로 매핑
@RequiredArgsConstructor    // 필수 의존성 주입을 위한 롬복 애노테이션
public class BoardController {

    private final BoardService boardService;

    private final ReplyService replyService;
    private final UserService userService;
    private final UserRepository userRepository;

    // 게시물 목록 조회 및 검색 처리
    @GetMapping
    public String getAllBoards(Model model, Principal principal,
                               // Spring Framework에서 HTTP 요청의 파라미터를 메서드의 매개변수로 바인딩하는 데 사용되는 어노테이션
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "searchType", required = false) String searchType,
                               Pageable pageable) {

        // 검색 결과를 저장할 변수
        Page<BoardDTO> boardPage;

        // 검색 키워드가 있을 경우 검색 처리
        if (keyword != null && !keyword.isEmpty()) {
            switch (searchType) {
                case "title":
                    boardPage = boardService.searchByTitle(keyword, pageable); // 제목으로 검색
                    break;
                case "content":
                    boardPage = boardService.searchByContent(keyword, pageable); // 내용으로 검색
                    break;
                case "writer":
                    boardPage = boardService.searchByWriter(keyword, pageable); // 작성자로 검색
                    break;
                default:
                    // 검색어가 없으면 전체 목록 페이징 처리
                    boardPage = boardService.paging(pageable); // 검색어가 없으면 전체 목록 페이징 처리
                    break;
            }
        } else {
            boardPage = boardService.paging(pageable); // 검색어가 없으면 전체 목록 페이징 처리
        }

        // 로그인된 사용자 정보 추가
        if (principal != null) {
            // 로그인된 사용자의 ID를 모델에 추가
            String userId = principal.getName();
            model.addAttribute("userId", userId);  // 이메일 또는 ID
        } else {
            model.addAttribute("userId", "Anonymous");
        }

        model.addAttribute("boards", boardPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);

        return "board/list"; // 게시물 목록 페이지로 이동
    }

    // 게시물 작성 페이지 이동
    @GetMapping("/new")
    public String addBoard(Model model, Principal principal) {
        // 새로운 게시물 DTO 생성
        BoardDTO boardDTO = new BoardDTO();
        // 작성자를 현재 로그인된 사용자로 설정
        boardDTO.setBoardWriter(principal.getName());
        // 모델에 게시물 DTO 추가
        model.addAttribute("boardDTO", boardDTO);
        // 사용자 ID 추가
        model.addAttribute("userId", principal.getName());
        return "board/add"; // 게시물 작성 페이지로 이동
    }

    // 게시물 작성 처리
    @PostMapping("/new")
    public String addBoard(@Valid @ModelAttribute("boardDTO") BoardDTO boardDTO,
                           BindingResult bindingResult, Model model, Principal principal) {

        // 현재 사용자 이름을 가져옴
        String username = principal.getName();

        // 입력값에 오류가 있을 경우 다시 작성 페이지로 이동
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", "입력값에 오류가 있습니다.");
            model.addAttribute("errors", bindingResult.getAllErrors()); // 오류 목록 추가
            return "board/add";
        }

        try {
            // 게시물 저장
            boardService.addBoard(boardDTO, username);
        } catch (Exception e) {
            // 오류가 발생한 경우 오류 메시지를 모델에 추가
            model.addAttribute("errorMsg", e.getMessage());
            return "board/add"; // 오류 발생 시 작성 페이지로 이동
        }
        return "redirect:/dadog/boards"; // 작성 완료 후 게시물 목록 페이지로 이동
    }

    // 특정 게시물 상세보기
    @GetMapping("/{boardNo}")
    public String getBoardByNo(@PathVariable Long boardNo, Model model, Principal principal) {

        // 게시물 번호로 게시물 조회, 없으면 예외 발생
        Board board = boardService.findBoardById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        // 조회수 증가
        boardService.viewBoard(boardNo);

        model.addAttribute("board", board);

        // 로그인된 사용자 정보 처리
        if (principal != null) {
            User user = principal.getName().contains("@") ?
                    userRepository.findByUserEmail(principal.getName()) :
                    userRepository.findByUserId(principal.getName());
            model.addAttribute("loggedInUserId", user.getUserId());
            model.addAttribute("userId", principal.getName()); // 로그인된 사용자 이메일
            model.addAttribute("userNo", user.getUserNo());    // 사용자 번호
        } else {
            // 비로그인 사용자는 guest로 처리
            model.addAttribute("loggedInUserId", null);
            model.addAttribute("userId", "guest");
        }


        //댓글 목록 조회
        List<ReplyDTO> replies = replyService.getReplyByBoardId(boardNo);
        // 로그인되지 않은 경우 guest로 처리
        model.addAttribute("replies", replies);
        System.out.println(" **********  확인해봅시다  :  " + replies);
        return "board/get"; // 게시물 상세보기 페이지로 이동
    }

    // 게시물 수정 페이지로 이동
    @GetMapping("/{boardNo}/update")
    public String updateBoard(@PathVariable Long boardNo, Model model, Principal principal) {

        // 게시물 번호로 게시물 조회, 없으면 예외 발생
        Board board = boardService.findBoardById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

//        이거 때문에 무한루프 돌아서 에러남 Because) board를 참조하고있는 Reply 때문/ @ToString 걸어놔서
//        System.out.println("************************* dto  :  " + board);

        // 조회된 게시물 정보를 DTO로 변환하여 모델에 추가
        BoardDTO boardDTO = new BoardDTO(
                board.getBoardNo(),
                board.getBoardWriter(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getBoardViews(),
                board.getCreateTime(),
                board.getUpdateTime()
        );

        model.addAttribute("boardDTO", boardDTO); // 수정할 게시물 정보를 모델에 추가
        model.addAttribute("userId", (principal != null) ? principal.getName() : "Anonymous"); // 사용자 ID 추가

        return "board/update"; // 게시물 수정 페이지로 이동
    }
    @PostMapping("/{boardNo}/update")
    public String updateBoard(@PathVariable Long boardNo,
                              @Valid @RequestBody UpdateBoardDTO boardDTO,
                              // BindingResult : 폼 데이터의 유효성 검사를 처리하는 데 사용되는 객체입니다. 주로 컨트롤러에서 폼 데이터가 바인딩될 때, 유효성 검사 결과 및 오류 정보를 담고 있음
                              BindingResult bindingResult, Model model, Principal principal) {

        // 현재 게시물 조회
        Board board = boardService.findBoardById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));


        // 현재 사용자가 게시물 작성자인지 확인
        if (!board.getBoardWriter().equals(principal.getName())) {
            // 권한이 없는 사용자 접근 시 처리
            return "redirect:/dadog/boards?error=unauthorized";
        }

        // 입력값에 오류가 있을 경우 다시 수정 페이지로 이동
        if (bindingResult.hasErrors()) {
            System.out.println("BindingResult Errors: " + bindingResult.getAllErrors());
            model.addAttribute("errorMsg", "입력값에 오류가 있습니다.");
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "board/update";
        }

        try {
            // 게시물 수정
            boardService.updateBoard(boardNo, boardDTO);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "board/update"; // 오류 발생 시 수정 페이지로 이동
        }
        return "redirect:/dadog/boards/" + boardNo; // 수정 완료 후 게시물 상세 페이지로 이동
    }

    // 게시물 삭제 처리
    @PostMapping("/{boardNo}/delete")
    public String deleteBoard(@PathVariable Long boardNo, Principal principal, RedirectAttributes redirectAttributes) {
        // 게시물 조회
        Board board = boardService.findBoardById(boardNo)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        // 현재 사용자가 게시물 작성자인지 확인
        if (!board.getBoardWriter().equals(principal.getName())) {
            // 권한이 없는 사용자 접근 시 처리
            redirectAttributes.addFlashAttribute("error", "삭제 권한이 없습니다.");
            System.out.println("권한 없는 사용자 접근: " + principal.getName());

            return "redirect:/dadog/boards/" + boardNo;
        }

        try {
            // 게시물 삭제 처리
            boardService.deleteBoard(boardNo);
            redirectAttributes.addFlashAttribute("success", "게시물이 성공적으로 삭제되었습니다.");
            System.out.println("게시물 삭제 성공: " + boardNo);

        } catch (Exception e) {
            // 삭제 도중 오류가 발생한 경우 처리
            redirectAttributes.addFlashAttribute("error", "게시물 삭제 중 오류가 발생했습니다.");
            System.out.println("게시물 삭제 중 오류: " + e.getMessage());

            return "redirect:/dadog/boards/" + boardNo;
        }
        return "redirect:/dadog/boards"; // 삭제 완료 후 게시물 목록 페이지로 이동
    }

}
