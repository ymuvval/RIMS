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
import model.Invoice;
import repository.InvoiceRepo;

@ExtendWith(MockitoExtension.class)
class TestInvoiceRepo {
	
	@Mock Connection conn;
	@Mock PreparedStatement statement;
	@Mock DBConnectionPool connpool;
	@InjectMocks InvoiceRepo invoiceRepo;
	@Mock ResultSet resultSet;
	
	private static final String ADD_INVOICE = "INSERT INTO `rims`.`invoice` (`bill_amount`, `discount`, `final_amount`) VALUES (?, ?, ?);";
	private static final String UPDATE_INVOICE = "UPDATE `rims`.`invoice` SET `bill_amount` = ?, `discount` = ?, `final_amount` = ? WHERE `id` = ?;";
	private static final String DELETE_INVOICE = "DELETE FROM `rims`.`invoice` WHERE `id` = ?;";
	private static final String DELETE_ALL = "DELETE FROM `rims`.`invoice`;";

	@BeforeEach
	public void beforeEach() throws SQLException {
		conn = Mockito.spy(conn);
		connpool = Mockito.spy(connpool);
		invoiceRepo = Mockito.spy(invoiceRepo);
		when(invoiceRepo.getConn()).thenReturn(conn);
		when(conn.prepareStatement(any())).thenReturn(statement);
	}
	
	@AfterEach
	public void afterEach() throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ALL);
		preparedStatement.executeUpdate();
	}
	
	@Test
	void testUpdatePositive() throws SQLException {
		Invoice invoice = new Invoice();
		invoice.setBill_amount(2.1);
		invoice.setDiscount(2.01);
		invoice.setFinal_bill(2.05);
		invoice.setId(1);
		
		invoiceRepo.Update(invoice);
		
		verify(conn, times(1)).prepareStatement(UPDATE_INVOICE);
	}
	
	@Test
	void testUpdateNegative() throws SQLException {
		Invoice invoice = new Invoice();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(UPDATE_INVOICE);
		assertThrows(SQLException.class, () -> invoiceRepo.Update(invoice));
		verify(conn, times(1)).prepareStatement(UPDATE_INVOICE);
	}
	
	@Test
	void testDeletePositive() throws SQLException {
		invoiceRepo.Delete(1);
		verify(conn).prepareStatement(DELETE_INVOICE);
	}
	
	@Test
	void testDeleteNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(DELETE_INVOICE);
		assertThrows(SQLException.class, () -> invoiceRepo.Delete(1));
		verify(conn).prepareStatement(DELETE_INVOICE);
	}
	
	@Test
	void testAddPositive() throws SQLException {
		resultSet = Mockito.spy(ResultSet.class);
		when(conn.prepareStatement(ADD_INVOICE, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(statement);
		when(statement.getGeneratedKeys()).thenReturn(resultSet);

		Invoice invoice = new Invoice();
		invoice.setBill_amount(1.0);
		invoice.setDiscount(1.0);
		invoice.setFinal_bill(1.0);
		invoice.setId(1);

		Invoice i = invoiceRepo.Add(invoice);
		assertEquals(i.getBill_amount(), invoice.getBill_amount());

		verify(conn, times(1)).prepareStatement(ADD_INVOICE, PreparedStatement.RETURN_GENERATED_KEYS);
	}
	
	@Test
	void testAddNegative() throws SQLException {
		Invoice invoice = new Invoice();
		Mockito.doThrow(SQLException.class).when(conn).prepareStatement(ADD_INVOICE, PreparedStatement.RETURN_GENERATED_KEYS);
		assertThrows(SQLException.class, () -> invoiceRepo.Add(invoice));
		verify(conn, times(1)).prepareStatement(ADD_INVOICE, PreparedStatement.RETURN_GENERATED_KEYS);
	}
}
