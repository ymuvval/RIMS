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
import javax.servlet.http.HttpSession;

import db.DBConnectionPool;
import model.Leave;
import repository.LeaveRepo;
import repository.UserRepo;
import service.LeaveApprovalService;
import service.LeaveRequestService;

/**
 * Servlet implementation class leave
 */
@WebServlet("/leave/*")
public class leave extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;

	@Resource(name="repo/leave")
	private LeaveRepo leaveRepo;
	
	@Resource(name="repo/user")
	private UserRepo userRepo;

	@Resource(name="service/leave")
	private LeaveRequestService leaveReqService;
	
	@Resource(name="service/leaveapprove")
	private LeaveApprovalService leaveApprovalService;
	
	public void init() {
		this.leaveRepo.setConnpool(dbConnPool);
		this.userRepo.setConnpool(dbConnPool);
		this.leaveApprovalService.setLeaveRepo(leaveRepo);
		this.leaveApprovalService.setUserRepo(userRepo);
		this.leaveReqService.setLeaveRepo(leaveRepo);
		this.leaveReqService.setUserRepo(userRepo);
	}

    public leave() {
        super();
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/leave-create.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	String role = (String) session.getAttribute("role");
    	Integer userid = (Integer) session.getAttribute("userid");
    	ArrayList<Leave> leaves = new ArrayList<Leave>();
		try {
			if (role.equals("EMPLOYEE")) {
				leaves = this.leaveRepo.ListByEmp(userid);
			} else {
				leaves = this.leaveRepo.ListByManager(userid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("leaves", leaves);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/leave-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, SQLException {
    	String id = request.getParameter("id");
    	String status = request.getParameter("status");
    	this.leaveApprovalService.ApproveLeave(Integer.parseInt(id), status);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/leave/list");
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
		default:
			list(request, response);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
    	Integer userid = (Integer) session.getAttribute("userid");
    	Integer manager_id = (Integer) session.getAttribute("manager_id");
		String action = request.getServletPath();
		String path = request.getPathInfo();
		System.out.println("action --> " + action + " path --> " + request.getPathInfo());
		switch (path) {
		case "/create": {
			Leave leave = new Leave();
			Date start = Date.valueOf(request.getParameter("start"));
			Date end = Date.valueOf(request.getParameter("end"));
			leave.setEmpId(userid);
			leave.setStartDate(start);
			leave.setEndDate(end);
			leave.setManagerId(manager_id);
			try {
				this.leaveReqService.RequestLeave(leave);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/edit": {
			String id = request.getParameter("id");
			Leave leave = new Leave();
			Integer emp_id = userid;
			Date start = Date.valueOf(request.getParameter("start"));
			Date end = Date.valueOf(request.getParameter("end"));
			leave.setId(Integer.parseInt(id));
			leave.setEmpId(emp_id);
			leave.setStartDate(start);
			leave.setEndDate(end);
			leave.setManagerId(manager_id);
			try {
				this.leaveReqService.UpdateLeave(leave);
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
			this.leaveRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
