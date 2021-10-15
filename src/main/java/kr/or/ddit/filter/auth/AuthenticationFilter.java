package kr.or.ddit.filter.auth;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter implements Filter{
	
	// 경로 지정을 위한 final 변수 지정
	public static final String ATTRNAME = "securedResources";
	
	// properties 에 있는 값들을 가져오기 위한 Map 설정
	// String  - key 값, String[] - value 값, 여러개일 수 있기 때문에 배열의 형태로 받음
	private Map<String, String[]> securedResources;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("{} 초기화", getClass().getSimpleName());
		
		// 초기화, 객체 생성
		securedResources = new LinkedHashMap<>();
		
		// 객체 생성
		Properties props = new Properties();
		
		try(
			// 해당 경로에 있는 파일을 읽음
			InputStream inStream = AuthenticationFilter.class.getResourceAsStream("/kr/or/ddit/secureResources.properties");
		){
			// 위에서 읽은 파일을 다시 읽음
			props.load(inStream);
			
			for(Entry<Object, Object> entry : props.entrySet()) { // key 와 value 의 값이 모두 필요하기 때문에 Entry 사용
				String url = entry.getKey().toString(); // 주소값 가져오기
				String roles = entry.getValue().toString(); // 키값 가져오기
				String[] array = roles.replaceAll("\\s+", "").split(","); // 키값 자르기
				Arrays.sort(array); // 배열 정렬
				securedResources.put(url.trim(), array);
				// url - key 값(url 주소) , array - value 값 (ROLE_~~~)
				log.info("첫번째 필터 로그 : {} : {}", url, Arrays.toString(array));
			}
			filterConfig.getServletContext().setAttribute(ATTRNAME, securedResources);
		} catch(IOException e) {
			throw new ServletException(e);
		}
	}	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
//		1. 보호자원에 대한 요청이 아닌 경우.
//		2. 보호자원에 대한 요청인 경우.
//		2-1. 인증된 유저.
//		2-2 미인증 유저.
		
		// 페이지 실행시 해당 페이지 주소를 가져옴 (ex - /webStudy04_MVC/prod/prodList.do)
		String uri = req.getRequestURI().split(";")[0];
	
		// ex - /webStudy04_MVC
		String contextPath = req.getContextPath();
		
		// ex - /prod/prodList.do
		uri = uri.substring(contextPath.length());
	
		
		boolean pass = true;
		
		if(securedResources.containsKey(uri)) {
			Object authMember = req.getSession().getAttribute("authMember");
			pass = authMember != null;
		}
		
		if(pass) {
			chain.doFilter(request, response);
		}else {
			resp.sendRedirect(req.getContextPath() + "/login/loginForm.jsp");
		}
	}

	@Override
	public void destroy() {
		log.info("{} 소멸", getClass().getSimpleName());
		
	}
}
