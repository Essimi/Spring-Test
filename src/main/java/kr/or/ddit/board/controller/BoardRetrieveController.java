package kr.or.ddit.board.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.resolvers.RequestHeader;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.SearchVO;

@Controller
public class BoardRetrieveController { 
	
	private BoardService service = new BoardServiceImpl();
	
	@RequestMapping("/board/boardView.do")
	public String boardView(@RequestParam("who") int boNo, HttpServletRequest req) {
		// what 이름을 가진 값을 받아온다.
		
		// 받아온 값을 토대로 메소드 실행
		BoardVO board = service.retrieveBoard(boNo);
		
		req.setAttribute("board", board);
		
		return "board/boardView";
	}
	
	@RequestMapping("/board/boardList.do")
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
						@ModelAttribute("searchVO") SearchVO searchVO,
						@RequestHeader("accept") String accept,
						HttpServletRequest req,
						HttpServletResponse resp) throws JsonGenerationException, JsonMappingException, IOException{

		PagingVO<BoardVO> pagingVO = new PagingVO<>(7, 5);
		pagingVO.setCurrentPage(currentPage);
		pagingVO.setSearchVO(searchVO);
		
		List<BoardVO> boardList = service.retrieveBoardList(pagingVO);
		
		pagingVO.setDataList(boardList);
		
		req.setAttribute("pagingVO", pagingVO);
		
		String viewName = null;
		
		if(accept.contains("json")) {
			resp.setContentType("application/json;charset=UTF-8");
			pagingVO = (PagingVO) req.getAttribute("pagingVO");
			try(
				PrintWriter out = resp.getWriter();	
			){
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(out, pagingVO);
			}
		}else {
			viewName = "board/boardList";
		}
		return viewName;
	}
	
}
