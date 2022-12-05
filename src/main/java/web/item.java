package web;

import java.io.IOException;
import java.sql.Date;
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
import model.Category;
import model.Item;
import model.ReportType;
import repository.CategoryRepo;
import repository.ItemRepo;
import service.ReportService;

/**
 * Servlet implementation class item
 */
@WebServlet("/item/*")
public class item extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/item")
	private ItemRepo itemRepo;
	
	@Resource(name="repo/category")
	private CategoryRepo categoryRepo;
	
	@Resource(name="service/report")
	private ReportService reportService;
	
	public void init() {
		this.itemRepo.setConnpool(dbConnPool);
		this.categoryRepo.setConnpool(dbConnPool);
		this.reportService.setItemRepo(itemRepo);
	}
       
    public item() {
        super();
    }
    
	protected void getReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		ArrayList<Item> items = new ArrayList<Item>();
		if (type == null || type == "" || type.equals("null")) {
			request.setAttribute("items", items);
			request.setAttribute("status", "success");
			response.sendRedirect("/rims/report.jsp");
			return;
		}
		try {
			items = this.reportService.GetReport(ReportType.valueOf(type.toUpperCase()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("reportitems", items);
		request.setAttribute("status", "success");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/report.jsp");
		dispatcher.forward(request, response);
	}
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Category> categories = new ArrayList<Category>();
		try {
			categories = this.categoryRepo.List();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("categories", categories);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/item-create.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Item> items = new ArrayList<Item>();
		try {
			items = this.itemRepo.ListWithCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("size of items  " + items.size());
		request.setAttribute("items", items);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/item-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, SQLException {
    	ArrayList<Category> categories = new ArrayList<Category>();
    	ArrayList<Item> items = new ArrayList<Item>();
    	String id = request.getParameter("id");
    	Item item = itemRepo.Get(Integer.parseInt(id));
		try {
			categories = this.categoryRepo.List();
			items = this.itemRepo.ListWithCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("categories", categories);
		request.setAttribute("items", items);
		request.setAttribute("itemid", id);
		request.setAttribute("itemname", item.getName());
		request.setAttribute("itemcategory_id", item.getCategoryId());
		request.setAttribute("itemquantity", item.getQuantity());
		request.setAttribute("itemprice", item.getPrice());
		request.setAttribute("itemexpiry", item.getExpiry().toString());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/item-edit.jsp");
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
		case "/report": {
			getReport(request, response);
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
		case "/item/create": {
			Item item = new Item();
			String name = request.getParameter("name");
			String categoryId= request.getParameter("category_id");
			String quantity = request.getParameter("quantity");
			String price = request.getParameter("price");
			String expiry = request.getParameter("expiry");
			item.setName(name);
			item.setCategoryId(Integer.parseInt(categoryId));
			item.setQuantity(Integer.parseInt(quantity));
			item.setPrice(Double.parseDouble(price));
			item.setExpiry(Date.valueOf(expiry));
			try {
				this.itemRepo.Add(item);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/item/edit": {
			Item item = new Item();
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String category_id = request.getParameter("category_id");
			String quantity = request.getParameter("quantity");
			String price = request.getParameter("price");
			String expiry = request.getParameter("expiry");
			item.setId(Integer.parseInt(id));
			item.setName(name);
			item.setCategoryId(Integer.parseInt(category_id));
			item.setQuantity(Integer.parseInt(quantity));
			item.setPrice(Double.parseDouble(price));
			item.setExpiry(Date.valueOf(expiry));
			try {
				this.itemRepo.Update(item);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		default:
			list(request, response);
			break;
		}
		request.setAttribute("status", "success");
		response.sendRedirect("/RIMS/item/list");
	}
	
	protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			this.itemRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
