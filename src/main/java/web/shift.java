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
import model.ShiftRequest;
import model.ShiftType;
import repository.ShiftReqRepo;
import repository.UserRepo;
import service.ShiftApprovalService;
import service.ShiftRequestService;

/**
 * Servlet implementation class shift
 */
@WebServlet("/shift/*")
public class shift extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/rims")
	private DBConnectionPool dbConnPool;

	@Resource(name="repo/shiftreq")
	private ShiftReqRepo shiftReqRepo;
	
	@Resource(name="repo/user")
	private UserRepo userRepo;

	@Resource(name="service/shiftreq")
	private ShiftRequestService shiftReqService;
	
	@Resource(name="service/shiftreqapprove")
	private ShiftApprovalService shiftApprovalService;
	
	public void init() {
		this.shiftReqRepo.setConnpool(dbConnPool);
		this.userRepo.setConnpool(dbConnPool);
		this.shiftApprovalService.setShiftReqRepo(shiftReqRepo);
		this.shiftApprovalService.setUserRepo(userRepo);
		this.shiftReqService.setShiftRepo(shiftReqRepo);
		this.shiftReqService.setUserRepo(userRepo);
	}

    public shift() {
        super();
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/shift-create.jsp");
		dispatcher.forward(request, response);
    }
    
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	String role = (String) session.getAttribute("role");
    	Integer userid = (Integer) session.getAttribute("userid");
    	ArrayList<ShiftRequest> shifts = new ArrayList<ShiftRequest>();
		try {
			if (role.equals("EMPLOYEE")) {
				shifts = this.shiftReqRepo.ListByEmp(userid);
			} else {
				shifts = this.shiftReqRepo.ListByManager(userid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("shifts", shifts);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/shift-list.jsp");
		dispatcher.forward(request, response);
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, SQLException {
    	String id = request.getParameter("id");
    	String status = request.getParameter("status");
    	this.shiftApprovalService.ApproveShift(Integer.parseInt(id), status);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/shift/list");
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
			ShiftRequest shiftRequest = new ShiftRequest();
			String new_shift = request.getParameter("new_shift");
			shiftRequest.setEmpId(userid);
			shiftRequest.setNewShift(ShiftType.valueOf(new_shift));
			shiftRequest.setManagerId(manager_id);
			try {
				this.shiftReqService.RequestShift(shiftRequest);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case "/edit": {
			String id = request.getParameter("id");
			ShiftRequest shiftRequest = new ShiftRequest();
			String new_shift = request.getParameter("new_shift");
			shiftRequest.setId(Integer.parseInt(id));
			shiftRequest.setEmpId(userid);
			shiftRequest.setNewShift(ShiftType.valueOf(new_shift));
			shiftRequest.setManagerId(manager_id);
			try {
				this.shiftReqService.UpdateShift(shiftRequest);
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
			this.shiftReqRepo.Delete(Integer.parseInt(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("status", "success");
		response.sendRedirect("list");
	}

}
