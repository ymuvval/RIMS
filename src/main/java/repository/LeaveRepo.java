package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnectionPool;
import model.Leave;
import model.ReqStatus;

public class LeaveRepo implements ILeaveRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_LEAVE = "SELECT * FROM `rims`.`leave` WHERE `id` = ?;";
	private static final String ADD_LEAVE = "INSERT INTO `rims`.`leave` (`emp_id`, `manager_id`, `start`, `end`, `status`) VALUES (?, ?, ?, ?, ?);";
	private static final String UPDATE_LEAVE = "UPDATE `rims`.`leave` SET `start` = ?, `end` = ? WHERE `id` = ?;";
	private static final String UPDATE_LEAVE_STATUS = "UPDATE `rims`.`leave` SET `status` = ? WHERE `id` = ?;";
	private static final String DELETE_LEAVE = "DELETE FROM `rims`.`leave` WHERE `id` = ?;";

	@Override
	public Leave Get(Integer id) throws SQLException {
		System.out.println(GET_LEAVE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(GET_LEAVE);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				Leave leave = new Leave() {};
				System.out.println("Email -- " + rs.getString("email"));
				leave.setEmpId(rs.getInt("emp_id"));
				leave.setManagerId(rs.getInt("manager_id"));
				leave.setStartDate(rs.getDate("start"));
				leave.setEndDate(rs.getDate("end"));
				leave.setStatus(ReqStatus.valueOf(rs.getString("status")));
				return leave;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}
	
	@Override
	public void Add(Leave leave) throws SQLException {
		System.out.println(ADD_LEAVE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_LEAVE);
			preparedStatement.setInt(1, leave.getEmpId());
			preparedStatement.setInt(2, leave.getManagerId());
			preparedStatement.setDate(3, leave.getStartDate());
			preparedStatement.setDate(4, leave.getEndDate());
			preparedStatement.setString(5, leave.getStatus().name());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

	@Override
	public void Update(Leave leave) throws SQLException {
		System.out.println(UPDATE_LEAVE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_LEAVE);
			preparedStatement.setDate(1, leave.getStartDate());
			preparedStatement.setDate(2, leave.getEndDate());
			preparedStatement.setInt(3, leave.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return;
	}
	
	@Override
	public void UpdateStatus(Leave leave) throws SQLException {
		System.out.println(UPDATE_LEAVE_STATUS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_LEAVE_STATUS);
			preparedStatement.setString(1, leave.getStatus().name());
			preparedStatement.setInt(2, leave.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return;
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_LEAVE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_LEAVE);
			preparedStatement.setInt(1, id);
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}		
	}

}
