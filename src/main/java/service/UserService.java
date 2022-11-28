package service;

import model.User;
import repository.*;
import java.sql.*;

public class UserService implements Auth, FeedBack, ForgotPassword {
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
	
	
	public Boolean IsUserPresent(String email) {
		User user = this.userRepo.Get(email);
		if (user != null && user.getEmail() == email) {
			return true;
		}
		return false;
	};

	public User Validate(String email, String password) {
		User user = this.userRepo.GetWithPass(email);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
//		System.out.println("user not found or some error");
		return null;
	}

	public void SendFeedBack(Integer from_id, Integer to_id, String feedback_msg) {
	}

	public void SetPassword(String email, String new_password) throws SQLException {
		this.userRepo.UpdatePass(email, new_password);
	}

//	@Override
//	public User Get(String email) {
//		return this.userRepo.Get(email);;
//	}

}
