package kr.or.ddit.mvc.annotation.reslovers;

import java.io.IOException;
import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletSpecArgumentResolver implements HandlerMethodArgumentResolver {
	// 리퀘스트, 리스폰스, 세션 처리 클래스

	@Override
	public boolean isSuppoited(Parameter parameter) {
		
		Class<?> parameterType = parameter.getType(); // 클래스 형태를 가져옴, 리퀘스트인지 리스폰스인지 세션인지
		
		return HttpServletResponse.class.isAssignableFrom(parameterType) ||
				HttpServletRequest.class.isAssignableFrom(parameterType) ||
				HttpSession.class.isAssignableFrom(parameterType);
	}

	@Override
	public Object argumentResolve(Parameter parameter, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		Class<?> parameterType = parameter.getType(); // 클래스 형태를 가져옴, 리퀘스트인지 리스폰스인지 세션인지

		Object parameterValue = null;
		
		if(HttpServletRequest.class.isAssignableFrom(parameterType)) {
			parameterValue = req;
		}else if(HttpServletResponse.class.isAssignableFrom(parameterType)) {
			parameterValue = resp;
		}else {
			parameterValue = req.getSession();
		}
		
		return parameterValue;
	}
}
