package service;

import java.sql.SQLException;

import model.ReqStatus;
import model.ShiftRequest;
import model.User;
import repository.ShiftReqRepo;
import repository.UserRepo;

public class ShiftRequestService implements ShiftRequestHandler {
	private UserRepo userRepo;
	private ShiftReqRepo shiftReqRepo;
	
	public UserRepo getUserRepo() {
		return userRepo;
	}


	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public void setShiftRepo(ShiftReqRepo shiftReqRepo) {
		this.shiftReqRepo = shiftReqRepo;
	}
	
	public void setShiftReqRepo(ShiftReqRepo shiftReqRepo) {
		this.shiftReqRepo = shiftReqRepo;
	}
	
	public NotificationObserver makeShiftReqObserver() {
		return new ShiftRequestObserver();
	}

	@Override
	public void RequestShift(ShiftRequest shiftReq) throws SQLException {
		NotificationObserver observer = this.makeShiftReqObserver();
//			assuming leave req is only for employees
		User emp = userRepo.GetByPk(shiftReq.getEmpId());
		User manager = userRepo.GetByPk(emp.getManagerId());
		shiftReq.setManagerId(manager.getId());
		shiftReq.setStatus(ReqStatus.APPLIED);
		shiftReqRepo.Add(shiftReq);
		observer.setUser(manager);
		observer.Notify();
	}
	
	@Override
	public void UpdateShift(ShiftRequest shiftReq) throws SQLException {
		NotificationObserver observer = this.makeShiftReqObserver();
//			assuming leave req is only for employees
		User emp = userRepo.GetByPk(shiftReq.getEmpId());
		User manager = userRepo.GetByPk(emp.getManagerId());
		shiftReq.setManagerId(manager.getId());
		shiftReq.setStatus(ReqStatus.APPLIED);
		shiftReqRepo.Update(shiftReq);
		observer.setUser(manager);
		observer.Notify();
	}


	@Override
	public void DeleteShift(Integer id) throws SQLException {
		shiftReqRepo.Delete(id);
	}

}
