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
import model.Item;
import repository.ItemRepo;

@ExtendWith(MockitoExtension.class)
class TestItemRepo {
	
	@Mock Connection conn;
	@Mock PreparedStatement statement;
	@Mock DBConnectionPool connpool;
	@InjectMocks ItemRepo itemRepo;
	@Mock ResultSet resultSet;
	
	private static final String GET_ITEM = "SELECT * FROM `rims`.`item` WHERE `id` = ?;";
	private static final String ADD_ITEM = "INSERT INTO `rims`.`item` (`name`, `category_id`, `quantity`, `price`, `expiry`) VALUES (?, ?, ?, ?, ?);";
	private static final String UPDATE_ITEM = "UPDATE `rims`.`item` SET `name` = ?, `category_id` = ?, `quantity` = ?, `price` = ?, `expiry` = ? WHERE `id` = ?;";
	private static final String DELETE_ITEM = "DELETE FROM `rims`.`item` WHERE `id` = ?;";
	private static final String LIST_ITEM = "SELECT * FROM `rims`.`item` ORDER BY `id`;";
	private static final String DELETE_ALL = "DELETE FROM `rims`.`item`;";
	
	@BeforeEach
	public void beforeEach() throws SQLException {
		conn = Mockito.spy(conn);
		connpool = Mockito.spy(connpool);
		itemRepo = Mockito.spy(itemRepo);
		when(itemRepo.getConn()).thenReturn(conn);
		when(conn.prepareStatement(any())).thenReturn(statement);
		for (int i = 0; i < 3; i++) {
			Item item = new Item() {};
			item.setCategoryId(i);
			item.setExpiry(null);
			item.setId(i);
			item.setName("item" + (i + 1));
			item.setPrice(100);
			item.setQuantity(10);
			
			PreparedStatement preparedStatement = conn.prepareStatement(ADD_ITEM);
			preparedStatement.setString(1, item.getName());
			preparedStatement.setInt(2, item.getCategoryId());
			preparedStatement.setInt(3, item.getQuantity());
			preparedStatement.setDouble(4, item.getPrice());
			preparedStatement.setDate(5, item.getExpiry());
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
		itemRepo.Delete(1);
		verify(conn).prepareStatement(DELETE_ITEM);
		PreparedStatement preparedStatement = conn.prepareStatement(GET_ITEM);
		preparedStatement.setInt(1, 1);
		ResultSet rs = preparedStatement.executeQuery();
		assertEquals(null, rs);
	}
	
	@Test
	void testDeleteNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(DELETE_ITEM);
		assertThrows(SQLException.class, () -> itemRepo.Delete(1));
		verify(conn).prepareStatement(DELETE_ITEM);
	}
	
	@Test
	void testAddPositive() throws SQLException {
		Item item = new Item();
		item.setCategoryId(4);
		item.setExpiry(null);
		item.setId(4);
		item.setName("item5");
		item.setPrice(100);
		item.setQuantity(10);
		itemRepo.Add(item);
		
		verify(conn, times(4)).prepareStatement(ADD_ITEM);
	}
	
	@Test
	void testAddNegative() throws SQLException {
		Item item = new Item();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(ADD_ITEM);
		assertThrows(SQLException.class, () -> itemRepo.Add(item));
		verify(conn, times(4)).prepareStatement(ADD_ITEM);
	}
	
	
	@Test
	void testUpdatePositive() throws SQLException {
		Item item = new Item();
		item.setCategoryId(3);
		item.setExpiry(null);
		item.setId(3);
		item.setName("item5updated");
		item.setPrice(100);
		item.setQuantity(10);
		
		itemRepo.Update(item);
		
		verify(conn, times(1)).prepareStatement(UPDATE_ITEM);
	}
	
	@Test
	void testUpdateNegative() throws SQLException {
		Item item = new Item();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_ITEM);
		assertThrows(SQLException.class, () -> itemRepo.Update(item));
		verify(conn, times(1)).prepareStatement(UPDATE_ITEM);
	}
	
	@Test
	void testGetPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("category_id")).thenReturn(1);
		when(conn.prepareStatement(GET_ITEM)).thenReturn(statement);

		when(statement.executeQuery()).thenReturn(resultSet);

		Item i = itemRepo.Get(1);
		
		System.out.print(i);
		
		assertEquals(1, i.getCategoryId());

		verify(conn, times(1)).prepareStatement(GET_ITEM);
	}
	
	@Test
	void testGetNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(GET_ITEM);
		assertThrows(SQLException.class, () -> itemRepo.Get(1));
		verify(conn, times(1)).prepareStatement(GET_ITEM);
	}
	
	@Test
	void testListPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getString("name")).thenReturn("item");
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("category_id")).thenReturn(1);
		when(resultSet.getInt("quantity")).thenReturn(1);
		when(resultSet.getDouble("price")).thenReturn((double) 1);
		when(resultSet.getDate("expiry")).thenReturn(null);
		when(conn.prepareStatement(LIST_ITEM)).thenReturn(statement);
		when(statement.executeQuery()).thenReturn(resultSet);
		
		ArrayList<Item> items = itemRepo.List();
		assertEquals(1, items.size());

		verify(conn, times(1)).prepareStatement(LIST_ITEM);
		verify(statement, times(1)).executeQuery();
	}
	
	@Test
	void testListNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(LIST_ITEM);
		assertThrows(SQLException.class, () -> itemRepo.List());
		verify(conn, times(1)).prepareStatement(LIST_ITEM);
	}
}
