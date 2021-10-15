package kr.or.ddit.employee.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.employee.service.EmployeeService;
import kr.or.ddit.employee.service.EmployeeServiceImpl;
import kr.or.ddit.mvc.annotation.resolvers.RequestHeader;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.EmployeeVO;
import kr.or.ddit.vo.PagingVO;

// /emp/employeeList.do
@Controller
public class EmployeeRetrieveController {
	
	private EmployeeService service = new EmployeeServiceImpl();
	
	
	
	
//	// 동기 요청 구조
//	@RequestMapping("/emp/employeeList.do") // GET 메소드로 매핑
//	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
//							   HttpServletRequest req){
//		
//		PagingVO<EmployeeVO> pagingVO = new PagingVO<>();
//		
//		pagingVO.setCurrentPage(currentPage);
//		
//		service.retrieveEmployeeList(pagingVO);
//		
//		req.setAttribute("pagingVO", pagingVO);
//			
//		return "employee/employeeList";
//		
//	}
	
	// 동기 요청 구조
		@RequestMapping("/emp/employeeList.do") // GET 메소드로 매핑
		public String list(HttpServletRequest req, @RequestHeader("accept") String accept,
								   HttpServletResponse resp) throws ServletException, IOException{
			
			// 응답데이터로 내보낼 배열을 캡슐화하기 위한 목적
			// DataList 존재
			PagingVO<EmployeeVO> pagingVO = new PagingVO<>(); //서버 사이드 구조
//			pagingVO.setCurrentPage(currentPage);
			
			
			List<EmployeeVO> empList = service.retrieveEmployeeList(pagingVO);
			
			// 마샬링
			if(StringUtils.containsIgnoreCase(accept, "json")) {
				resp.setContentType("application/json;charset=UTF-8");
				
				try(
					PrintWriter out = resp.getWriter();	
				){
					ObjectMapper mapper = new ObjectMapper();
					mapper.writeValue(out, pagingVO);
				}
				
				return null;
				
			}else {
				req.setAttribute("pagingVO", pagingVO);
				
				return "employee/employeeList";
			}
			
			
	}
}
