package repository.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import db.DBConnectionPool;
import model.Leave;
import model.ReqStatus;
import repository.LeaveRepo;

@ExtendWith(MockitoExtension.class)
class TestLeaveRepo {
	
	@Mock Connection conn;
	@Mock PreparedStatement statement;
	@Mock DBConnectionPool connpool;
	@InjectMocks LeaveRepo leaveRepo;
	@Mock ResultSet resultSet;
	
	private static final String GET_LEAVE = "SELECT * FROM `rims`.`leave` WHERE `id` = ?;";
	private static final String ADD_LEAVE = "INSERT INTO `rims`.`leave` (`emp_id`, `manager_id`, `start`, `end`, `status`) VALUES (?, ?, ?, ?, ?);";
	private static final String UPDATE_LEAVE = "UPDATE `rims`.`leave` SET `start` = ?, `end` = ? WHERE `id` = ?;";
	private static final String UPDATE_LEAVE_STATUS = "UPDATE `rims`.`leave` SET `status` = ? WHERE `id` = ?;";
	private static final String DELETE_LEAVE = "DELETE FROM `rims`.`leave` WHERE `id` = ?;";
	private static final String DELETE_ALL = "DELETE FROM `rims`.`leave`;";


	@BeforeEach
	public void beforeEach() throws SQLException {
		conn = Mockito.spy(conn);
		connpool = Mockito.spy(connpool);
		leaveRepo = Mockito.spy(leaveRepo);
		when(leaveRepo.getConn()).thenReturn(conn);
		when(conn.prepareStatement(any())).thenReturn(statement);
		for (int i = 0; i < 3; i++) {
			Leave leave = new Leave() {};
			leave.setEmpId(i);
			leave.setEndDate(null);
			leave.setId(i);
			leave.setManagerId(i);
			leave.setStartDate(null);
			leave.setStatus(ReqStatus.APPLIED);
			
			PreparedStatement preparedStatement = conn.prepareStatement(ADD_LEAVE);
			preparedStatement.setInt(1, leave.getEmpId());
			preparedStatement.setInt(2, leave.getManagerId());
			preparedStatement.setDate(3, leave.getStartDate());
			preparedStatement.setDate(4, leave.getEndDate());
			preparedStatement.setString(5, leave.getStatus().toString());

			preparedStatement.executeUpdate();
		}
	}
	
	@AfterEach
	public void afterEach() throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ALL);
		preparedStatement.executeUpdate();
	}
	
	@Test
	void testDeletePositive() throws SQLException {
		leaveRepo.Delete(1);
		verify(conn).prepareStatement(DELETE_LEAVE);
		PreparedStatement preparedStatement = conn.prepareStatement(GET_LEAVE);
		preparedStatement.setInt(1, 1);
		ResultSet rs = preparedStatement.executeQuery();
		assertEquals(null, rs);
	}
	
	@Test
	void testDeleteNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(DELETE_LEAVE);
		assertThrows(SQLException.class, () -> leaveRepo.Delete(1));
		verify(conn).prepareStatement(DELETE_LEAVE);
	}
	
	@Test
	void testAddPositive() throws SQLException {
		Leave leave = new Leave();
		leave.setEmpId(4);
		leave.setEndDate(null);
		leave.setId(4);
		leave.setManagerId(4);
		leave.setStartDate(null);
		leave.setStatus(ReqStatus.APPLIED);
		
		leaveRepo.Add(leave);
		
		verify(conn, times(4)).prepareStatement(ADD_LEAVE);
	}
	
	@Test
	void testAddNegative() throws SQLException {
		Leave leave = new Leave();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(ADD_LEAVE);
		assertThrows(SQLException.class, () -> leaveRepo.Add(leave));
		verify(conn, times(4)).prepareStatement(ADD_LEAVE);
	}
	
	
	@Test
	void testUpdatePositive() throws SQLException {
		Leave leave = new Leave();
		leave.setEmpId(4);
		leave.setEndDate(null);
		leave.setId(4);
		leave.setManagerId(4);
		leave.setStartDate(null);
		leave.setStatus(ReqStatus.APPLIED);
		
		leaveRepo.Update(leave);
		
		verify(conn, times(1)).prepareStatement(UPDATE_LEAVE);
	}
	
	@Test
	void testUpdateNegative() throws SQLException {
		Leave leave = new Leave();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_LEAVE);
		assertThrows(SQLException.class, () -> leaveRepo.Update(leave));
		verify(conn, times(1)).prepareStatement(UPDATE_LEAVE);
	}
	
	
	@Test
	void testUpdateStatusPositive() throws SQLException {
		Leave leave = new Leave();
		leave.setEmpId(4);
		leave.setEndDate(null);
		leave.setId(4);
		leave.setManagerId(4);
		leave.setStartDate(null);
		leave.setStatus(ReqStatus.REJECTED);
		
		leaveRepo.UpdateStatus(leave);
		
		verify(conn, times(1)).prepareStatement(UPDATE_LEAVE_STATUS);
	}
	
	@Test
	void testUpdateStatusNegative() throws SQLException {
		Leave leave = new Leave();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_LEAVE_STATUS);
		assertThrows(SQLException.class, () -> leaveRepo.UpdateStatus(leave));
		verify(conn, times(1)).prepareStatement(UPDATE_LEAVE_STATUS);
	}
	
	@Test
	void testGetPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("emp_id")).thenReturn(1);
		when(resultSet.getString("status")).thenReturn("APPLIED");
		when(conn.prepareStatement(GET_LEAVE)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);

		Leave i = leaveRepo.Get(1);
		assertEquals(1, i.getEmpId());

		verify(conn, times(1)).prepareStatement(GET_LEAVE);
	}
	
	@Test
	void testGetStatusNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(GET_LEAVE);
		assertThrows(SQLException.class, () -> leaveRepo.Get(1));
		verify(conn, times(1)).prepareStatement(GET_LEAVE);
	}
	
}
