package kr.or.ddit.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterDesc implements Filter{
	
//	Examples that have been identified for this design are:

//	Authentication Filters
//	Logging and Auditing Filters
//	Image conversion Filters
//	Data compression Filters
//	Encryption Filters
//	Tokenizing Filters
//	Filters that trigger resource access events
//	XSL/T filters
//	Mime-type chain Filter
//	Since:
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info(getClass().getSimpleName() + " 초기화 되었음.");
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		long start = System.currentTimeMillis(); // 시간 스타트
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		String url = req.getRequestURI();
		
		String method = req.getMethod();
		
		log.info("url : {}, method : {}", url, method);
		
		//. request filtering
		chain.doFilter(request, response); // 다음 필터로 제어권을 넘김, 없다면 Front 로 넘김
		//. response filtering
		
		long end = System.currentTimeMillis(); // 시간 종료
		log.info("url : {}, method : {}, 소요시간 : {}ms", url, method, (end-start));
		
	}

	@Override
	public void destroy() {
		log.info(getClass().getSimpleName() + " 소멸되었음.");	
	}
}
