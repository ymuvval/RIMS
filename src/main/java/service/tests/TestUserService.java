package service.tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.User;
import repository.UserRepo;
import service.UserService;


@ExtendWith(MockitoExtension.class)
class TestUserService {
	
	@Mock UserRepo userRepo;
	
	@InjectMocks UserService userService;
	
	User user;

	@Test
	void testIsUserPresentPositive() throws SQLException {
		user = new User() {};
		user.setEmail("a@b.com");
		when(userRepo.Get("a@b.com")).thenReturn(user);
		Boolean actual = userService.IsUserPresent("a@b.com");
		Assert.assertTrue(actual);
	}
	
	@Test
	void testIsUserPresentNegative() throws SQLException {
		user = new User() {};
		when(userRepo.Get("a@b.com")).thenReturn(null);
		Boolean actual = userService.IsUserPresent("a@b.com");
		Assert.assertFalse(actual);
	}
	
	@Test
	void testValidatePositive() throws SQLException {
		user = new User() {};
		user.setEmail("a@b.com");
		user.setPassword("pass");
		when(userRepo.GetWithPass("a@b.com")).thenReturn(user);
		User actual = userService.Validate("a@b.com", "pass");
		Assert.assertEquals(user, actual);
	}
	
	@Test
	void testValidateNegative() throws SQLException {
		user = new User() {};
		user.setEmail("a@b.com");
		user.setPassword("password");
		when(userRepo.GetWithPass("a@b.com")).thenReturn(user);
		User actual = userService.Validate("a@b.com", "pass");
		Assert.assertNotEquals(user, actual);
	}
	
	@Test()
	void testSetPasswordPositive() throws SQLException {
		userService.SetPassword("a@b.com", "pass");
		Mockito.verify(userRepo).UpdatePass("a@b.com", "pass");
	}
	
	@Test()
	void testSetPasswordNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(userRepo).UpdatePass("a@b.com", "pass");
		assertThrows(SQLException.class, () -> userService.SetPassword("a@b.com", "pass"));
	}

}
