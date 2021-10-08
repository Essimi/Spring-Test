package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;

// D.L 도메인 생성 



@Data
public class EmployeeVO implements Serializable{

	private Integer employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String hireDate;
	private String jobId;
	private Integer salary;
	private Integer commissionPct;
	private Integer managerId;
	private Integer departmentId;
	
	private int childCount;
	
	private DepartmentVO department; // 1 : 1 // Has A
	
}
