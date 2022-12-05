package service;

import java.sql.SQLException;

import model.ReqStatus;
import model.ShiftRequest;
import model.ShiftType;
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
	public void ApproveShift(Integer id, String status) throws SQLException {
		NotificationObserver observer = this.makeShiftApprovalObserver();
		ShiftRequest shiftRequest = shiftReqRepo.Get(id);
		shiftRequest.setStatus(ReqStatus.valueOf(status.toUpperCase()));
		User emp = userRepo.GetByPk(shiftRequest.getEmpId());
		observer.setUser(emp);
		this.shiftReqRepo.UpdateStatus(shiftRequest);
		this.userRepo.UpdateShift(emp.getId(), ShiftType.valueOf(shiftRequest.getNewShift().name().toUpperCase()));
		observer.Notify();
	}
	
}
