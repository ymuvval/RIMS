package service;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Invoice;
import model.Order;
import model.OrderItem;
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
	
	public OrderService(OrderRepo or, OrderItemRepo oir, InvoiceService is) {
		super();
		this.setInvoiceRepo(or);
		this.setOrderItemRepo(oir);
		this.setInvoiceService(is);
	}

	public void setInvoiceRepo(OrderRepo orderRepo) {
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
			if (updatedOrder.getInvoiceId() == null) {
				Invoice invoice = this.invoiceService.GenerateInvoice(updatedOrder);
				updatedOrder.setInvoiceId(invoice.getId());
				this.orderRepo.Update(updatedOrder);
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
