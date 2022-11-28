package web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;
import repository.UserRepo;
import db.*;
import service.UserService;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/user")
	private UserRepo userRepo;
	
	@Resource(name="service/user")
	private UserService userService;
	
	public void init() {
		System.out.print("came came came came");
		this.userRepo.setConnpool(dbConnPool);
		this.userService.setUserRepo(userRepo);
	}
	
	private User getUserServiceObj(String email, String pass) {
		LoginBean lb = new LoginBean();
		lb.setUname(email);
		lb.setPass(pass);
		
		User user = userService.Validate(email, pass);
		if (user == null) {
			return null;
		}
		return user;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		String email = request.getParameter("username");
		String pass = request.getParameter("password");
		User user = this.getUserServiceObj(email, pass);
		if (user == null) {
			request.setAttribute("loginError", "wrong creds or user doesnt exist");
			request.setAttribute("status", "failed");
			dispatcher = request.getRequestDispatcher("login.jsp");
		} else {
			session.setAttribute("name", user.getEmail());
			session.setAttribute("role", user.getRole());
			dispatcher = request.getRequestDispatcher("index.jsp");
		}
		dispatcher.forward(request, response);
		
	}

}
