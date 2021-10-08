package kr.or.ddit.mvc.annotation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.mvc.annotation.reslovers.BadRequestException;
import kr.or.ddit.mvc.annotation.reslovers.HandlerMethodArgumentResolver;
import kr.or.ddit.mvc.annotation.reslovers.ModelAttributeArgumentResolver;
import kr.or.ddit.mvc.annotation.reslovers.RequestHeaderArgumentResolver;
import kr.or.ddit.mvc.annotation.reslovers.RequestParamArgumentResolver;
import kr.or.ddit.mvc.annotation.reslovers.ServletSpecArgumentResolver;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
	List<HandlerMethodArgumentResolver> argumentResolver;
	
	public RequestMappingHandlerAdapter() {
		super();
		argumentResolver = new ArrayList<>();
		argumentResolver.add(new ModelAttributeArgumentResolver());
		argumentResolver.add(new ServletSpecArgumentResolver());
		argumentResolver.add(new RequestParamArgumentResolver());
		argumentResolver.add(new RequestHeaderArgumentResolver());
	}
	
	
	private HandlerMethodArgumentResolver findArgumentResolver(Parameter parameter){ // 설계도에 없음
		
		HandlerMethodArgumentResolver finded = null;
		
		for(HandlerMethodArgumentResolver resolver : argumentResolver) {
			if(resolver.isSuppoited(parameter)) {
				finded = resolver;
				break;
			}
		}
		return finded;
	}
	

	@Override
	public String invokeHandler(RequestMappingInfo mappingInfo, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object commandHandler = mappingInfo.getCommandHandler();
		Method handlerMethod = mappingInfo.getHandlerMethod();
		Parameter[] parameters = handlerMethod.getParameters();
		try {
			Object[] parameterValues = null;
			if(parameters.length > 0) {
				parameterValues = new Object[parameters.length];
				for(int i = 0; i < parameterValues.length; i++) {
					HandlerMethodArgumentResolver finded = findArgumentResolver(parameters[i]);
					if(finded != null) {
						parameterValues[i] = finded.argumentResolve(parameters[i], req, resp);
					}else {
						throw new RuntimeException(String.format("현재 파라미터[%s]는 처리할 수 없는 형태임", parameters[i].toString()));
					}	
				}
			}
			String viewName = (String) handlerMethod.invoke(commandHandler, parameterValues);
			return viewName;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ServletException(e);
		} catch (BadRequestException e) {
			resp.sendError(400, e.getMessage());
			return null;
		}
	}

}
