package service.tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Leave;
import model.User;
import repository.LeaveRepo;
import repository.UserRepo;
import service.LeaveApprovalService;
import service.NotificationObserver;

@ExtendWith(MockitoExtension.class)
class TestLeaveApprovalService {
	
	@Mock UserRepo userRepo;
	@Mock LeaveRepo leaveRepo;
	
	@InjectMocks LeaveApprovalService las;

	@Test
	void testLeaveApprovalServicePositive() throws SQLException {
		las = Mockito.spy(las);
		
		Leave leaveSample = new Leave();
		leaveSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(4);
		
		
		NotificationObserver observer = las.makeLeaveApprovalObserver();
		observer = spy(observer);
		when(las.makeLeaveApprovalObserver()).thenReturn(observer);
		when(leaveRepo.Get(1)).thenReturn(leaveSample);
		when(userRepo.GetByPk(1)).thenReturn(empSample);
		
		las.ApproveLeave(1, "APPROVED");
		
		verify(leaveRepo).UpdateStatus(leaveSample);
		verify(userRepo).GetByPk(1);
		verify(observer).setUser(empSample);
		verify(observer).Notify();
	}
	
	@Test
	void testLeaveApprovalServiceNegative() throws SQLException {
		las = Mockito.spy(las);
		
		Leave leaveSample = new Leave();
		leaveSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		when(leaveRepo.Get(1)).thenThrow(new SQLException());
		
		assertThrows(SQLException.class, () -> las.ApproveLeave(1, "APPROVED"));
	}

}
