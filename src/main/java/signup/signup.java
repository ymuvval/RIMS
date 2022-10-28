package signup;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import db.DBConnectionPool;
import service.UserService;
import service.ManagerService;

/**
 * Servlet implementation class signup
 */
@WebServlet("/signup")
public class signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService getUserServiceObj(String email, String pass) {
		DBConnectionPool dbConnPool = new DBConnectionPool(
			"com.mysql.jdbc.Driver",
			"jdbc:mysql://localhost:3306/rims",
			"root", ""
		);
//		Check user role using email and initiate the serive(manager or employee)
		return new ManagerService(dbConnPool);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public signup() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		RequestDispatcher dispatcher = null;
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		UserService user = this.getUserServiceObj(email, pass);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(pass);
		try {
			user.CreateUser(user);
			dispatcher = request.getRequestDispatcher("signup.jsp");
			request.setAttribute("status", "success");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

