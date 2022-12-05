package service;

import model.User;
import repository.*;
import java.sql.*;

public class UserService implements Auth, ForgotPassword {
	private UserRepo userRepo;
	
	public UserRepo getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public UserService() {
		super();
	}
	
	public UserService(UserRepo uR) {
		super();
		this.userRepo = uR;
	}
	
	
	public Boolean IsUserPresent(String email) throws SQLException {
		User user = this.userRepo.Get(email);
		if (user != null && user.getEmail() == email) {
			return true;
		}
		return false;
	};

	public User Validate(String email, String password) throws SQLException {
		User user = this.userRepo.GetWithPass(email);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}

	public void SetPassword(String email, String new_password) throws SQLException {
		this.userRepo.UpdatePass(email, new_password);
	}

}
