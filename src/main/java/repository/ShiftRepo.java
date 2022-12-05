package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnectionPool;
import model.Shift;
import model.ShiftType;

public class ShiftRepo implements IShiftRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_SHIFT = "SELECT * FROM `rims`.`shift` WHERE `id` = ?;";
	private static final String GET_EMP_SHIFT = "SELECT * FROM `rims`.`shift` WHERE `emp_id` = ?;";
	private static final String ADD_SHIFT= "INSERT INTO `rims`.`shift` (`user_id`, `type`) VALUES (?, ?);";
	private static final String UPDATE_SHIFT= "UPDATE `rims`.`shift` SET `type` = ? WHERE `user_id` = ?;";

	public Connection getConn() {
		return this.connpool.create();
	}
	
	@Override
	public Shift GetByPk(Integer id) throws SQLException {
		System.out.println(GET_SHIFT);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_SHIFT);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			Shift shift = new Shift() {};
			if (rs.next()) {
				shift.setId(rs.getInt("id"));
				shift.setEmpId(rs.getInt("empId"));
				shift.setShift(ShiftType.valueOf(rs.getString("name")));
			}
			return shift;
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
	public Shift Get(Integer empId) throws SQLException {
		System.out.println(GET_EMP_SHIFT);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_EMP_SHIFT);
			preparedStatement.setInt(1, empId);
			ResultSet rs = preparedStatement.executeQuery();
			Shift shift = new Shift() {};
			if (rs.next()) {
				shift.setId(rs.getInt("id"));
				shift.setEmpId(rs.getInt("empId"));
				shift.setShift(ShiftType.valueOf(rs.getString("name")));
			}
			return shift;
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
	public void Add(Shift shift) throws SQLException {
		System.out.println(ADD_SHIFT);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(ADD_SHIFT);
			preparedStatement.setInt(1, shift.getEmpId());
			preparedStatement.setString(2, shift.getType().name());
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
	public void Update(Shift shift) throws SQLException {
		System.out.println(UPDATE_SHIFT);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_SHIFT);
			preparedStatement.setString(1, shift.getType().name());
			preparedStatement.setInt(2, shift.getEmpId());
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
