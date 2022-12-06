package service.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Invoice;
import model.Order;
import model.OrderItem;
import repository.OrderItemRepo;
import repository.OrderRepo;
import service.InvoiceService;
import service.OrderService;


@ExtendWith(MockitoExtension.class)
class TestOrderService {
	
	@Mock OrderRepo orderRepo;
	@Mock OrderItemRepo orderItemRepo;
	@Mock InvoiceService invoiceService;
	@Mock OrderService discountService;
	
	@InjectMocks OrderService orderService;
	
	static Order order;
	static ArrayList<OrderItem> orderItems;
	
	@BeforeEach
	void beforeEach() {
		order = new Order();
		order = Mockito.spy(order);
		order.setDiscount(10.0);
		order.setName("bulk order");
		order.setPhone("secretnumb");
		orderItems = new ArrayList<OrderItem>();
		for (int i = 0; i < 3; i++) {
			OrderItem orderItem = new OrderItem();
			orderItem.setItem_id(i + 1);
			orderItem.setPrice(10.0);
			orderItem.setQuantity(3);
			orderItems.add(orderItem);
		}
	}

	@Test
	void testAddOrderPositive() throws SQLException {
		when(orderRepo.Add(order)).thenReturn(order);
		when(order.getId()).thenReturn(1);
		ArrayList<OrderItem> expected = new ArrayList<OrderItem>(); 
		for (int i = 0; i < orderItems.size(); i++) {
			expected.add(orderItems.get(i).Clone());
			expected.get(i).setOrder_id(1);
		}
		
		orderService.AddOrder(order, orderItems);
		
		for (int i = 0; i < orderItems.size(); i++) {
			assertEquals(expected.get(i).getOrder_id(), orderItems.get(i).getOrder_id());
		}
		verify(orderRepo).Add(order);
		verify(order, times(orderItems.size())).getId();
		verify(orderItemRepo).AddBatch(orderItems);
	}

	@Test
	void testUpdateOrderGenerateInvoicePositive() throws SQLException {
		Order newOrder = order.Clone();
		newOrder = spy(newOrder);
		newOrder.setId(1);
		for (int i = 0; i < orderItems.size(); i++) {
			orderItems.get(i).setOrder_id(1);
		}
		Invoice invoice = new Invoice();
		invoice = spy(invoice);
		invoice.setId(1);
		
		when(invoiceService.GenerateInvoice(newOrder)).thenReturn(invoice);
		when(orderItemRepo.List(newOrder.getId())).thenReturn(orderItems);
		
		orderService.UpdateOrder(newOrder);
		
		verify(invoiceService).GenerateInvoice(newOrder);
		verify(orderRepo, times(1)).UpdateOrderWithInvoice(newOrder);
//		as we are setting 1 as invoice id in line 114
		assertEquals(1, newOrder.getInvoiceId());
	}
	
	@Test
	void testUpdateOrderUpdateInvoicePositive() throws SQLException {
		Order newOrder = order.Clone();
		newOrder = spy(newOrder);
		newOrder.setId(1);
		newOrder.setInvoiceId(1);
		for (int i = 0; i < orderItems.size(); i++) {
			orderItems.get(i).setOrder_id(1);
		}
		Invoice invoice = new Invoice();
		invoice = spy(invoice);
		invoice.setId(1);
		
		when(orderItemRepo.List(newOrder.getId())).thenReturn(orderItems);
		
		orderService.UpdateOrder(newOrder);
		
		verify(invoiceService).UpdateInvoice(newOrder);
		verify(orderRepo).Update(newOrder);
	}

	@Test
	void testDeleteOrderPositive() throws SQLException {
		orderService.DeleteOrder(1);
		verify(orderRepo).Delete(1);
	}
	
	@Test
	void testAddOrderNegative() throws SQLException {
		when(orderRepo.Add(order)).thenThrow(new SQLException());
		ArrayList<OrderItem> expected = new ArrayList<OrderItem>(); 
		for (int i = 0; i < orderItems.size(); i++) {
			expected.add(orderItems.get(i).Clone());
			expected.get(i).setOrder_id(1);
		}
		
		orderService.AddOrder(order, orderItems);
		
		verify(orderRepo).Add(order);
	}

	@Test
	void testUpdateOrderGenerateInvoiceNegative() throws SQLException {
		Order newOrder = order.Clone();
		newOrder = spy(newOrder);
		newOrder.setId(1);
		for (int i = 0; i < orderItems.size(); i++) {
			orderItems.get(i).setOrder_id(1);
		}
		Invoice invoice = new Invoice();
		invoice = spy(invoice);
		invoice.setId(1);
		
		Mockito.doThrow(SQLException.class).when(orderRepo).Update(newOrder);
		
		orderService.UpdateOrder(newOrder);
		
		verify(orderRepo, times(1)).Update(newOrder);
	}
	
	@Test
	void testUpdateOrderUpdateInvoiceNegative() throws SQLException {
		Order newOrder = order.Clone();
		newOrder = spy(newOrder);
		newOrder.setId(1);
		newOrder.setInvoiceId(1);
		for (int i = 0; i < orderItems.size(); i++) {
			orderItems.get(i).setOrder_id(1);
		}
		Invoice invoice = new Invoice();
		invoice = spy(invoice);
		invoice.setId(1);
		
		Mockito.doThrow(SQLException.class).when(orderRepo).Update(newOrder);
		
		orderService.UpdateOrder(newOrder);
		
		verify(orderRepo).Update(newOrder);
	}

	@Test
	void testDeleteOrderNegative() throws SQLException {
		Mockito.doThrow(SQLException.class).when(orderRepo).Delete(1);;
		orderService.DeleteOrder(1);
		verify(orderRepo).Delete(1);
	}

}
