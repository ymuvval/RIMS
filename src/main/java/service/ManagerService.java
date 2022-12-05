package service;

import java.sql.SQLException;
import java.util.ArrayList;

import Exception.UserAlreadyExist;
import model.Employee;
import model.ShiftType;
import model.User;
import repository.ManagerRepo;
import repository.UserRepo;

public class ManagerService extends UserService implements EmployeeManagementService, SignUp {
	private ManagerRepo managerRepo = null;
	
	public ManagerRepo getManagerRepo() {
		return managerRepo;
	}

	public void setManagerRepo(ManagerRepo managerRepo) {
		this.managerRepo = managerRepo;
	}

	public ManagerService() {
		super();
	}
	
	public ManagerService(ManagerRepo mR, UserRepo uR) {
		super(uR);
		this.setManagerRepo(mR);
	}
	
	@Override
	public User AddEmployee(Employee emp) throws SQLException {
		return this.managerRepo.Add(emp);
	}
	
	@Override
	public void DeleteEmployee(Integer id) throws SQLException {
		this.managerRepo.Delete(id);
	}
	
	@Override
	public void UpdateEmployee(Integer id, String name, ShiftType shift) throws SQLException {
		this.managerRepo.Update(id, name, shift);
	}
	
	@Override
	public User CreateUser(User user) throws SQLException, UserAlreadyExist {
		if (this.IsUserPresent(user.getEmail())) {
			throw new UserAlreadyExist("User already present");
		}
		return this.managerRepo.Create(user);
	}
	
	public ArrayList<User> GetEmployees(Integer id) throws SQLException {
		return this.managerRepo.GetEmployees(id);
	}

}
