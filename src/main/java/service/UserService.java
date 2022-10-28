package service;

import model.User;
import db.DBConnectionPool;
import java.sql.*;

public class UserService extends User implements SignUp, Auth, FeedBack, ForgotPassword, Leave {
	private DBConnectionPool connpool;

	private static final String CREATE_USER = "INSERT INTO `rims`.`users`"
			+ "(`name`, `email`, `password`) VALUES (?, ?, ?)";

	public UserService(DBConnectionPool connpool) {
		super();
		this.connpool = connpool;
	}

	public void CreateUser(User user) {
		System.out.println(CREATE_USER);
		Connection dbConn = null;
		try {
			dbConn = connpool.create();
			PreparedStatement preparedStatement = dbConn.prepareStatement(CREATE_USER);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connpool.dead(dbConn);
		}
	}

	public void Validate(String name, String password) {
	}

	public void IsUserPresent(String name) {
	}

	public void SendFeedBack(Integer from_id, Integer to_id, String feedback_msg) {
	}

	public void SetPassword(String name, String new_password) {
	}

	public void RequestLeave() {
	}

}
