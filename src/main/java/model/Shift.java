package model;

public class Shift {
	private Integer id;
	private Integer empId;
	private ShiftType type;
	
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public ShiftType getType() {
		return type;
	}
	public void setShift(ShiftType type) {
		this.type= type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
