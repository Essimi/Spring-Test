package kr.or.ddit.vo;

import java.util.List;

public class EmployeeWrapper implements FancyTreeNode{
	
	private EmployeeVO employee;
	
	public EmployeeWrapper(EmployeeVO employee) {
		
		this.employee = employee;
	}

	// 제목 생성
	@Override
	public String getTitle() {
		return employee.getFirstName() + " " + employee.getLastName();
	}
	
	// 키 생성
	@Override
	public String getKey() {
		return employee.getEmployeeId().toString();
	}

	// 폴더를 만들어야 하는지
	@Override
	public boolean isFolder() {
		return employee.getChildCount() > 0;
	}

	// lazy?
	@Override
	public boolean isLazy() {
		return isFolder();
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FancyTreeNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
}
