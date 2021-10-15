package kr.or.ddit.mvc.annotation.reslovers;

import java.io.IOException;

import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 
 * 요청 헤더 하나의 값을 핸들러 메소드의 파라미터로 전달하기 위한 객체
 *
 */
public class RequestHeaderArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean isSuppoited(Parameter parameter) {
		
		Class<?> headerType = parameter.getType();
		
		RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);
		
		return (ClassUtils.isPrimitiveOrWrapper(headerType)||
				String.class.equals(headerType)) && annotation != null;
		
	
	}

	@Override
	public Object argumentResolve(Parameter parameter, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Class<?> headerType = parameter.getType();
		
		RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);
		
		String requestHeaderName = annotation.value(); // 이름값 뽑기
		
		String requestHeader = req.getHeader(requestHeaderName);
		
		boolean requird = annotation.required();
		
		if(requird && StringUtils.isBlank(requestHeader)) { // 필수 파라메터인데, 데이터가 넘어오지 않았다면
			throw new BadRequestException(requestHeaderName + " 파라메터 누락");
		}else if(!requird && StringUtils.isBlank(requestHeader)){ // 필수 파라메터가 아닌데, 데이터가 넘어오지 않았다면
			requestHeader = annotation.defaultValue();
		}
		
		Object headerValue = null;
		if(byte.class.equals(headerType) || Byte.class.equals(headerType)) {
			headerValue = Byte.parseByte(requestHeader);
		}else if(short.class.equals(headerType) || Short.class.equals(headerType)) {
			headerValue = Short.parseShort(requestHeader);
		}else if(int.class.equals(headerType) || Integer.class.equals(headerType)) {
			headerValue = Integer.parseInt(requestHeader);
		}else if(long.class.equals(headerType) || Long.class.equals(headerType)) {
			headerValue = Long.parseLong(requestHeader);
		}else if(float.class.equals(headerType) || Float.class.equals(headerType)) {
			headerValue = Float.parseFloat(requestHeader);
		}else if(double.class.equals(headerType) || Double.class.equals(headerType)) {
			headerValue = Double.parseDouble(requestHeader);
		}else if(boolean.class.equals(headerType) || Boolean.class.equals(headerType)) {
			headerValue = Boolean.parseBoolean(requestHeader);
		}else if(char.class.equals(headerType) || Character.class.equals(headerType)) {
			headerValue = requestHeader.charAt(0);
		}else {
			headerValue = requestHeader;
		}
		
		return headerValue;
	}

}
