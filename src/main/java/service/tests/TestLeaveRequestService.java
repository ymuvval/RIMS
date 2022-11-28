package service.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import model.ReqStatus;
import model.User;
import repository.LeaveRepo;
import repository.UserRepo;
import service.LeaveRequestService;
import service.NotificationObserver;


@ExtendWith(MockitoExtension.class)
class TestLeaveRequestService {

	@Mock UserRepo userRepo;
	@Mock LeaveRepo leaveRepo;
	
	@InjectMocks LeaveRequestService lrs;
	
	@Test
	void testRequestLeavePositive() throws SQLException {
		lrs = Mockito.spy(lrs);
		
		Leave leaveSample = new Leave();
		leaveSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		User managerSample = new User() {};
		managerSample.setId(2);
		
		NotificationObserver observer = lrs.makeLeaveRequestObserver();
		observer = spy(observer);
		when(lrs.makeLeaveRequestObserver()).thenReturn(observer);
		when(userRepo.GetByPk(any())).thenReturn(empSample).thenReturn(managerSample);
		
		lrs.RequestLeave(leaveSample);
		
		assertEquals(ReqStatus.APPLIED, leaveSample.getStatus());
		verify(userRepo).GetByPk(1);
		verify(userRepo).GetByPk(2);
		verify(observer).setUser(managerSample);
		verify(observer).Notify();
		
	}

	@Test
	void testUpdateLeavePositive() throws SQLException {
		lrs = Mockito.spy(lrs);
		
		Leave leaveSample = new Leave();
		leaveSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(3);
		
		User managerSample = new User() {};
		managerSample.setId(3);
		
		NotificationObserver observer = lrs.makeLeaveRequestObserver();
		observer = spy(observer);
		when(lrs.makeLeaveRequestObserver()).thenReturn(observer);
		when(userRepo.GetByPk(any())).thenReturn(empSample).thenReturn(managerSample);
		
		lrs.UpdateLeave(leaveSample);
		
		assertEquals(ReqStatus.APPLIED, leaveSample.getStatus());
		verify(userRepo).GetByPk(1);
		verify(userRepo).GetByPk(3);
		verify(observer).setUser(managerSample);
		verify(observer).Notify();
	}

	@Test
	void testDeleteLeavePositive() throws SQLException {
		lrs.DeleteLeave(1);
		verify(leaveRepo).Delete(1);
	}
	
	@Test
	void testRequestLeaveNegative() throws SQLException {
		lrs = Mockito.spy(lrs);
		
		Leave leaveSample = new Leave();
		leaveSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		User managerSample = new User() {};
		managerSample.setId(2);
		
		when(userRepo.GetByPk(any())).thenThrow(new SQLException()).thenReturn(managerSample);
		
		assertThrows(SQLException.class, () -> lrs.RequestLeave(leaveSample));
		
		verify(userRepo).GetByPk(1);
	}

	@Test()
	void testUpdateLeaveNegative() throws SQLException {
		lrs = Mockito.spy(lrs);
		
		Leave leaveSample = new Leave();
		leaveSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		User managerSample = new User() {};
		managerSample.setId(2);
		
		when(userRepo.GetByPk(1)).thenThrow(new SQLException());
		
		Exception exc = assertThrows(SQLException.class, () -> lrs.UpdateLeave(leaveSample));
		
		assertTrue(exc instanceof SQLException);
		
		verify(userRepo).GetByPk(1);
	}

	@Test
	void testDeleteLeaveNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(leaveRepo).Delete(1);
		assertThrows(SQLException.class, () -> lrs.DeleteLeave(1));
	}

}
