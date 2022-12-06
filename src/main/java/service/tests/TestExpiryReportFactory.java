package service.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Clock;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Item;
import repository.ItemRepo;
import service.ExpiryReportFactory;

@ExtendWith(MockitoExtension.class)
class TestExpiryReportFactory {

	@Mock ItemRepo itemRepo;
	@Mock Clock clock;
	
	@InjectMocks ExpiryReportFactory erf;
	
	static ArrayList<Item> items;
	static ArrayList<Item> expiring;
	static Date date;
	
	@BeforeAll
	static void beforeAll() throws ParseException {
		date = new Date(Clock.systemUTC().millis());

		items = new ArrayList<Item>();
		expiring = new ArrayList<Item>();
		for (int i = 0; i < 5; i++) {
			Item item = new Item("name", (Integer)1, (Integer)14, 10.0, Date.valueOf(date.toLocalDate().plusDays(10)));
			items.add(item);
			expiring.add(item);
		}
		for (int i = 0; i < 5; i++) {
			Item item = new Item("name", (Integer)1, (Integer)14, 10.0, Date.valueOf(date.toLocalDate().plusDays(20)));
			items.add(item);
		}
	}
	
	@Test
	void testGenerateReportPositive() throws SQLException {
		when(itemRepo.ListWithCategory()).thenReturn(items);
		
		ArrayList<Item> actual = erf.GenerateReport();
		
		verify(itemRepo).ListWithCategory();
		assertEquals(expiring.size(), actual.size());
	}
	
	@Test
	void testGenerateReportNegative() throws SQLException {
		when(itemRepo.ListWithCategory()).thenThrow(new SQLException());
		
		assertThrows(SQLException.class, () -> erf.GenerateReport());
		
		verify(itemRepo).ListWithCategory();
	}

}
