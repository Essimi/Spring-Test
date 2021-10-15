package kr.or.ddit.mvc.annotation.resolvers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ClassUtils;

public class ModelAttributeArgumentResolver implements HandlerMethodArgumentResolver {
	// 	파라메터가 모델어트리뷰트를 가지고 있을때

	@Override
	public boolean isSupported(Parameter parameter) {
		
		Class<?> parameterType = parameter.getType();
		
		ModelAttribute annotation = parameter.getAnnotation(ModelAttribute.class);
		
		return !ClassUtils.isPrimitiveOrWrapper(parameterType) && annotation != null; //// INT/INTERGER 인지
		
		
	}

	@Override
	public Object argumentResolve(Parameter parameter, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Class<?> parameterType = parameter.getType();
		
		ModelAttribute annotation = parameter.getAnnotation(ModelAttribute.class);
		
		try {
			Object model = parameterType.newInstance(); // 객체 생성, 기본생성자가 없을 경우 생성되지 않음
			String attributeName = annotation.value();
			req.setAttribute("attribute", model);
			
			Map<String, String[]> paramterMap = req.getParameterMap();
			
			BeanUtils.populate(model, paramterMap);
			
			return model;
		
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new ServletException(e);
		} 
	}

}
