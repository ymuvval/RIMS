package web;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBConnectionPool;
import model.LoginBean;
import model.User;
import repository.UserRepo;
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
		this.userRepo.setConnpool(dbConnPool);
		this.userService.setUserRepo(userRepo);
	}
	
	private User getUserServiceObj(String email, String pass) throws SQLException {
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
		String email = request.getParameter("username");
		String pass = request.getParameter("password");
		User user = null;
		try {
			user = this.getUserServiceObj(email, pass);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (user == null) {
			request.setAttribute("error", "wrong creds or user doesnt exist");
			request.setAttribute("status", "failed");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
//			response.sendRedirect(request.getContextPath() + "/login.jsp");
		} else {
			if (user.getManagerId() != null && user.getManagerId() > 0) {
				try {
					User manager = this.userRepo.GetByPk(user.getManagerId());
					session.setAttribute("manager_id", user.getManagerId());
					session.setAttribute("manager_name", manager.getName());
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			session.setAttribute("userid", user.getId());
			session.setAttribute("name", user.getName().toUpperCase());
			session.setAttribute("role", user.getRole());
			session.setAttribute("email", user.getEmail());
			response.sendRedirect(request.getContextPath() + "/home.jsp");
		}
		
	}

}
