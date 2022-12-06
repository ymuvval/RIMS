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
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import db.DBConnectionPool;
import model.Category;
import repository.CategoryRepo;

@ExtendWith(MockitoExtension.class)
class TestCategoryRepo {

	@Mock
	Connection conn;
	@Mock
	PreparedStatement statement;
	@Mock
	ResultSet resultSet;
	@Mock
	DBConnectionPool connpool;
	@InjectMocks
	CategoryRepo categoryRepo;

	private static final String ADD_CATEGORY = "INSERT INTO `rims`.`category` (`type`) VALUES (?);";
	private static final String DELETE_ALL = "DELETE FROM `rims`.`category`;";
	private static final String GET_CATEGORY = "SELECT * FROM `rims`.`category` WHERE `id` = ?;";
//	private static final String LIST_CATEGORY = "SELECT * FROM `rims`.`category`;";

	@BeforeEach
	public void beforeEach() throws SQLException {
		conn = Mockito.spy(conn);
		connpool = Mockito.spy(connpool);
		categoryRepo = Mockito.spy(categoryRepo);
		when(categoryRepo.getConn()).thenReturn(conn);
		when(conn.prepareStatement(any())).thenReturn(statement);
		for (int i = 0; i < 3; i++) {
			Category category = new Category() {
			};
			category.setType("category" + (i + 1));
			PreparedStatement preparedStatement = conn.prepareStatement(ADD_CATEGORY);
			preparedStatement.setString(1, category.getType());
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
		String DELETE_CATEGORY = "DELETE FROM `rims`.`category` WHERE `id` = ?;";
		when(conn.prepareStatement(DELETE_CATEGORY)).thenReturn(statement);
		categoryRepo.Delete(1);
		verify(conn).prepareStatement(DELETE_CATEGORY);
		PreparedStatement preparedStatement = conn.prepareStatement(GET_CATEGORY);
		preparedStatement.setInt(1, 1);
		ResultSet rs = preparedStatement.executeQuery();
		verify(statement, times(1)).executeQuery();
		assertEquals(null, rs);
	}

	@Test
	void testDeleteNegative() throws SQLException {
		String DELETE_CATEGORY = "DELETE FROM `rims`.`category` WHERE `id` = ?;";
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(DELETE_CATEGORY);
		assertThrows(SQLException.class, () -> categoryRepo.Delete(1));

		verify(conn).prepareStatement(DELETE_CATEGORY);
	}

	@Test
	void testAddPositive() throws SQLException {
		String ADD_CATEGORY = "INSERT INTO `rims`.`category` (`type`) VALUES (?);";
		when(conn.prepareStatement(ADD_CATEGORY)).thenReturn(statement);
		Category category = new Category();
		category.setType("category4");

		categoryRepo.Add(category);

		verify(statement, times(4)).executeUpdate();
		verify(conn, times(4)).prepareStatement(ADD_CATEGORY);
	}

	@Test
	void testAddNegative() throws SQLException {
		String ADD_CATEGORY = "INSERT INTO `rims`.`category` (`type`) VALUES (?);";
		Category category = new Category();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(ADD_CATEGORY);
		assertThrows(SQLException.class, () -> categoryRepo.Add(category));

		verify(conn, times(4)).prepareStatement(ADD_CATEGORY);
	}

	@Test
	void testUpdatePositive() throws SQLException {
		String UPDATE_CATEGORY = "UPDATE `rims`.`category` SET `type` = ? WHERE `id` = ?;";
		when(conn.prepareStatement(UPDATE_CATEGORY)).thenReturn(statement);
		Category category = new Category();
		category.setType("category4updated");
		category.setId(4);

		categoryRepo.Update(category);

		verify(statement, times(4)).executeUpdate();
		verify(conn, times(1)).prepareStatement(UPDATE_CATEGORY);
	}

	@Test
	void testUpdateNegative() throws SQLException {
		String UPDATE_CATEGORY = "UPDATE `rims`.`category` SET `type` = ? WHERE `id` = ?;";
		Category category = new Category();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_CATEGORY);

		assertThrows(SQLException.class, () -> categoryRepo.Update(category));
		verify(conn, times(1)).prepareStatement(UPDATE_CATEGORY);
	}

	@Test
	void testListPositive() throws SQLException {
		String LIST_CATEGORY = "SELECT * FROM `rims`.`category`;";
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getString("type")).thenReturn("category1");
		when(resultSet.getInt("id")).thenReturn(1);
		when(conn.prepareStatement(LIST_CATEGORY)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);

		ArrayList<Category> categories = categoryRepo.List();
		assertEquals(1, categories.size());

		verify(conn, times(1)).prepareStatement(LIST_CATEGORY);
		verify(statement, times(1)).executeQuery();
	}

	@Test
	void testListNegative() throws SQLException {
		String LIST_CATEGORY = "SELECT * FROM `rims`.`category`;";
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(LIST_CATEGORY);

		assertThrows(SQLException.class, () -> categoryRepo.List());
		verify(conn, times(1)).prepareStatement(LIST_CATEGORY);
	}
}
