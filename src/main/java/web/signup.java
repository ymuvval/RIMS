package web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import db.DBConnectionPool;
import model.User;
import repository.ManagerRepo;
import repository.UserRepo;
import service.UserService;
import service.ManagerService;

/**
 * Servlet implementation class signup
 */
@WebServlet("/signup")
public class signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/manager")
	private ManagerRepo managerRepo;
	
	@Resource(name="service/manager")
	private ManagerService managerService;
	
	public void init() {
		this.managerRepo.setConnpool(dbConnPool);
		this.managerService.setManagerRepo(this.managerRepo);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = null;
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		
		if (this.managerService.IsUserPresent(email) == null) {
//			send user present error
			
		}
		User user = new User() {};
		user.setName(name);
		user.setEmail(email);
		user.setPassword(pass);
		user.setRole("MANAGER");
		try {
			this.managerService.CreateUser(user);
			dispatcher = request.getRequestDispatcher("login.jsp");
			request.setAttribute("status", "success");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

