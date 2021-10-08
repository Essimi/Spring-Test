package kr.or.ddit.member.controller;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.reslovers.ModelAttribute;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.util.ValidateUtils;
import kr.or.ddit.validate.groups.UpdateGroup;
import kr.or.ddit.vo.MemberVO;

@Controller
public class MemberUpdateController{
private MemberService service = new MemberServiceImpl();
	
	@RequestMapping("/member/memberUpdate.do")
	public String form(HttpSession session, HttpServletRequest req){
		MemberVO authMember = (MemberVO) session.getAttribute("authMember");
		MemberVO member = service.retrieveMember(authMember.getMemId());
		
		req.setAttribute("member", member);
		
		return "member/memberForm";
	}
	
	@RequestMapping(value = "/member/memberUpdate.do", method = RequestMethod.POST)
	public String process(@ModelAttribute("member") MemberVO member, HttpServletRequest req){
		
		Map<String, List<String>> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		boolean valid = ValidateUtils.validate(member, errors, UpdateGroup.class);
		
		String viewName = null;
		String message = null;
		if(valid) {
//		3-1. 통과
//			로직 사용
			ServiceResult result = service.modifyMember(member);
			switch(result) {
			case INVALIDPASSWORD:
//			비번오류 : memberForm 으로 이동(기존 데이터 + 메시지 전달).
				viewName = "member/memberForm";
				message = "비번 오류";
				break;
			case OK:
//			OK : 웰컴 페이지로 이동
				viewName = "redirect:/mypage.do";
				break;
			default:
//			FAIL : memberForm 으로 이동(기존 데이터 + 메시지 전달).
				viewName = "member/memberForm";
				message = "서버 오류, 잠시뒤 다시 해보셈.";
			}
			
		}else {
//		3-2. 불통
//			: memberForm으로 이동 (기존데이터 + 검증 결과 메시지 전달)
			viewName = "member/memberForm";
			
		}
		
		req.setAttribute("message", message);
		
		return viewName;
		
 	}


}