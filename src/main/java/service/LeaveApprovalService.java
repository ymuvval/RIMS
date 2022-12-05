package service;

import java.sql.SQLException;

import model.Leave;
import model.ReqStatus;
import model.User;
import repository.LeaveRepo;
import repository.UserRepo;

public class LeaveApprovalService implements LeaveApprovalHandler {
	private UserRepo userRepo;
	private LeaveRepo leaveRepo;
	
	public UserRepo getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	public LeaveRepo getLeaveRepo() {
		return leaveRepo;
	}
	
	public void setLeaveRepo(LeaveRepo leaveRepo) {
		this.leaveRepo = leaveRepo;
	}
	
	public NotificationObserver makeLeaveApprovalObserver() {
		return new LeaveApprovalObserver();
	}
	

	@Override
	public void ApproveLeave(Integer id, String status) throws SQLException {
		NotificationObserver observer = this.makeLeaveApprovalObserver();
		Leave leave = this.leaveRepo.Get(id);
		leave.setStatus(ReqStatus.valueOf(status.toUpperCase()));
		User emp = userRepo.GetByPk(leave.getEmpId());
		observer.setUser(emp);
		leaveRepo.UpdateStatus(leave);
		observer.Notify();
	}

}
