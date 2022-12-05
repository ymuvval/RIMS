package model;

import java.sql.Date;
//import java.time.LocalDate;

public class Leave {
	private Integer id;
	private Integer empId;
	private Integer managerId;
	private Date startDate;
	private Date endDate;
	private ReqStatus status; // Approved or Reject
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public ReqStatus getStatus() {
		return status;
	}
	public void setStatus(ReqStatus status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Integer getManagerId() {
		return managerId;
	}
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

}
