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
import model.Category;
import repository.CategoryRepo;

/**
 * Servlet implementation class category
 */
@WebServlet("/category/*")
public class category extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/category")
	private CategoryRepo categoryRepo;
	
	public void init() {
		this.categoryRepo.setConnpool(dbConnPool);
	}
       
    public category() {
        super();
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/createCategory.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Category> categories = new ArrayList<Category>();
		try {
			categories = this.categoryRepo.List();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("size of category  " + categories.size());
		request.setAttribute("categories", categories);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/category-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ArrayList<Category> categories = new ArrayList<Category>();
		try {
			categories = this.categoryRepo.List();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("size of category  " + categories.size());
		request.setAttribute("categories", categories);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/category-list.jsp");
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
			edit(request, response);
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
		switch (path) {
		case "/create": {
			Category cat = new Category();
			String type = request.getParameter("type");
			cat.setType(type);
			try {
				this.categoryRepo.Add(cat);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/edit": {
			Category cat = new Category();
			String id = request.getParameter("id");
			String type = request.getParameter("type");
			cat.setType(type);
			cat.setId(Integer.parseInt(id));
			try {
				this.categoryRepo.Update(cat);
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
			this.categoryRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
