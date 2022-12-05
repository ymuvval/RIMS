package model;

import java.util.ArrayList;

public class Manager extends User {
	private ArrayList<Employee> employees;

	public ArrayList<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
	}
	
	public Employee getEmployee(int empId) {
		for (Employee emp : this.employees) {
			if (emp.getId() == empId) {
				return emp;
			}
		}
		return null;
	}
	
	public Employee getEmployee(String empEmail) {
		for (Employee emp : this.employees) {
			if (emp.getEmail() == empEmail) {
				return emp;
			}
		}
		return null;
	}
}
