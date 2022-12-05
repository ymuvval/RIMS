package service;

import java.sql.SQLException;

import model.Leave;
import model.ReqStatus;
import model.User;
import repository.LeaveRepo;
import repository.UserRepo;

public class LeaveRequestService implements LeaveRequestHandler {
	private UserRepo userRepo;
	public UserRepo getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public LeaveRepo getLeaveRepo() {
		return leaveRepo;
	}


	private LeaveRepo leaveRepo;

	
	public void setLeaveRepo(LeaveRepo leaveRepo) {
		this.leaveRepo = leaveRepo;
	}
	
	public NotificationObserver makeLeaveRequestObserver() {
		return new LeaveRequestObserver();
	}

	@Override
	public void RequestLeave(Leave leave) throws SQLException {
		NotificationObserver observer = this.makeLeaveRequestObserver();
//		assuming leave req is only for employees
		User emp = userRepo.GetByPk(leave.getEmpId());
		User manager = userRepo.GetByPk(emp.getManagerId());
		leave.setManagerId(manager.getId());
		leave.setStatus(ReqStatus.APPLIED);
		leaveRepo.Add(leave);
		observer.setUser(manager);
		observer.Notify();
	}
	
	@Override
	public void UpdateLeave(Leave leave) throws SQLException {
		NotificationObserver observer = this.makeLeaveRequestObserver();
//		assuming leave req is only for employees
		User emp = userRepo.GetByPk(leave.getEmpId());
		User manager = userRepo.GetByPk(emp.getManagerId());
		leave.setManagerId(manager.getId());
		leave.setStatus(ReqStatus.APPLIED);
		leaveRepo.Update(leave);
		observer.setUser(manager);
		observer.Notify();
	}


	@Override
	public void DeleteLeave(Integer id) throws SQLException {
		leaveRepo.Delete(id);
	}
}
