package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.reslovers.ModelAttribute;
import kr.or.ddit.mvc.annotation.reslovers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.util.ValidateUtils;
import kr.or.ddit.validate.groups.UpdateGroup;
import kr.or.ddit.vo.ProdVO;

@Controller
public class ProdUpdateController {
	
	private ProdService service = new ProdServiceImpl();
	
	@RequestMapping("/prod/prodUpdate.do")
	public String form(@RequestParam("what")String prodId, HttpServletRequest req){
		
		ProdVO prod =service.retrieveProd(prodId);
		
		req.setAttribute("prod", prod);
		
		return "prod/prodForm";
	}
	
	@RequestMapping(value = "/prod/prodUpdate.do", method = RequestMethod.POST)
	public String process(@ModelAttribute("prod") ProdVO prod, HttpServletRequest req){
		
		Map<String, List<String>> errors = new LinkedHashMap<>();
		
		req.setAttribute("errors", errors);
		
		boolean valid = ValidateUtils.validate(prod, errors, UpdateGroup.class);
		
		String viewName = null;
		
		if(valid) {
			
			ServiceResult result = service.modifyProd(prod);
			
			if(ServiceResult.OK.equals(result)) {
				viewName = "redirect:/prod/prodView.do?what=" + prod.getProdId();
			}else {
				req.setAttribute("message", "서버 오류");
				viewName = "prod/prodForm";
			}
		}else {
			viewName = "prod/prodForm";
		}
		return viewName;
	}
}
