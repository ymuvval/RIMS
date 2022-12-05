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
import javax.servlet.http.HttpSession;

import db.DBConnectionPool;
import model.Supplier;
import model.SupplierStatus;
import repository.SupplierRepo;
import repository.UserRepo;

/**
 * Servlet implementation class supplier
 */
@WebServlet("/supplier/*")
public class supplier extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/supplier")
	private SupplierRepo supplierRepo;
	
	@Resource(name="repo/user")
	private UserRepo userRepo;
	
	public void init() {
		this.supplierRepo.setConnpool(dbConnPool);
		this.userRepo.setConnpool(dbConnPool);
	}

	public supplier() {
        super();
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/supplier-create.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		try {
			suppliers = this.supplierRepo.List();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("suppliers", suppliers);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/supplier-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
    	Supplier _supplier = new Supplier() {};
    	
    	String id = request.getParameter("id");
    	try {
    		_supplier = this.supplierRepo.Get(Integer.parseInt(id));
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	request.setAttribute("supplierid", _supplier.getId());
    	request.setAttribute("suppliername", _supplier.getName());
    	request.setAttribute("supplierphone", _supplier.getPhone());
    	request.setAttribute("supplieraddress", _supplier.getAddress());
    	request.setAttribute("supplierstatus", _supplier.getStatus().name());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/supplier-edit.jsp");
		dispatcher.forward(request, response);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		String path = request.getPathInfo();
		System.out.println("action --> " + action + " path --> " + request.getPathInfo());
		switch (path) {
		case "/create": {
			showCreateForm(request, response);
			break;
		}
		case "/edit": {
			try {
				edit(request, response);
			} catch (ServletException | IOException | SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/delete": {
			delete(request, response);
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
		
		HttpSession session = request.getSession();
    	String role = (String) session.getAttribute("role");
    	if (!role.equals("MANAGER")) {
    		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    		return;
    	}
    	
		switch (path) {
		case "/create": {
			Supplier supplier = new Supplier();
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			supplier.setName(name);
			supplier.setPhone(phone);
			supplier.setAddress(address);
			try {
				this.supplierRepo.Add(supplier);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/supplier/edit": {
			Supplier supplier = new Supplier();
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			String status = request.getParameter("status");
			supplier.setId(Integer.parseInt(id));
			supplier.setName(name);
			supplier.setPhone(phone);
			supplier.setAddress(address);
			supplier.setStatus(SupplierStatus.valueOf(status));
			try {
				this.supplierRepo.Update(supplier);
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
		response.sendRedirect("list");
	}
	
	protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			this.supplierRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
