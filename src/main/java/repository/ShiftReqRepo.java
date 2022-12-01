package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnectionPool;
import model.ReqStatus;
import model.ShiftRequest;
import model.ShiftType;

public class ShiftReqRepo implements IShiftReqRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_SHIFT_REQ = "SELECT * FROM `rims`.`shift_req` WHERE `id` = ?;";
	private static final String ADD_SHIFT_REQ = "INSERT INTO `rims`.`shift_req` (`emp_id`, `new_shift`, `status`, `manager_id`) VALUES (?, ?, ?, ?);";
	private static final String UPDATE_SHIFT_REQ = "UPDATE `rims`.`shift_req` SET `new_shift` = ? WHERE `id` = ?;";
	private static final String UPDATE_SHIFT_REQ_STATUS = "UPDATE `rims`.`shift_req` SET `status` = ? WHERE `id` = ?;";
	private static final String DELETE_SHIFT_REQ = "DELETE FROM `rims`.`shift_req` WHERE `id` = ?;";

	@Override
	public ShiftRequest Get(Integer id) throws SQLException {
		System.out.println(GET_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(GET_SHIFT_REQ);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				ShiftRequest shiftRequest = new ShiftRequest() {};
				System.out.println("Email -- " + rs.getString("email"));
				shiftRequest.setEmpId(rs.getInt("emp_id"));
				shiftRequest.setManagerId(rs.getInt("manager_id"));
				shiftRequest.setStatus(ReqStatus.valueOf(rs.getString("status")));
				shiftRequest.setNewShift(ShiftType.valueOf(rs.getString("new_shift")));
				return shiftRequest;
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
	public void Add(ShiftRequest sr) throws SQLException {
		System.out.println(ADD_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_SHIFT_REQ);
			preparedStatement.setInt(1, sr.getEmpId());
			preparedStatement.setString(2, sr.getNewShift().name());
			preparedStatement.setString(3, sr.getStatus().name());
			preparedStatement.setInt(4, sr.getManagerId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}		
	}

	@Override
	public void Update(ShiftRequest sr) throws SQLException {
		System.out.println(UPDATE_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_SHIFT_REQ);
			preparedStatement.setString(1, sr.getNewShift().name());
			preparedStatement.setInt(2, sr.getId());
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
	public void UpdateStatus(ShiftRequest sr) throws SQLException {
		System.out.println(UPDATE_SHIFT_REQ_STATUS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_SHIFT_REQ_STATUS);
			preparedStatement.setString(1, sr.getNewShift().name());
			preparedStatement.setInt(2, sr.getId());
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
		System.out.println(DELETE_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_SHIFT_REQ);
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
