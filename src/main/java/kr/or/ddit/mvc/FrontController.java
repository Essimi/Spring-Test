package kr.or.ddit.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.mvc.annotation.HandlerAdapter;
import kr.or.ddit.mvc.annotation.HandlerMapping;
import kr.or.ddit.mvc.annotation.RequestMappingHandlerAdapter;
import kr.or.ddit.mvc.annotation.RequestMappingHandlerMapping;
import kr.or.ddit.mvc.annotation.RequestMappingInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrontController extends HttpServlet{
	
	private HandlerMapping handlerMapping;
	private HandlerAdapter handlerAdapter;
	private ViewResolver viewResolver;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		// web.xml 에 있는 param name 등록(kr.or.ddit)
		String basePackages = config.getInitParameter("basePackages");
		//log.debug("basePackages  = {}", basePackages);
		
		// web.xml 에 있는 prefix 등록(/WEB-INF/views)
		String prefix = config.getInitParameter("prefix");
		//log.debug("prefix  = {}", prefix);
		
		// web.xml 에 있는 suffix 등록(.jsp)
		String suffix = config.getInitParameter("suffix");
		//log.debug("suffix  = {}", suffix);
		
		
		handlerMapping = new RequestMappingHandlerMapping(basePackages.split("\\s+"));
		handlerAdapter = new RequestMappingHandlerAdapter();
		
		log.debug("Mapping : {}", handlerMapping);
		log.debug("Adapter : {}", handlerAdapter);
		
		
		viewResolver = new InternalResourceViewResolver();
		
		//log.debug("viewResolver : {}", viewResolver);
		((InternalResourceViewResolver)viewResolver).setPrefix(prefix);
		((InternalResourceViewResolver)viewResolver).setSuffix(suffix);
		//log.debug("viewResolver2 : {}", viewResolver.toString());
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		log.debug("req = {}", req);
		
		RequestMappingInfo mappingInfo = handlerMapping.findCommandHanler(req);
		
		log.debug("mapping = {}", mappingInfo);
		
		if(mappingInfo == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, mappingInfo+"는 제공하지 않는 서비스임.");
			return;
		}
		
		String viewName = handlerAdapter.invokeHandler(mappingInfo, req, resp);
		
		log.debug("vvvaaa = {}", viewName);
		
		if(viewName == null) {
			if(!resp.isCommitted()) {
				// 메소드 지원되지 않는 경우,
				// 개발자가 잘못 만든 경우,
				resp.sendError(500);
			}
			// backend controller 에서 응답이 이미 결정된 경우,
			return;
		}else {
			viewResolver.viewResolve(viewName, req, resp);
		}
	}
}
