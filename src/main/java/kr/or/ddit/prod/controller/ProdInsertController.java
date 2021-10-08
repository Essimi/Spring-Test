package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.prod.dao.OthersDAO;
import kr.or.ddit.prod.dao.OthersDAOImpl;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.util.ValidateUtils;
import kr.or.ddit.validate.groups.InsertGroup;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ProdVO;

@Controller 
public class ProdInsertController{
	private ProdService service = new ProdServiceImpl();

	@RequestMapping("/prod/prodForm.do") // 핸들러매핑이 해당주소를 트레이싱
	public String prodForm(HttpServletRequest req, HttpServletResponse resp) {
		
		return "prod/prodForm.do"; // 핸들러어댑터가 가져가고, 프론트에 넘기고, 뷰리절브에 넘김
		
		
	}
	
	@RequestMapping(value = "/prod/prodForm.do", method = RequestMethod.POST)
	public String process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ProdVO prod = new ProdVO();
		req.setAttribute("prod", prod);
		
		Map<String, String[]> paramterMap = req.getParameterMap();
		try {
			BeanUtils.populate(prod, paramterMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ServletException(e);
		}
		Map<String, List<String>> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		boolean valid = ValidateUtils.validate(prod, errors, InsertGroup.class);
		
		String viewName = null;
		String message = null;
		if(valid) {
			ServiceResult result = service.createProd(prod);
			switch(result) {
			case OK:
				viewName = "redirect:/prod/prodView.do?what="+prod.getProdId();
				break;
			default:
				viewName = "prod/prodForm";
				message = "서버 오류, 잠시뒤 다시 해보셈.";
			}
			
		}else {
//		3-2. 불통
			viewName = "prod/prodForm";
			
		}
		
		req.setAttribute("message", message);
		
		return viewName;
		
	}
}
