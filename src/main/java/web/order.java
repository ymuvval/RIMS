package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DBConnectionPool;
import model.Invoice;
import model.Item;
import model.Order;
import model.OrderItem;
import repository.InvoiceRepo;
import repository.ItemRepo;
import repository.OrderItemRepo;
import repository.OrderRepo;
import service.InvoiceService;
import service.OrderService;

/**
 * Servlet implementation class order
 */
@WebServlet("/order/*")
public class order extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/item")
	private ItemRepo itemRepo;
	
	@Resource(name="repo/order")
	private OrderRepo orderRepo;
	
	@Resource(name="repo/orderitem")
	private OrderItemRepo orderItemRepo;
	
	@Resource(name="repo/invoice")
	private InvoiceRepo invoiceRepo;
	
	@Resource(name="service/invoice")
	private InvoiceService invoiceService;
	
	@Resource(name="service/order")
	private OrderService orderService;
	
	public void init() {
		this.orderItemRepo.setConnpool(dbConnPool);
		this.orderRepo.setConnpool(dbConnPool);
		this.invoiceRepo.setConnpool(dbConnPool);
		this.orderService.setOrderRepo(orderRepo);
		this.orderService.setOrderItemRepo(orderItemRepo);
		this.invoiceService.setInvoiceRepo(invoiceRepo);
		this.orderService.setInvoiceService(invoiceService);
		this.itemRepo.setConnpool(dbConnPool);
	}
       
    public order() {
        super();
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Item> items = new ArrayList<Item>();
		try {
			items = this.itemRepo.ListWithCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("items", items);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/order-create.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Order> orders = new ArrayList<Order>();
		try {
			orders = this.orderRepo.List();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("size of ordres  " + orders.size());
		request.setAttribute("orders", orders);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/order-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, SQLException {
    	Integer id = Integer.parseInt(request.getParameter("id"));
    	ArrayList<Item> items = new ArrayList<Item>();
    	ArrayList<OrderItem> orderitems = new ArrayList<OrderItem>();
    	Order order = this.orderRepo.Get(id);
    	Invoice invoice = new Invoice();
    	items = this.itemRepo.ListWithCategory();
    	orderitems = this.orderItemRepo.List(id);
    	
    	if (!(order.getInvoiceId() == null || order.getInvoiceId() <= 0)) {
    		invoice = this.invoiceRepo.GetByPk(order.getInvoiceId());
    	}
		request.setAttribute("order", order);
		request.setAttribute("items", items);
		request.setAttribute("orderid", id);
		request.setAttribute("ordername", order.getName());
		request.setAttribute("orderphone", order.getPhone());
		request.setAttribute("orderitems", orderitems);
		request.setAttribute("orderdiscount", order.getDiscount());
		request.setAttribute("invoice", invoice);
		request.setAttribute("invoicebill_amount", invoice.getBill_amount());
		request.setAttribute("invoicediscount", invoice.getDiscount());
		request.setAttribute("invoicefinal_amount", invoice.getFinal_bill());
		request.setAttribute("invoiceid", invoice.getId());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/order-edit.jsp");
		dispatcher.forward(request, response);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		String path = request.getPathInfo();
		System.out.println("action --> " + action + " path --> " + request.getPathInfo());
		System.out.println("params " + request.getParameterNames());
		switch (path) {
		case "/create": {
			showCreateForm(request, response);
			break;
		}
		case "/edit": {
			try {
				edit(request, response);
			} catch (NumberFormatException | ServletException | IOException | SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/delete": {
			delete(request, response);
			break;
		}
		case "/delete-invoice": {
			deleteInvoice(request, response);
			break;
		}
		case "/remove-item": {
			String id = request.getParameter("id");
			String orderid = request.getParameter("orderid");
			try {
				this.orderItemRepo.Delete(Integer.parseInt(id));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/order/edit?id="+orderid);
			break;
		}
		default:
			list(request, response);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		String path = request.getPathInfo();
		System.out.println("action --> " + action + " path --> " + request.getPathInfo());
		switch (path) {
		case "/order/create": {
			Order order = new Order();
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String discount = request.getParameter("discount");
			if (discount == "") {
				discount = "0";
			}
			order.setName(name);
			order.setPhone(phone);
			order.setDiscount(Double.parseDouble(discount));
			try {
				this.orderRepo.Add(order);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/order/list");
			break;
		}
		case "/order/edit": {
			Order order = new Order();
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String discount = request.getParameter("discount");
			String invoiceid = request.getParameter("invoiceid");
			if (discount.equals("")) {
				discount = "0";
			}
			if (invoiceid.equals("") || invoiceid.equals("null")) {
				invoiceid = "0";
			}
			order.setId(Integer.parseInt(id));
			order.setName(name);
			order.setPhone(phone);
			order.setDiscount(Double.parseDouble(discount));
			if (!invoiceid.equals("")) {
				order.setInvoiceId(Integer.parseInt(invoiceid));
			} else {
				order.setInvoiceId(null);
			}
			try {
				if (order.getInvoiceId() > 0) {
					this.orderRepo.UpdateOrderWithInvoice(order);
				} else {
					this.orderRepo.Update(order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/order/edit?id="+id);
			break;
		}
		case "/order/edit-invoice": {
			Order order = new Order();
			String id = request.getParameter("id");
			try {
				order = this.orderRepo.Get(Integer.parseInt(id));
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
			this.orderService.UpdateOrder(order);
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/order/edit?id="+id);
			break;
		}
		case "/order/add-item": {
			Integer orderId = Integer.parseInt(request.getParameter("orderid"));
			Integer itemId = Integer.parseInt(request.getParameter("itemid"));
			Double price = Double.parseDouble(request.getParameter("price"));
			Integer quantity = Integer.parseInt(request.getParameter("quantity"));
			OrderItem oi = new OrderItem();
			oi.setQuantity(quantity);
			oi.setOrder_id(orderId);
			oi.setItem_id(itemId);
			oi.setPrice(price);
			try {
				OrderItem existing = this.orderItemRepo.Get(orderId, itemId);
				if (existing != null && existing.getId() != null && existing.getId() > 0) {
					oi.setId(existing.getId());
					oi.setQuantity(existing.getQuantity() + 1);
					this.orderItemRepo.Update(oi);
				} else {
					this.orderItemRepo.Add(oi);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/order/edit?id="+orderId);
			break;
		}
		case "/order/edit-quantity": {
			Integer id = Integer.parseInt(request.getParameter("id"));
			Integer orderId = Integer.parseInt(request.getParameter("orderid"));
			Integer itemId = Integer.parseInt(request.getParameter("itemid"));
			Double price = Double.parseDouble(request.getParameter("price"));
			Integer quantity = Integer.parseInt(request.getParameter("quantity"));
			OrderItem oi = new OrderItem();
			oi.setId(id);
			oi.setOrder_id(orderId);
			oi.setItem_id(itemId);
			oi.setPrice(price);
			oi.setQuantity(quantity);
			try {
				this.orderItemRepo.Update(oi);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/order/edit?id="+orderId);
			break;
		}
		default:
			list(request, response);
			break;
		}

	}
	
	protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			this.orderRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}
	
	protected void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("invoiceid");
		try {
			this.invoiceRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
