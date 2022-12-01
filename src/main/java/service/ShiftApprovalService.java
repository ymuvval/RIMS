package service;

import java.sql.SQLException;

import model.ShiftRequest;
import model.User;
import repository.ShiftReqRepo;
import repository.UserRepo;

public class ShiftApprovalService implements ShiftApprovalHandler {
	private UserRepo userRepo;
	private ShiftReqRepo shiftReqRepo;
	

	public UserRepo getUserRepo() {
		return userRepo;
	}


	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}


	public ShiftReqRepo getShiftReqRepo() {
		return shiftReqRepo;
	}


	public void setShiftReqRepo(ShiftReqRepo shiftReqRepo) {
		this.shiftReqRepo = shiftReqRepo;
	}
	
	public NotificationObserver makeShiftApprovalObserver() {
		return new ShiftApprovalObserver();
	}

	@Override
	public void ApproveShift(Integer id) throws SQLException {
		NotificationObserver observer = this.makeShiftApprovalObserver();
		ShiftRequest shiftRequest = shiftReqRepo.Get(id);
		User emp = userRepo.GetByPk(shiftRequest.getEmpId());
		observer.setUser(emp);
		shiftReqRepo.UpdateStatus(shiftRequest);
		observer.Notify();
	}
	
}
