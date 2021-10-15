package kr.or.ddit.filter.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SampleTrickHttpServletRequestWrapper extends HttpServletRequestWrapper{ // 래퍼 지정

	public SampleTrickHttpServletRequestWrapper(HttpServletRequest request) { // 생성자 추가, 무조건 해줘야 됨
		super(request);
		// TODO Auto-generated constructor stub
	} 
	
	@Override
	public String getParameter(String name) {
		if("what".equals(name)) {
			return "P101000001";
		}else {
			return super.getParameter(name);
		}
			
	}
}
