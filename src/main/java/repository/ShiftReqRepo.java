package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	private static final String LIST_EMP_SHIFTS = "SELECT * FROM `rims`.`shift_req` WHERE `emp_id` = ?;";
	private static final String LIST_MANAGER_SHIFTS = "SELECT * FROM `rims`.`shift_req` WHERE `manager_id` = ?;";

	public Connection getConn() {
		return this.connpool.create();
	}
	
	@Override
	public ShiftRequest Get(Integer id) throws SQLException {
		System.out.println(GET_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_SHIFT_REQ);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			ShiftRequest shiftRequest = new ShiftRequest() {};
			if (rs.next()) {
				shiftRequest.setId(rs.getInt("id"));
				shiftRequest.setEmpId(rs.getInt("emp_id"));
				shiftRequest.setManagerId(rs.getInt("manager_id"));
				shiftRequest.setStatus(ReqStatus.valueOf(rs.getString("status")));
				shiftRequest.setNewShift(ShiftType.valueOf(rs.getString("new_shift")));
			}
			return shiftRequest;
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
	public void Add(ShiftRequest sr) throws SQLException {
		System.out.println(ADD_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(ADD_SHIFT_REQ);
			preparedStatement.setInt(1, sr.getEmpId());
			preparedStatement.setString(2, sr.getNewShift().name());
			preparedStatement.setString(3, sr.getStatus().name());
			preparedStatement.setInt(4, sr.getManagerId());
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
	public void Update(ShiftRequest sr) throws SQLException {
		System.out.println(UPDATE_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_SHIFT_REQ);
			preparedStatement.setString(1, sr.getNewShift().name());
			preparedStatement.setInt(2, sr.getId());
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
	public void UpdateStatus(ShiftRequest sr) throws SQLException {
		System.out.println(UPDATE_SHIFT_REQ_STATUS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_SHIFT_REQ_STATUS);
			preparedStatement.setString(1, sr.getStatus().name());
			preparedStatement.setInt(2, sr.getId());
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
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_SHIFT_REQ);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(DELETE_SHIFT_REQ);
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
	public ArrayList<ShiftRequest> ListByEmp(Integer empId) throws SQLException {
		System.out.println(LIST_EMP_SHIFTS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(LIST_EMP_SHIFTS);
			preparedStatement.setInt(1, empId);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();
			while (rs.next()) {
				ShiftRequest shiftRequest = new ShiftRequest() {};
				shiftRequest.setId(rs.getInt("id"));
				shiftRequest.setEmpId(rs.getInt("emp_id"));
				shiftRequest.setNewShift(ShiftType.valueOf(rs.getString("new_shift")));
				shiftRequest.setStatus(ReqStatus.valueOf(rs.getString("status")));
				shiftRequest.setManagerId(rs.getInt("manager_id"));
				shiftRequests.add(shiftRequest);
			}
			return shiftRequests;
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
	public ArrayList<ShiftRequest> ListByManager(Integer managerId) throws SQLException {
		System.out.println(LIST_MANAGER_SHIFTS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(LIST_MANAGER_SHIFTS);
			preparedStatement.setInt(1, managerId);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<ShiftRequest> shiftRequests = new ArrayList<ShiftRequest>();
			while (rs.next()) {
				ShiftRequest shiftRequest = new ShiftRequest() {};
				shiftRequest.setId(rs.getInt("id"));
				shiftRequest.setEmpId(rs.getInt("emp_id"));
				shiftRequest.setNewShift(ShiftType.valueOf(rs.getString("new_shift")));
				shiftRequest.setStatus(ReqStatus.valueOf(rs.getString("status")));
				shiftRequest.setManagerId(rs.getInt("manager_id"));
				shiftRequests.add(shiftRequest);
			}
			return shiftRequests;
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
