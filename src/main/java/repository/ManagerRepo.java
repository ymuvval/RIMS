package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Employee;
import model.User;

public class ManagerRepo implements IManagerRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	
	private static final String CREATE_USER = "INSERT INTO `rims`.`user`"
			+ " (`name`, `email`, `password`, `role`) VALUES (?, ?, ?, ?)";
	
	private static final String GET_USER = "SELECT * FROM `rims`.`user` WHERE email = ?;";
	private static final String GET_USER_BY_PK = "SELECT * FROM `rims`.`user` WHERE id = ?;";
	private static final String UPDATE_USER_NAME = "UPDATE `rims`.`user` SET `name` = ? WHERE `id` = ?;";
	private static final String UPDATE_USER_PASS = "UPDATE `rims`.`user` SET `pass` = ? WHERE `email` = ?;";
	private static final String DELETE_USER = "DELETE FROM `rims`.`user` WHERE `id` = ?;";
	
	public ManagerRepo() {
		super();
	}
	
	public ManagerRepo(DBConnectionPool connpool) {
		super();
		this.connpool = connpool;
	}
	
	@Override
	public User Create(User user) throws SQLException {
		System.out.println(CREATE_USER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			user.setRole("MANAGER");
			preparedStatement = dbConn.prepareStatement(CREATE_USER);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getRole());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public User Get(String email) {
		System.out.println(GET_USER);
		Connection dbConn = null;
		try {
			dbConn = connpool.create();
			PreparedStatement preparedStatement = dbConn.prepareStatement(GET_USER);
			preparedStatement.setString(1, email);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				User user = new User() {};
				user.setEmail(rs.getString("email"));
				user.setEmail(rs.getString("name"));
				user.setEmail(rs.getString("role"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public void Update(Integer id, String name) throws SQLException {
		System.out.println(UPDATE_USER_NAME);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_USER_NAME);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
//				throw exception
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return;
	}

	@Override
	public void UpdatePass(String email, String pass) throws SQLException {
		System.out.println(UPDATE_USER_PASS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_USER_PASS);
			preparedStatement.setString(1, pass);
			preparedStatement.setString(2, email);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
//				throw exception
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return;
	}

	@Override
	public User Add(Employee emp) throws SQLException {
		System.out.println(CREATE_USER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			emp.setRole("EMPLOYEE");
			preparedStatement = dbConn.prepareStatement(CREATE_USER);
			preparedStatement.setString(1, emp.getName());
			preparedStatement.setString(2, emp.getEmail());
			preparedStatement.setString(3, emp.getPassword());
			preparedStatement.setString(4, emp.getRole());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
			return emp;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_USER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_USER);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

	@Override
	public void DeActivate(Integer id) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public ArrayList<User> List(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User GetByPk(Integer id) throws SQLException {
		System.out.println(GET_USER_BY_PK);
		Connection dbConn = null;
		try {
			dbConn = connpool.create();
			PreparedStatement preparedStatement = dbConn.prepareStatement(GET_USER_BY_PK);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				User user = new User() {};
				System.out.println("Email -- " + rs.getString("email"));
				user.setEmail(rs.getString("email"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connpool.dead(dbConn);
		}
		return null;
	}

}
