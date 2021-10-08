package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.mvc.annotation.reslovers.ModelAttribute;
import kr.or.ddit.mvc.annotation.reslovers.RequestHeader;
import kr.or.ddit.mvc.annotation.reslovers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.prod.dao.OthersDAO;
import kr.or.ddit.prod.dao.OthersDAOImpl;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ProdVO;
import kr.or.ddit.vo.SearchVO;

/**
 * 검색 조건 : 상품명, 거래처명, 분류명 세가지 검색 타입으로 검색 가능.
 * 	1. 세가지 검색 조건 중 하나만을 사용해서 검색.
 *     동기요청 
 *  2. 두가지 이상의 검색 조건을 동시 사용.
 *     비동기 요청   
 *
 */
@Controller
public class ProdRetrieveController extends HttpServlet{
	
	private ProdService service = new ProdServiceImpl();
	private OthersDAO othersDAO = new OthersDAOImpl();
	
	private void addAttribute(HttpServletRequest req) {
		req.setAttribute("lprodList", othersDAO.selectLprodList());
		req.setAttribute("buyerList", othersDAO.selectBuyerList(null));
	}
	
	@RequestMapping("/prod/prodList.do")
	public String prodList(@RequestParam(value = "page", required = false, defaultValue = "1") int currentPage, 
					@RequestHeader(value = "accept", required = false, defaultValue = "html") String accept,
					@ModelAttribute("detailSearch") ProdVO detailSearch,
					HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	//	String accept = req.getHeader("Accept");
		
		addAttribute(req);
 		
 		PagingVO<ProdVO> pagingVO = new PagingVO<>();
 		pagingVO.setCurrentPage(currentPage);
 		pagingVO.setDetailSearch(detailSearch);
 		
 		service.retrieveProdList(pagingVO);
 		
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
 			
 			return "prod/prodList";
 		}
	}
	
	@RequestMapping("/prod/prodView.do")
	public String prodView(@RequestParam(value = "what", required = true ) 
				String prodId, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ProdVO prod = service.retrieveProd(prodId);
		req.setAttribute("prod", prod);
		
		String viewName = "prod/prodView";
		
		return viewName;
	}
}

















