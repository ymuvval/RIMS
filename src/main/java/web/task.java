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
import model.Task;
import model.TaskStatus;
import model.User;
import repository.ManagerRepo;
import repository.TaskRepo;

/**
 * Servlet implementation class task
 */
@WebServlet("/task/*")
public class task extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;
	
	@Resource(name="repo/manager")
	private ManagerRepo managerRepo;
	
	@Resource(name="repo/task")
	private TaskRepo taskRepo;
	
	public void init() {
		this.managerRepo.setConnpool(dbConnPool);
		this.taskRepo.setConnpool(dbConnPool);
	}

    public task() {
        super();
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
    	ArrayList<User> employees = new ArrayList<User>();
		try {
			employees = this.managerRepo.GetEmployees(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("employees", employees);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/task-create.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
    	String role = (String) session.getAttribute("role");
    	ArrayList<User> employees = new ArrayList<User>();
		try {
			employees = this.managerRepo.GetEmployees(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("employees", employees);
    	ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			if (role.equals("MANAGER")) {
				tasks = this.taskRepo.List();
			} else if (role.equals("EMPLOYEE")) {
				tasks = this.taskRepo.ListEmpTasks(userid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("employees", employees);
		request.setAttribute("tasks", tasks);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/task-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void editStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String id = request.getParameter("id");
    	String status = request.getParameter("status");
    	Task task = new Task() {};
    	task.setId(Integer.parseInt(id));
    	try {
    		task.setStatus(TaskStatus.valueOf(status.toUpperCase()));
    		this.taskRepo.UpdateStatus(task);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/task/list");
		dispatcher.forward(request, response);
    }
    
    private void editTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
    	ArrayList<User> employees = new ArrayList<User>();
		try {
			employees = this.managerRepo.GetEmployees(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("employees", employees);
    	String id = request.getParameter("id");
    	Task task = new Task() {};
    	task.setId(Integer.parseInt(id));
    	try {
    		task = this.taskRepo.Get(task.getId());
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	request.setAttribute("taskemployeeid", task.getEmpId());
    	request.setAttribute("taskContent", task.getTask());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/task-edit.jsp");
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
		case "/edit-status": {
			editStatus(request, response);
			break;
		}
		case "/edit-task": {
			editTask(request, response);
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
		case "/task/create": {
			Task task = new Task();
			String empId = request.getParameter("empId");
			String taskContent = request.getParameter("task");
			task.setEmpId(Integer.parseInt(empId));
			task.setManagerId(userid);
			task.setTask(taskContent);
			try {
				this.taskRepo.Add(task);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/task/edit": {
			Task task = new Task() {};
			String id = request.getParameter("id");
	    	String taskContent = request.getParameter("task");
	    	task.setId(Integer.parseInt(id));
	    	task.setTask(taskContent);
			try {
				this.taskRepo.UpdateTask(task);
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
			this.taskRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
