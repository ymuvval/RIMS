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

import model.ReqStatus;
import model.ShiftRequest;
import model.User;
import repository.ShiftReqRepo;
import repository.UserRepo;
import service.NotificationObserver;
import service.ShiftRequestService;


@ExtendWith(MockitoExtension.class)
class TestShiftRequestService {

	@Mock UserRepo userRepo;
	@Mock ShiftReqRepo shiftReqRepo;
	
	@InjectMocks ShiftRequestService srs;
	
	@Test
	void testRequestShiftRequestPositive() throws SQLException {
		srs = Mockito.spy(srs);
		
		ShiftRequest shiftReqSample = new ShiftRequest();
		shiftReqSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		User managerSample = new User() {};
		managerSample.setId(2);
		
		NotificationObserver observer = srs.makeShiftReqObserver();
		observer = spy(observer);
		when(srs.makeShiftReqObserver()).thenReturn(observer);
		when(userRepo.GetByPk(any())).thenReturn(empSample).thenReturn(managerSample);
		
		srs.RequestShift(shiftReqSample);
		
		assertEquals(ReqStatus.APPLIED, shiftReqSample.getStatus());
		verify(userRepo).GetByPk(1);
		verify(userRepo).GetByPk(2);
		verify(observer).setUser(managerSample);
		verify(observer).Notify();
		
	}

	@Test
	void testUpdateShiftRequestPositive() throws SQLException {
		srs = Mockito.spy(srs);
		
		ShiftRequest shiftReqSample = new ShiftRequest();
		shiftReqSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(3);
		
		User managerSample = new User() {};
		managerSample.setId(3);
		
		NotificationObserver observer = srs.makeShiftReqObserver();
		observer = spy(observer);
		when(srs.makeShiftReqObserver()).thenReturn(observer);
		when(userRepo.GetByPk(any())).thenReturn(empSample).thenReturn(managerSample);
		
		srs.UpdateShift(shiftReqSample);
		
		assertEquals(ReqStatus.APPLIED, shiftReqSample.getStatus());
		verify(userRepo).GetByPk(1);
		verify(userRepo).GetByPk(3);
		verify(observer).setUser(managerSample);
		verify(observer).Notify();
	}

	@Test
	void testDeleteShiftRequestPositive() throws SQLException {
		srs.DeleteShift(1);
		verify(shiftReqRepo).Delete(1);
	}
	
	@Test
	void testRequestShiftRequestNegative() throws SQLException {
		srs = Mockito.spy(srs);
		
		ShiftRequest shiftReqSample = new ShiftRequest();
		shiftReqSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		User managerSample = new User() {};
		managerSample.setId(2);
		
		when(userRepo.GetByPk(any())).thenThrow(new SQLException()).thenReturn(managerSample);
		
		assertThrows(SQLException.class, () -> srs.RequestShift(shiftReqSample));
		
		verify(userRepo).GetByPk(1);
	}

	@Test()
	void testUpdateShiftRequestNegative() throws SQLException {
		srs = Mockito.spy(srs);
		
		ShiftRequest shiftReqSample = new ShiftRequest();
		shiftReqSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(2);
		
		User managerSample = new User() {};
		managerSample.setId(2);
		
		when(userRepo.GetByPk(1)).thenThrow(new SQLException());
		
		Exception exc = assertThrows(SQLException.class, () -> srs.UpdateShift(shiftReqSample));
		
		assertTrue(exc instanceof SQLException);
		
		verify(userRepo).GetByPk(1);
	}

	@Test
	void testDeleteShiftRequestNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(shiftReqRepo).Delete(1);
		assertThrows(SQLException.class, () -> srs.DeleteShift(1));
	}

}
