package kr.or.ddit.mvc.annotation.resolvers;

import java.io.IOException;

import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 
 * 요청 파라미터 하나의 값을 핸들러 메소드의 파라미터로 전달하기 위한 객체
 *
 */
public class RequestParamArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean isSupported(Parameter parameter) {
		
		Class<?> parametertype = parameter.getType();
		
		RequestParam annotation = parameter.getAnnotation(RequestParam.class);
		
		return (ClassUtils.isPrimitiveOrWrapper(parametertype)||
				String.class.equals(parametertype)) && annotation != null;
		
	
	}

	@Override
	public Object argumentResolve(Parameter parameter, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Class<?> parameterType = parameter.getType();
		
		RequestParam annotation = parameter.getAnnotation(RequestParam.class);
		
		String requestParamName = annotation.value(); // 이름값 뽑기
		
		String requestParam = req.getParameter(requestParamName);
		
		boolean requird = annotation.required();
		
		if(requird && StringUtils.isBlank(requestParam)) { // 필수 파라메터인데, 데이터가 넘어오지 않았다면
			throw new BadRequestException(requestParamName + " 파라메터 누락");
		}else if(!requird && StringUtils.isBlank(requestParam)){ // 필수 파라메터가 아닌데, 데이터가 넘어오지 않았다면
			requestParam = annotation.defaultValue();
		}
		
		Object paramValue = null;
		if(byte.class.equals(parameterType) || Byte.class.equals(parameterType)) {
			paramValue = Byte.parseByte(requestParam);
		}else if(short.class.equals(parameterType) || Short.class.equals(parameterType)) {
			paramValue = Short.parseShort(requestParam);
		}else if(int.class.equals(parameterType) || Integer.class.equals(parameterType)) {
			paramValue = Integer.parseInt(requestParam);
		}else if(long.class.equals(parameterType) || Long.class.equals(parameterType)) {
			paramValue = Long.parseLong(requestParam);
		}else if(float.class.equals(parameterType) || Float.class.equals(parameterType)) {
			paramValue = Float.parseFloat(requestParam);
		}else if(double.class.equals(parameterType) || Double.class.equals(parameterType)) {
			paramValue = Double.parseDouble(requestParam);
		}else if(boolean.class.equals(parameterType) || Boolean.class.equals(parameterType)) {
			paramValue = Boolean.parseBoolean(requestParam);
		}else if(char.class.equals(parameterType) || Character.class.equals(parameterType)) {
			paramValue = requestParam.charAt(0);
		}else {
			paramValue = requestParam;
		}
		
		return paramValue;
	}

}
