package kr.or.ddit.employee.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.EmployeeVO;
import kr.or.ddit.vo.PagingVO;

public interface EmployeeDAO {
	
	public int insertEmployee(EmployeeDAO employee);
	public List<EmployeeVO> selectEmployeeList(@Param("pagingVO") PagingVO<EmployeeVO> pagingVO); // 페이징 처리 필요
	public int selectTotalRecord(PagingVO<EmployeeVO> pagingVO); // 페이징 처리를 위한 메소드
	public EmployeeVO selectEmployee(String empId); // 직원 개인 정보
	public int updateEmployee(EmployeeVO employee);
	public int deleteEmployee(String empId);

}
 