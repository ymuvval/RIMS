package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnectionPool;
import model.ShiftType;
import model.User;
import model.UserStatus;

public class UserRepo implements IUserRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_USER = "SELECT * FROM `rims`.`user` WHERE email = ?;";
	private static final String GET_USER_BY_PK = "SELECT * FROM `rims`.`user` WHERE id = ?;";
	private static final String UPDATE_USER = "UPDATE `rims`.`user` SET `name` = ?, `email` = ?, `password` = ?, `question` = ?, `answer` = ?, `status` = ?, `shift` = ? WHERE `id` = ?;";
	private static final String UPDATE_USER_NAME = "UPDATE `rims`.`user` SET `name` = ? WHERE `id` = ?;";
	private static final String UPDATE_USER_PASS = "UPDATE `rims`.`user` SET `password` = ? WHERE `email` = ?;";
	private static final String UPDATE_USER_SHIFT = "UPDATE `rims`.`user` SET `shift` = ? WHERE `id` = ?;";
	
	public UserRepo() {
		super();
	}
	
	public UserRepo(DBConnectionPool connpool) {
		super();
		this.connpool = connpool;
	}
	
	public Connection getConn() {
		return this.connpool.create();
	}
	
	
	@Override
	public User Get(String email) throws SQLException {
		System.out.println(GET_USER);
		Connection dbConn = null;
		try {
			dbConn = this.getConn();
			PreparedStatement preparedStatement = dbConn.prepareStatement(GET_USER);
			preparedStatement.setString(1, email);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			User user = new User() {};
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setManagerId(rs.getInt("manager_id"));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setStatus(UserStatus.valueOf(rs.getString("status")));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connpool.dead(dbConn);
		}
		return null;
	}
	
	@Override
	public User GetByPk(Integer id) throws SQLException {
		System.out.println(GET_USER_BY_PK);
		Connection dbConn = null;
		try {
			dbConn = this.getConn();
			PreparedStatement preparedStatement = dbConn.prepareStatement(GET_USER_BY_PK);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				User user = new User() {};
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setManagerId(rs.getInt("manager_id"));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setStatus(UserStatus.valueOf(rs.getString("status")));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			connpool.dead(dbConn);
		}
		return null;
	}
	
	public User GetWithPass(String email) throws SQLException {
		System.out.println(GET_USER);
		Connection dbConn = null;
		try {
			dbConn = this.getConn();
			PreparedStatement preparedStatement = dbConn.prepareStatement(GET_USER);
			preparedStatement.setString(1, email);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				User user = new User() {};
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setPassword(rs.getString("password"));
				user.setManagerId(rs.getInt("manager_id"));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setStatus(UserStatus.valueOf(rs.getString("status")));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			connpool.dead(dbConn);
		}
		return null;
	}
	
	public User GetWithPass(Integer id) throws SQLException {
		System.out.println(GET_USER_BY_PK);
		Connection dbConn = null;
		try {
			dbConn = this.getConn();
			PreparedStatement preparedStatement = dbConn.prepareStatement(GET_USER_BY_PK);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				User user = new User() {};
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setPassword(rs.getString("password"));
				user.setManagerId(rs.getInt("manager_id"));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setStatus(UserStatus.valueOf(rs.getString("status")));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public void Update(Integer id, String name, ShiftType shift) throws SQLException {
		System.out.println(UPDATE_USER_NAME);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_USER_NAME);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
		return;
	}
	
	@Override
	public void Update(User user) throws SQLException {
		System.out.println(UPDATE_USER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_USER);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getQuestion());
			preparedStatement.setString(5, user.getAnswer());
			preparedStatement.setString(6, user.getStatus().name());
			preparedStatement.setString(7, user.getShiftType().name());
			preparedStatement.setInt(8, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
		return;
	}

	@Override
	public void UpdatePass(String email, String pass) throws SQLException {
		System.out.println(UPDATE_USER_PASS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_USER_PASS);
			preparedStatement.setString(1, pass);
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
		return;
	}
	
	@Override
	public void UpdateShift(Integer id, ShiftType shift) throws SQLException {
		System.out.println(UPDATE_USER_SHIFT);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_USER_SHIFT);
			preparedStatement.setString(1, shift.name());
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
		return;
	}

}
