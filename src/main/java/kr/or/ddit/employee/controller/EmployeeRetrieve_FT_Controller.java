package kr.or.ddit.employee.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpPrincipal;

import kr.or.ddit.employee.dao.DepartmentDAO;
import kr.or.ddit.employee.service.EmployeeService;
import kr.or.ddit.employee.service.EmployeeServiceImpl;
import kr.or.ddit.mvc.annotation.resolvers.RequestHeader;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.DepartmentVO;
import kr.or.ddit.vo.DepartmentWrapper;
import kr.or.ddit.vo.EmployeeVO;
import kr.or.ddit.vo.EmployeeWrapper;
import kr.or.ddit.vo.FancyTreeNode;
import kr.or.ddit.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class EmployeeRetrieve_FT_Controller {
	
	private EmployeeService service = new EmployeeServiceImpl();
	
	private DepartmentDAO deptDAO;
	
	@RequestMapping("/emp/deptList.do")
	public String listDept(@RequestParam(value = "dept", required = false, defaultValue = "0") Integer dept,
							HttpServletResponse resp) throws IOException {
						   
		
		List<DepartmentVO> deptList = deptDAO.selectDepartMentList();
		
		List<FancyTreeNode> nodeList = new ArrayList<>(deptList.size());
		
		for(DepartmentVO tmp : deptList) {
			nodeList.add(new DepartmentWrapper(tmp));
		}
		
		resp.setContentType("application/json;charset=UTF-8");
		
		try(
			PrintWriter out = resp.getWriter();	
		){
			// 위에서 생성한 nodeList 를 전송함
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(out, nodeList);
		}
		
		return null;
	}

	@RequestMapping("/emp/employeeList_FT.do")
	public String  listFT(@RequestParam(value = "manager", required = false, defaultValue = "0") Integer manager,
						  HttpServletResponse resp,
						  @RequestHeader("accept") String accept) throws IOException{

		log.debug("manager = {}", manager);
		
		
		// manager 라는 옵셔널 파라미터를 통해 해당 부하직원의 목록을 조회.
		
		// 페이징을 위한 PagingVO 객체 생성(EmployeeVO 대입)
		PagingVO<EmployeeVO> pagingVO = new PagingVO<>();
		
		// EmployeeVO 객체 생성
		EmployeeVO vo = new EmployeeVO();
		
		// EmployeeVO 객체에 view에서 받아온 manager 값 set
		vo.setManagerId(manager);
		
		// 받아온 manager 값을 pagingVO의 detailSearch 에 대입
		pagingVO.setDetailSearch(vo);
		
		log.debug("vo = {}", vo);
		
		// EmployeeVO 는 여러개이기 때문에 리스트로 생성]
		// 위에서 지정한 값들을 기반으로 service 의 메소드 진입
		List<EmployeeVO> empList = service.retrieveEmployeeList(pagingVO);
		
		// FancyTree 구조를 사용하기 위한 배열형태의 객체 생성
		// Employee 사이즈만큼 만들기 때문에 위에서 생성한 empList의 객체 크기만큼 만든다
		List<FancyTreeNode> nodeList = new ArrayList<>(empList.size());
		
		// empList 의 값을 EmployeeVO 형태의 emp 값에 하나씩 대입
		for(EmployeeVO emp : empList) {
			// 나온 emp 값(배열형태의 값, 인덱스 첫번째부터 실행)
			// 을 EmplyeeWrapper 메소드에 대입 후 nodeList에 add
			nodeList.add(new EmployeeWrapper(emp));
			log.debug("emp = {}", emp);
			log.debug("nodeList = {}", nodeList);
		}
		
		// 마샬링, 요청헤더가 (accept) json 일경우 실행
		if(StringUtils.containsIgnoreCase(accept, "json")) {
			resp.setContentType("application/json;charset=UTF-8");
			
			try(
				PrintWriter out = resp.getWriter();	
			){
				// 위에서 생성한 nodeList 를 전송함
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(out, nodeList);
			}
			
			return null;
			
		}else {
		
			return "employee/employeeList_FT";
			
		}
	}
	
}
