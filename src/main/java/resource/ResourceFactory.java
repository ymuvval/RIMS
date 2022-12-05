package resource;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import repository.CategoryRepo;
import repository.InvoiceRepo;
import repository.ItemRepo;
import repository.LeaveRepo;
import repository.ManagerRepo;
import repository.OrderItemRepo;
import repository.OrderRepo;
import repository.ShiftReqRepo;
import repository.SupplierRepo;
import repository.TaskRepo;
import repository.UserRepo;
import service.InvoiceService;
import service.LeaveApprovalService;
import service.LeaveRequestService;
import service.ManagerService;
import service.OrderService;
import service.ReportService;
import service.ShiftApprovalService;
import service.ShiftRequestService;
import service.UserService;

public class ResourceFactory implements ObjectFactory {

	public Object getObjectInstance(Object obj, Name _name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {
		Reference ref = (Reference) obj;
		Enumeration<RefAddr> addrs = ref.getAll();
		Object res = null;
		while (addrs.hasMoreElements()) {
			RefAddr addr = (RefAddr) addrs.nextElement();
			String name = addr.getType();
			String value = (String) addr.getContent();
			System.out.println("[Resource Factory] --> " + "name:" + name + " value:" +  value);
			if (name.equals("resType")) {
				switch (value) {
				case "service.UserService": {
					res = (new UserService());
					break;
				}
				case "repository.UserRepo": {
					res = (new UserRepo());
					break;
				}
				case "service.ManagerService": {
					res = (new ManagerService());
					break;
				}
				case "repository.ManagerRepo": {
					res = (new ManagerRepo());
					break;
				}
				case "service.EmployeeService": {
					res = (new ManagerService());
					break;
				}
				case "repository.EmployeeRepo": {
					res = (new ManagerRepo());
					break;
				}
				case "repository.CategoryRepo": {
					res = (new CategoryRepo());
					break;
				}
				case "repository.OrderRepo": {
					res = (new OrderRepo());
					break;
				}
				case "repository.OrderItemRepo": {
					res = (new OrderItemRepo());
					break;
				}
				case "repository.InvoiceRepo": {
					res = (new InvoiceRepo());
					break;
				}
				case "service.InvoiceService": {
					res = (new InvoiceService());
					break;
				}
				case "service.OrderService": {
					res = (new OrderService());
					break;
				}
				case "repository.ItemRepo": {
					res = (new ItemRepo());
					break;
				}
				case "service.ReportService": {
					res = (new ReportService());
					break;
				}
				case "repository.LeaveRepo": {
					res = (new LeaveRepo());
					break;
				}
				case "service.LeaveRequestService": {
					res = (new LeaveRequestService());
					break;
				}
				case "service.LeaveApprovalService": {
					res = (new LeaveApprovalService());
					break;
				}
				case "repository.ShiftReqRepo": {
					res = (new ShiftReqRepo());
					break;
				}
				case "service.ShiftRequestService": {
					res = (new ShiftRequestService());
					break;
				}
				case "service.ShiftApprovalService": {
					res = (new ShiftApprovalService());
					break;
				}
				case "repository.SupplierRepo": {
					res = (new SupplierRepo());
					break;
				}
				case "repository.TaskRepo": {
					res = (new TaskRepo());
					break;
				}
				default:
					System.out.println("exception here " + value);
					throw new IllegalArgumentException("Unexpected value: " + value);
				}
			}
		}
		return res;
	}

}
