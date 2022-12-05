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
import model.Employee;
import model.ShiftType;
import model.User;
import model.UserStatus;
import repository.ManagerRepo;
import repository.UserRepo;
import service.ManagerService;
import service.UserService;

/**
 * Servlet implementation class user
 */
@WebServlet("/user/*")
public class user extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/user")
	private UserRepo userRepo;
	
	@Resource(name="repo/manager")
	private ManagerRepo managerRepo;
	
	@Resource(name="service/user")
	private UserService userService;
	
	@Resource(name="service/manager")
	private ManagerService managerService;

	public void init() {
		this.userRepo.setConnpool(dbConnPool);
		this.userService.setUserRepo(userRepo);
		this.managerRepo.setConnpool(dbConnPool);
		this.managerService.setManagerRepo(managerRepo);
	}

    public user() {
        super();
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/edit-emp.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
    	ArrayList<User> users = new ArrayList<User>();
		try {
			users = this.managerService.GetEmployees(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("employees", users);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/emp-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
    	User _user = new User() {};
    	HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
		_user = this.userRepo.GetWithPass(userid);
		request.setAttribute("userid", _user.getId());
		request.setAttribute("username", _user.getName());
		request.setAttribute("useremail", _user.getEmail());
		request.setAttribute("userrole", _user.getRole());
		request.setAttribute("userpass", _user.getPassword());
		request.setAttribute("usershift", _user.getShiftType().name());
		request.setAttribute("useranswer", _user.getAnswer());
		request.setAttribute("userquestion", _user.getQuestion());
		request.setAttribute("userstatus", _user.getStatus().name());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-profile.jsp");
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
		case "/update-profile": {
			try {
				updateProfile(request, response);
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
		HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
		String action = request.getServletPath();
		String path = request.getPathInfo();
		System.out.println("action --> " + action + " path --> " + request.getPathInfo());
		switch (path) {
		case "/create": {
			Employee emp = new Employee();
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String question = request.getParameter("question");
			String answer = request.getParameter("answer");
			String shift = request.getParameter("shift");
			emp.setName(name);
			emp.setEmail(email);
			emp.setPassword(password);
			emp.setManagerId(userid);
			emp.setQuestion(question);
			emp.setAnswer(answer);
			emp.setShiftType(ShiftType.valueOf(shift.toUpperCase()));
			try {
				this.managerService.AddEmployee(emp);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("list");
			break;
		}
		case "/edit": {
			Employee emp = new Employee();
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String shift = request.getParameter("shift");
			emp.setId(Integer.parseInt(id));
			try {
				this.managerService.UpdateEmployee(emp.getId(), name, ShiftType.valueOf(shift.toUpperCase()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("status", "success");
			response.sendRedirect("list");
			break;
		}
		case "/user/update-profile": {
			User _user = new User() {};
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String pass = request.getParameter("pass");
			String question = request.getParameter("question");
			String answer = request.getParameter("answer");
			String status = request.getParameter("status");
			String shift = request.getParameter("shift");
			_user.setId(Integer.parseInt(id));
			_user.setName(name);
			_user.setEmail(email);
			_user.setPassword(pass);
			_user.setQuestion(question);
			_user.setAnswer(answer);
			_user.setStatus(UserStatus.valueOf(status.toUpperCase()));
			_user.setShiftType(ShiftType.valueOf(shift.toUpperCase()));
			try {
				this.userRepo.Update(_user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (_user.getStatus().equals(UserStatus.DEACTIVATED)) {
				request.setAttribute("status", "success");
				response.sendRedirect("/RIMS/logout");
				break;
			}
			request.setAttribute("status", "success");
			response.sendRedirect("/RIMS/home.jsp");
			break;
		}
		case "/forgot-pass": {
			String email = request.getParameter("email");
			User user = null;
			try {
				user = this.userRepo.Get(email);
			} catch (SQLException e1) {
				e1.printStackTrace();
				request.setAttribute("error", e1.getMessage());
				RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot-pass.jsp");
				dispatcher.forward(request, response);
				break;
			}
			if (user == null || user.getId() == null || user.getId() == 0) {
				request.setAttribute("error", "user does not exist. Please enter valid email");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot-pass.jsp");
				dispatcher.forward(request, response);
				break;
			}
			String question = request.getParameter("question");
			String answer = request.getParameter("answer");
			if (!question.equals(user.getQuestion()) || !answer.equals(user.getAnswer())) {
				request.setAttribute("error", "Question and Answer do not match");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot-pass.jsp");
				dispatcher.forward(request, response);
				break;
			}
			String pass = request.getParameter("password");
			user.setPassword(pass);
			try {
				this.userRepo.UpdatePass(email, pass);
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("error", e.getMessage());
				RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot-pass.jsp");
				dispatcher.forward(request, response);
				break;
			}
			request.setAttribute("status", "success");
			request.setAttribute("message", "Password updated successfully");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot-pass.jsp");
			dispatcher.forward(request, response);
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
			this.managerRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
