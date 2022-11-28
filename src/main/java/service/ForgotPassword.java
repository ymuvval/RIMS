package service;

import java.sql.SQLException;

public interface ForgotPassword {
	public void SetPassword(String name, String new_password) throws SQLException;
}
