package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Employee;
import model.ShiftType;
import model.User;

public class ManagerRepo extends UserRepo implements IManagerRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	
	private static final String CREATE_USER = "INSERT INTO `rims`.`user`"
			+ " (`name`, `email`, `password`, `role`, `question`, `answer`) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String ADD_EMP = "INSERT INTO `rims`.`user`"
			+ " (`name`, `email`, `password`, `role`, `manager_id`, `question`, `answer`, `shift`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String GET_USER = "SELECT * FROM `rims`.`user` WHERE email = ?;";
	private static final String GET_EMPLOYEES = "SELECT * FROM `rims`.`user` WHERE manager_id = ?;";
	private static final String GET_USER_BY_PK = "SELECT * FROM `rims`.`user` WHERE id = ?;";
	private static final String UPDATE_USER_NAME = "UPDATE `rims`.`user` SET `name` = ?, `shift` = ? WHERE `id` = ?;";
	private static final String UPDATE_USER_PASS = "UPDATE `rims`.`user` SET `pass` = ? WHERE `email` = ?;";
	private static final String DELETE_USER = "DELETE FROM `rims`.`user` WHERE `id` = ?;";
	
	public ManagerRepo() {
		super();
	}
	
	public ManagerRepo(DBConnectionPool connpool) {
		super();
		this.connpool = connpool;
	}
	
	public Connection getConn() {
		return this.connpool.create();
	}
	
	@Override
	public User Create(User user) throws SQLException {
		System.out.println(CREATE_USER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			user.setRole("MANAGER");
			preparedStatement = dbConn.prepareStatement(CREATE_USER);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getRole());
			preparedStatement.setString(5, user.getQuestion());
			preparedStatement.setString(6, user.getAnswer());
			preparedStatement.executeUpdate();
			return user;
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
			if (rs.next()) {
				User user = new User() {};
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setEmail(rs.getString("name"));
				user.setEmail(rs.getString("role"));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
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
			preparedStatement.setString(2, shift.name());
			preparedStatement.setInt(3, id);
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
	public User Add(Employee emp) throws SQLException {
		System.out.println(ADD_EMP);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			emp.setRole("EMPLOYEE");
			preparedStatement = dbConn.prepareStatement(ADD_EMP);
			preparedStatement.setString(1, emp.getName());
			preparedStatement.setString(2, emp.getEmail());
			preparedStatement.setString(3, emp.getPassword());
			preparedStatement.setString(4, emp.getRole());
			preparedStatement.setInt(5, emp.getManagerId());
			preparedStatement.setString(6, emp.getQuestion());
			preparedStatement.setString(7, emp.getAnswer());
			preparedStatement.setString(8, emp.getShiftType().name());
			preparedStatement.executeUpdate();
			return emp;
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
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_USER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(DELETE_USER);
			preparedStatement.setInt(1, id);
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
	}
	
	
	@Override
	public User GetByPk(Integer id) throws SQLException {
		System.out.println(GET_USER_BY_PK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_USER_BY_PK);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				User user = new User() {};
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
				return user;
			}
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
		return null;
	}
	
	@Override
	public ArrayList<User> GetEmployees(Integer id) throws SQLException {
		System.out.println(GET_EMPLOYEES);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_EMPLOYEES);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<User> emps = new ArrayList<User>();
			while (rs.next()) {
				User user = new User() {};
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setQuestion(rs.getString("question"));
				user.setAnswer(rs.getString("answer"));
				user.setShiftType(ShiftType.valueOf(rs.getString("shift")));
				emps.add(user);
			}
			return emps;
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
	}

}
