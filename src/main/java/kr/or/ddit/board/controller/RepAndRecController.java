package kr.or.ddit.board.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.dao.BoardDAO;
import kr.or.ddit.board.dao.BoardDAOImpl;
import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;

@Controller
public class RepAndRecController {
	
	private BoardDAO dao = new BoardDAOImpl();
	
	@RequestMapping("/board/recommend.do") // 추천
	public String recommend(@RequestParam("what") int boNo,	HttpServletResponse resp) throws IOException {
		
		Map<String, Object> pMap = new HashMap<>();
		
		pMap.put("boNo", boNo);
		pMap.put("incType", "BO_REC");
		
		int cnt = dao.incrementCount(pMap);
		
		ServiceResult result = null;
		
		if(cnt>0) {
			result = ServiceResult.OK;
		}else {
			result = ServiceResult.FAILED;
		}
		try( // 직렬화
			PrintWriter out = resp.getWriter();	
		){
			out.print(result);
		}
		return null;
	}
	
	@RequestMapping("/board/report.do") // 신고
	public String report(@RequestParam("what") int boNo, HttpServletResponse resp) throws IOException {
		
		Map<String, Object> pMap = new HashMap<>();
		
		pMap.put("boNo", boNo);
		pMap.put("incType", "BO_REP");
		
		int cnt = dao.incrementCount(pMap);
		
		ServiceResult result = null;
		
		if(cnt>0) {
			result = ServiceResult.OK;
		}else {
			result = ServiceResult.FAILED;
		}
		try(
			PrintWriter out = resp.getWriter();	
		){
			out.print(result);
		}
		return null;
	}
}













