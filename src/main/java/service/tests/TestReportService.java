package service.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Clock;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Item;
import model.ReportType;
import repository.ItemRepo;
import service.ExpiryReportFactory;
import service.ReportService;

@ExtendWith(MockitoExtension.class)
class TestReportService {
	
	@Mock ItemRepo itemRepo;
	
	@InjectMocks ReportService reportService;
	
	static ArrayList<Item> items;
	
	@BeforeAll
	static void beforeAll() {
		items = new ArrayList<Item>();
		for (int i = 0; i < 5; i++) {
			Item item = new Item("name", (Integer)1, (Integer)4, 10.0, new Date(Clock.systemUTC().millis()));
			items.add(item);
		}
	}

	@Test
	void testReportPositive() throws SQLException {
		ExpiryReportFactory erf = new ExpiryReportFactory(itemRepo);
		when(erf.GenerateReport()).thenReturn(items);

		ArrayList<Item> actual = reportService.GetReport(ReportType.STOCK);

		assertEquals(items, actual);

	}

	@Test
	void testReportNegative() throws SQLException {

		assertThrows(IllegalArgumentException.class, () -> reportService.GetReport(ReportType.UNKNOWN));

	}
	
	@Test
	void testReportSQLException() throws SQLException {
		ExpiryReportFactory erf = new ExpiryReportFactory(itemRepo);
		when(erf.GenerateReport()).thenThrow(new SQLException());
		
		assertThrows(SQLException.class, () -> reportService.GetReport(ReportType.STOCK));

	}
	

}
