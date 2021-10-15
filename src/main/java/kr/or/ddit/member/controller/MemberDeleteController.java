package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.MemberVO;

@Controller
public class MemberDeleteController{
	private MemberService service = new MemberServiceImpl();
	
	
	@RequestMapping(value = "/member/memberDelete.do", method = RequestMethod.POST)
	public String delete(HttpSession session, @RequestParam("memPass") String memPass){
		
		MemberVO authMember = (MemberVO) session.getAttribute("authMember");
		
	 	ServiceResult result = service.removeMember(new MemberVO(authMember.getMemId(), memPass));
	 	String viewName = null;
	 	String message = null;
	 	switch (result) {
		case INVALIDPASSWORD:
			viewName = "redirect:/mypage.do";
			message = "비번 오류";
			session.setAttribute("message", message);
			break;
		case OK:
			session.invalidate();
			viewName = "redirect:/";
			break;
		default:
			viewName = "redirect:/mypage.do";
			message = "서버 오류";
			session.setAttribute("message", message);
			break;
		}
	 	
	 	return viewName;
	}
}













