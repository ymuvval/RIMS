package service;

import java.sql.SQLException;

import model.Employee;
import model.User;

public interface EmployeeManagementService {
	public User AddEmployee(Employee emp) throws SQLException;
	public void DeleteEmployee(Integer id) throws SQLException;
	public void UpdateEmployee(Integer id, String name) throws SQLException;
}
