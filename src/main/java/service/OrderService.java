package service;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Invoice;
import model.Order;
import model.OrderItem;
import repository.InvoiceRepo;
import repository.OrderItemRepo;
import repository.OrderRepo;

public class OrderService {
	private OrderRepo orderRepo;
	private OrderItemRepo orderItemRepo;
	private InvoiceService invoiceService;
	private OrderService discountService;
	
	public OrderRepo getInvoiceRepo() {
		return orderRepo;
	}
	
	public OrderService() {
		super();
	}
	
	public OrderService(OrderRepo oR) {
		super();
		this.orderRepo = oR;
	}
	
	public OrderService(OrderRepo or, OrderItemRepo oir, InvoiceRepo ir, InvoiceService is) {
		super();
		this.orderRepo = or;
		this.setOrderItemRepo(oir);
		is.setInvoiceRepo(ir);
		this.setInvoiceService(is);
	}
	
	public OrderRepo getOrderRepo() {
		return orderRepo;
	}

	public void setOrderRepo(OrderRepo orderRepo) {
		this.orderRepo = orderRepo;
	}

	public OrderItemRepo getOrderItemRepo() {
		return orderItemRepo;
	}

	public void setOrderItemRepo(OrderItemRepo orderItemRepo) {
		this.orderItemRepo = orderItemRepo;
	}

	public InvoiceService getInvoiceService() {
		return invoiceService;
	}

	public void setInvoiceService(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

	public OrderService getDiscountService() {
		return discountService;
	}

	public void setDiscountService(OrderService discountService) {
		this.discountService = discountService;
	}
	
	public void AddOrder(Order order, ArrayList<OrderItem> orderItems) {
		try {
			this.orderRepo.Add(order);
			for (int i = 0; i < orderItems.size(); i++) {
				orderItems.get(i).setOrder_id(order.getId());
			}
			this.orderItemRepo.AddBatch(orderItems);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void UpdateOrder(Order updatedOrder) {
		try {
			this.orderRepo.Update(updatedOrder);
			ArrayList<OrderItem> orderItems = this.orderItemRepo.List(updatedOrder.getId());
			updatedOrder.setItems(orderItems);
			if (orderItems.size() == 0) {
				this.invoiceService.UpdateInvoice(updatedOrder);
				return;
			}
			System.out.println("invoice id " + updatedOrder.getInvoiceId());
			if (updatedOrder.getInvoiceId() == null || updatedOrder.getInvoiceId() <= 0) {
				Invoice invoice = this.invoiceService.GenerateInvoice(updatedOrder);
				updatedOrder.setInvoiceId(invoice.getId());
				this.orderRepo.UpdateOrderWithInvoice(updatedOrder);
			} else {
				this.invoiceService.UpdateInvoice(updatedOrder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteOrder(Integer id) {
		try {
			this.orderRepo.Delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
