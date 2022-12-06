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
import model.ReqStatus;
import model.ShiftRequest;
import model.ShiftType;
import repository.ShiftReqRepo;

@ExtendWith(MockitoExtension.class)
class TestShiftReqRepo {
	
	@Mock Connection conn;
	@Mock PreparedStatement statement;
	@Mock DBConnectionPool connpool;
	@InjectMocks ShiftReqRepo shiftReqRepo;
	@Mock ResultSet resultSet;
	
	private static final String GET_SHIFT_REQ = "SELECT * FROM `rims`.`shift_req` WHERE `id` = ?;";
	private static final String ADD_SHIFT_REQ = "INSERT INTO `rims`.`shift_req` (`emp_id`, `new_shift`, `status`, `manager_id`) VALUES (?, ?, ?, ?);";
	private static final String UPDATE_SHIFT_REQ = "UPDATE `rims`.`shift_req` SET `new_shift` = ? WHERE `id` = ?;";
	private static final String UPDATE_SHIFT_REQ_STATUS = "UPDATE `rims`.`shift_req` SET `status` = ? WHERE `id` = ?;";
	private static final String DELETE_SHIFT_REQ = "DELETE FROM `rims`.`shift_req` WHERE `id` = ?;";
	private static final String DELETE_ALL = "DELETE FROM `rims`.`shift_req`;";


	@BeforeEach
	public void beforeEach() throws SQLException {
		conn = Mockito.spy(conn);
		connpool = Mockito.spy(connpool);
		shiftReqRepo = Mockito.spy(shiftReqRepo);
		when(shiftReqRepo.getConn()).thenReturn(conn);
		when(conn.prepareStatement(any())).thenReturn(statement);
		for (int i = 0; i < 3; i++) {
			ShiftRequest shiftRequest = new ShiftRequest() {};
			shiftRequest.setEmpId(i);
			shiftRequest.setId(i);
			shiftRequest.setManagerId(i);
			shiftRequest.setNewShift(ShiftType.AFTERNOON);
			shiftRequest.setStatus(ReqStatus.APPLIED);
			
			PreparedStatement preparedStatement = conn.prepareStatement(ADD_SHIFT_REQ);
			preparedStatement.setInt(1, shiftRequest.getEmpId());
			preparedStatement.setString(2, shiftRequest.getNewShift().toString());
			preparedStatement.setString(3, shiftRequest.getStatus().toString());
			preparedStatement.setInt(4, shiftRequest.getManagerId());
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
		shiftReqRepo.Delete(1);
		verify(conn).prepareStatement(DELETE_SHIFT_REQ);
		PreparedStatement preparedStatement = conn.prepareStatement(GET_SHIFT_REQ);
		preparedStatement.setInt(1, 1);
		ResultSet rs = preparedStatement.executeQuery();
		assertEquals(null, rs);
	}
	
	@Test
	void testDeleteNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(DELETE_SHIFT_REQ);
		assertThrows(SQLException.class, () -> shiftReqRepo.Delete(1));
		verify(conn).prepareStatement(DELETE_SHIFT_REQ);
	}
	
	@Test
	void testAddPositive() throws SQLException {
		ShiftRequest shiftRequest = new ShiftRequest();
		shiftRequest.setEmpId(4);
		shiftRequest.setId(4);
		shiftRequest.setManagerId(4);
		shiftRequest.setNewShift(ShiftType.AFTERNOON);
		shiftRequest.setStatus(ReqStatus.APPLIED);
		
		shiftReqRepo.Add(shiftRequest);
		
		verify(conn, times(4)).prepareStatement(ADD_SHIFT_REQ);
	}
	
	@Test
	void testAddNegative() throws SQLException {
		ShiftRequest shiftRequest = new ShiftRequest();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(ADD_SHIFT_REQ);
		assertThrows(SQLException.class, () -> shiftReqRepo.Add(shiftRequest));
		verify(conn, times(4)).prepareStatement(ADD_SHIFT_REQ);
	}
	
	
	@Test
	void testUpdatePositive() throws SQLException {
		ShiftRequest shiftRequest = new ShiftRequest();
		shiftRequest.setEmpId(2);
		shiftRequest.setId(2);
		shiftRequest.setManagerId(2);
		shiftRequest.setNewShift(ShiftType.EVENING);
		shiftRequest.setStatus(ReqStatus.APPLIED);
		
		shiftReqRepo.Update(shiftRequest);
		
		verify(conn, times(1)).prepareStatement(UPDATE_SHIFT_REQ);
	}
	
	@Test
	void testUpdateNegative() throws SQLException {
		ShiftRequest shiftRequest = new ShiftRequest();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_SHIFT_REQ);
		assertThrows(SQLException.class, () -> shiftReqRepo.Update(shiftRequest));
		verify(conn, times(1)).prepareStatement(UPDATE_SHIFT_REQ);
	}
	
	
	@Test
	void testUpdateStatusPositive() throws SQLException {
		ShiftRequest shiftRequest = new ShiftRequest();
		shiftRequest.setEmpId(2);
		shiftRequest.setId(2);
		shiftRequest.setManagerId(2);
		shiftRequest.setNewShift(ShiftType.EVENING);
		shiftRequest.setStatus(ReqStatus.APPLIED);
		
		shiftReqRepo.UpdateStatus(shiftRequest);
		
		verify(conn, times(1)).prepareStatement(UPDATE_SHIFT_REQ_STATUS);
	}
	
	@Test
	void testUpdateStatusNegative() throws SQLException {
		ShiftRequest shiftRequest = new ShiftRequest();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_SHIFT_REQ_STATUS);
		assertThrows(SQLException.class, () -> shiftReqRepo.UpdateStatus(shiftRequest));
		verify(conn, times(1)).prepareStatement(UPDATE_SHIFT_REQ_STATUS);
	}
	
	@Test
	void testGetPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("emp_id")).thenReturn(1);
		when(resultSet.getString("new_shift")).thenReturn("MORNING");
		when(resultSet.getString("status")).thenReturn("APPLIED");
		when(conn.prepareStatement(GET_SHIFT_REQ)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);

		ShiftRequest shiftReq = shiftReqRepo.Get(1);
		assertEquals(1, shiftReq.getEmpId());

		verify(conn, times(1)).prepareStatement(GET_SHIFT_REQ);
	}
	
	@Test
	void testGetNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(GET_SHIFT_REQ);
		assertThrows(SQLException.class, () -> shiftReqRepo.Get(1));
		verify(conn, times(1)).prepareStatement(GET_SHIFT_REQ);
	}
}
