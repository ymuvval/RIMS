package service.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Item;
import repository.ItemRepo;
import service.StockReportFactory;

@ExtendWith(MockitoExtension.class)
class TestStockReportFactory {
	
	@Mock ItemRepo itemRepo;
	@InjectMocks StockReportFactory srf;
	
	static ArrayList<Item> items;
	static ArrayList<Item> lowstock;
	static Date date;
	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	@BeforeAll
	static void beforeAll() throws ParseException {
		java.util.Date fixedDateTime = DATE_FORMATTER.parse("2022-11-10");
		date = Mockito.mock(Date.class);
		Mockito.when(date.toLocalDate()).thenReturn(new Date(fixedDateTime.getTime()).toLocalDate());
        Mockito.when(date.getTime()).thenReturn(fixedDateTime.getTime());
		items = new ArrayList<Item>();
		lowstock = new ArrayList<Item>();
		for (int i = 0; i < 5; i++) {
			Item item = new Item("name", (Integer)1, (Integer)4, 10.0, Date.valueOf(date.toLocalDate().plusDays(10)));
			items.add(item);
			lowstock.add(item);
		}
		for (int i = 0; i < 5; i++) {
			Item item = new Item("name", (Integer)1, (Integer)14, 10.0, Date.valueOf(date.toLocalDate().plusDays(20)));
			items.add(item);
		}
	}

	@Test
	void testStockReportPositive() throws SQLException {
		when(itemRepo.ListWithCategory()).thenReturn(items);
		
		ArrayList<Item> actual = srf.GenerateReport();
		
		verify(itemRepo).ListWithCategory();
		assertEquals(lowstock.size(), actual.size());
	}
	
	@Test
	void testStockReportNegative() throws SQLException {
		when(itemRepo.ListWithCategory()).thenThrow(new SQLException());

		assertThrows(SQLException.class, () -> srf.GenerateReport());

		verify(itemRepo).ListWithCategory();
	}

}
