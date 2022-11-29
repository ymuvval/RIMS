package service;

import java.sql.SQLException;

import Exception.UserAlreadyExist;
import model.User;

public interface SignUp {
	public User CreateUser(User user) throws SQLException, UserAlreadyExist;
}
