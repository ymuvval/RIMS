package service;

import java.sql.SQLException;

import model.*;

public interface Auth {
	public User Validate(String email, String password) throws SQLException;
}
