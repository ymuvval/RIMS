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

import model.ShiftRequest;
import model.ShiftType;
import model.User;
import repository.ShiftReqRepo;
import repository.UserRepo;
import service.NotificationObserver;
import service.ShiftApprovalService;

@ExtendWith(MockitoExtension.class)
class TestShiftApprovalService {
	
	@Mock UserRepo userRepo;
	@Mock ShiftReqRepo shiftReqRepo;
	
	@InjectMocks ShiftApprovalService sas;

	@Test
	void testShiftApprovalServicePositive() throws SQLException {
		sas = Mockito.spy(sas);
		
		ShiftRequest shiftReqSample = new ShiftRequest();
		shiftReqSample.setEmpId(1);
		shiftReqSample.setNewShift(ShiftType.valueOf("NIGHT"));
		shiftReqSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(4);
		
		
		NotificationObserver observer = sas.makeShiftApprovalObserver();
		observer = spy(observer);
		when(sas.makeShiftApprovalObserver()).thenReturn(observer);
		when(shiftReqRepo.Get(1)).thenReturn(shiftReqSample);
		when(userRepo.GetByPk(1)).thenReturn(empSample);
		
		sas.ApproveShift(1, "APPROVED");
		
		verify(shiftReqRepo).UpdateStatus(shiftReqSample);
		verify(userRepo).GetByPk(1);
		verify(observer).setUser(empSample);
		verify(observer).Notify();
	}
	
	@Test
	void testShiftApprovalServiceNegative() throws SQLException {
		sas = Mockito.spy(sas);
		
		ShiftRequest shiftReqSample = new ShiftRequest();
		shiftReqSample.setEmpId(1);
		
		User empSample = new User() {};
		empSample.setId(1);
		empSample.setManagerId(4);
		
		when(shiftReqRepo.Get(1)).thenThrow(new SQLException());
		
		assertThrows(SQLException.class, () -> sas.ApproveShift(1, "APPROVED"));
			
	}

}
