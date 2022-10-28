package service;

//import java.sql.*;
import db.*;

public class ManagerService extends UserService implements EmployeeManagementService, LeaveApprove {
	public ManagerService(DBConnectionPool connpool) {
		super(connpool);
		// TODO Auto-generated constructor stub
	}
	public void AddEmployee() {}
	public void DeleteEmployee() {}
	public void UpdateEmployee() {}
	public void ApproveLeave() {}
}
