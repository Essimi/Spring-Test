package kr.or.ddit.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import kr.or.ddit.filter.wrapper.SampleTrickHttpServletRequestWrapper;

public class SampleFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) // 필터링 수행
			throws IOException, ServletException { // request - 원본 요청, 변경시 값들을 바꾼다는 의미
		
		SampleTrickHttpServletRequestWrapper wrapper = new SampleTrickHttpServletRequestWrapper((HttpServletRequest)request);
		
		chain.doFilter(wrapper, response); // 
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
