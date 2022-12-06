package service.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import Exception.UserAlreadyExist;
import model.Employee;
import model.ShiftType;
import model.User;
import repository.ManagerRepo;
import repository.UserRepo;
import service.ManagerService;

@ExtendWith(MockitoExtension.class)
class TestManagerService {
	
	@Mock ManagerRepo managerRepo;
	@Mock UserRepo userRepo;
	
	@InjectMocks ManagerService managerService;
	
	Employee emp;

	@Test
	void testAddEmployee() throws SQLException {
		emp = new Employee() {};
		emp.setEmail("a@b.com");
		when(managerRepo.Add(emp)).thenReturn(emp);
		
		User actual = managerService.AddEmployee(emp);
		
		Assert.assertEquals(emp, actual);
		verify(managerRepo).Add(emp);
	}

	@Test
	void testDeleteEmployee() throws SQLException {
		managerService.DeleteEmployee(1);
		verify(managerRepo).Delete(1);
	}

	@Test
	void testUpdateEmployee() throws SQLException {
		managerService.UpdateEmployee(1, "new_name", ShiftType.valueOf("MORNING"));
		verify(managerRepo).Update(1, "new_name", ShiftType.valueOf("MORNING"));
	}

	@Test
	void testCreateUser() throws SQLException, UserAlreadyExist {
		User user = new User() {};
		user.setEmail("a@b.com");
		user.setName("a");
		managerService = Mockito.spy(managerService);
		when(managerService.IsUserPresent("a@b.com")).thenReturn(false);
		when(managerRepo.Create(user)).thenReturn(user);
		
		User actual = managerService.CreateUser(user);
		
		verify(managerService).IsUserPresent("a@b.com");
		verify(managerRepo).Create(user);
		
		assertEquals(user, actual);
	}
	
	@Test
	void testAddEmployeeNegative() throws SQLException {
		emp = new Employee() {};
		emp.setEmail("a@b.com");
		when(managerRepo.Add(emp)).thenThrow(new SQLException());
	
		assertThrows(SQLException.class, () -> managerService.AddEmployee(emp));
	
		verify(managerRepo).Add(emp);
	}

	@Test
	void testDeleteEmployeeNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(managerRepo).Delete(1);
		assertThrows(SQLException.class, () -> managerService.DeleteEmployee(1));
	}

	@Test
	void testUpdateEmployeeNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(managerRepo).Update(1, "new_name", ShiftType.valueOf("MORNING"));
		assertThrows(SQLException.class, () -> managerService.UpdateEmployee(1, "new_name", ShiftType.valueOf("MORNING")));
	}

	@Test
	void testCreateUserNegative() throws SQLException, UserAlreadyExist {
		User user = new User() {};
		user.setEmail("a@b.com");
		managerService = Mockito.spy(managerService);
		when(managerService.IsUserPresent("a@b.com")).thenReturn(true);
		
		assertThrows(UserAlreadyExist.class, () -> managerService.CreateUser(user));
		
		verify(managerService, times(1)).IsUserPresent("a@b.com");
	}

}
