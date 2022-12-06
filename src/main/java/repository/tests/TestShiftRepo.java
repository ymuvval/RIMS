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
import model.Shift;
import model.ShiftType;
import repository.ShiftRepo;

@ExtendWith(MockitoExtension.class)
class TestShiftRepo {
	
	@Mock Connection conn;
	@Mock PreparedStatement statement;
	@Mock DBConnectionPool connpool;
	@InjectMocks ShiftRepo shiftRepo;
	@Mock ResultSet resultSet;
	
	private static final String GET_SHIFT = "SELECT * FROM `rims`.`shift` WHERE `id` = ?;";
	private static final String GET_EMP_SHIFT = "SELECT * FROM `rims`.`shift` WHERE `emp_id` = ?;";
	private static final String ADD_SHIFT= "INSERT INTO `rims`.`shift` (`user_id`, `type`) VALUES (?, ?);";
	private static final String UPDATE_SHIFT= "UPDATE `rims`.`shift` SET `type` = ? WHERE `user_id` = ?;";
	private static final String DELETE_ALL = "DELETE FROM `rims`.`shift`;";

	@BeforeEach
	public void beforeEach() throws SQLException {
		conn = Mockito.spy(conn);
		connpool = Mockito.spy(connpool);
		shiftRepo = Mockito.spy(shiftRepo);
		when(shiftRepo.getConn()).thenReturn(conn);
		when(conn.prepareStatement(any())).thenReturn(statement);
		for (int i = 0; i < 3; i++) {
			Shift shift = new Shift() {};
			shift.setEmpId(i);
			shift.setId(1);
			shift.setShift(ShiftType.AFTERNOON);
			
			PreparedStatement preparedStatement = conn.prepareStatement(ADD_SHIFT);
			preparedStatement.setInt(1, shift.getId());
			preparedStatement.setString(2, shift.getType().toString());
			preparedStatement.executeUpdate();
		}
	}
	
	@AfterEach
	public void afterEach() throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ALL);
		preparedStatement.executeUpdate();
	}
	
	@Test
	void testAddPositive() throws SQLException {
		Shift shift = new Shift();
		shift.setEmpId(4);
		shift.setId(4);
		shift.setShift(ShiftType.AFTERNOON);
		
		shiftRepo.Add(shift);
		
		verify(conn, times(4)).prepareStatement(ADD_SHIFT);
	}
	
	@Test
	void testAddNegative() throws SQLException {
		Shift shift = new Shift();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(ADD_SHIFT);
		assertThrows(SQLException.class, () -> shiftRepo.Add(shift));
		verify(conn, times(4)).prepareStatement(ADD_SHIFT);
	}
	
	
	@Test
	void testUpdatePositive() throws SQLException {
		Shift shift = new Shift();
		shift.setEmpId(1);
		shift.setId(1);
		shift.setShift(ShiftType.EVENING);
		
		shiftRepo.Update(shift);
		
		verify(conn, times(1)).prepareStatement(UPDATE_SHIFT);
	}
	
	@Test
	void testUpdateNegative() throws SQLException {
		Shift shift = new Shift();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_SHIFT);
		assertThrows(SQLException.class, () -> shiftRepo.Update(shift));
		verify(conn, times(1)).prepareStatement(UPDATE_SHIFT);
	}
	
	@Test
	void testGetByPkPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getString("name")).thenReturn("MORNING");
		when(conn.prepareStatement(GET_SHIFT)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);

		Shift shift = shiftRepo.GetByPk(1);
		assertEquals(1, shift.getId());

		verify(conn, times(1)).prepareStatement(GET_SHIFT);
	}
	
	@Test
	void testGetByPkNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(GET_SHIFT);
		assertThrows(SQLException.class, () -> shiftRepo.GetByPk(1));
		verify(conn, times(1)).prepareStatement(GET_SHIFT);
	}
	
	@Test
	void testGetPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("empId")).thenReturn(1);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getString("name")).thenReturn("MORNING");
		when(conn.prepareStatement(GET_EMP_SHIFT)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);

		Shift shift = shiftRepo.Get(1);
		assertEquals(1, shift.getEmpId());

		verify(conn, times(1)).prepareStatement(GET_EMP_SHIFT);
	}
	
	@Test
	void testGetNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(GET_EMP_SHIFT);
		assertThrows(SQLException.class, () -> shiftRepo.Get(1));
		verify(conn, times(1)).prepareStatement(GET_EMP_SHIFT);
	}
}
