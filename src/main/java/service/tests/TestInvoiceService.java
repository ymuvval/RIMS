package service.tests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Invoice;
import model.Order;
import model.OrderItem;
import repository.InvoiceRepo;
import service.InvoiceService;


@ExtendWith(MockitoExtension.class)
class TestInvoiceService {
	
	@Mock
	static InvoiceRepo invoiceRepo;
	
	@InjectMocks
	static InvoiceService invoiceService;
	
	static Order inputOrder;
	static ArrayList<OrderItem> orderItems;
	static Invoice invoice;
	
	@BeforeAll
	static void beforeAll() {
		orderItems = new ArrayList<OrderItem>();
		invoice = Mockito.spy(Invoice.class);
		inputOrder = Mockito.spy(Order.class);
		invoiceService = new InvoiceService(invoiceRepo);
		invoiceService = Mockito.spy(invoiceService);
		inputOrder.setId(1);
		inputOrder.setDiscount(10.0);
		inputOrder.setInvoiceId(2);
		inputOrder.setName("cake");
		inputOrder.setPhone("8759345270");
		
		for (int i = 0; i < 5; i++) {
			OrderItem oitem = new OrderItem(1, 1, 15.0, 3);
			orderItems.add(oitem);
		}
		inputOrder.setItems(orderItems);
		invoice.setBill_amount(inputOrder.CalculateBill());
		invoice.setDiscount(inputOrder.getDiscount());
		invoice.setFinal_bill(invoice.getBill_amount() - invoice.getDiscount());
	}
	
	@Test
	void testGenerateInvoicePositive() throws SQLException {
		Invoice invoiceToReturn = invoice.Copy();
		invoiceToReturn.setId(1);
		when(invoiceService.makeInvoice()).thenReturn(invoice);
		when(invoiceRepo.Add(invoice)).thenReturn(invoiceToReturn);
		
		Invoice actual = invoiceService.GenerateInvoice(inputOrder);
		
		assertEquals(invoiceToReturn, actual);
		assertEquals(invoiceToReturn.getId(), actual.getId());
		
		verify(invoiceRepo).Add(invoice);
	}
	
	@Test
	void testUpdateInvoicePositive() throws SQLException {
		when(invoiceService.makeInvoice()).thenReturn(invoice);
		
		invoiceService.UpdateInvoice(inputOrder);
		
		verify(invoiceRepo).Update(invoice);
	}
	
	@Test
	void testDeleteInvoicePositive() throws SQLException {
		invoiceService.DeleteInvoice(1);
		verify(invoiceRepo).Delete(1);
	}
	
	@Test
	void testGenerateInvoiceNegative() throws SQLException {
		Invoice invoiceToReturn = invoice.Copy();
		invoiceToReturn.setId(1);
		when(invoiceService.makeInvoice()).thenReturn(invoice);
		when(invoiceRepo.Add(invoice)).thenThrow(new SQLException());
		
		Invoice actual = invoiceService.GenerateInvoice(inputOrder);
		
		assertNull(actual);
		verify(invoiceRepo).Add(invoice);
	}
	
	@Test
	void testUpdateInvoiceNegative() throws SQLException {
		when(invoiceService.makeInvoice()).thenReturn(invoice);
		Mockito.doThrow(SQLException.class).when(invoiceRepo).Update(invoice);
		
		invoiceService.UpdateInvoice(inputOrder);
		
		verify(invoiceRepo).Update(invoice);
	}
	
	@Test
	void testDeleteInvoiceNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(invoiceRepo).Delete(1);
		Exception exc = assertThrows(SQLException.class, () -> invoiceService.DeleteInvoice(1));
		assertTrue(exc instanceof SQLException);
		verify(invoiceRepo, times(1)).Delete(1);
	}

}
