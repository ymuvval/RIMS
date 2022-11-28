package resource;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import repository.ManagerRepo;
import repository.UserRepo;
import service.ManagerService;
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
				case "ManagerService": {
					res = (new ManagerService());
					break;
				}
				case "ManagerRepo": {
					res = (new ManagerRepo());
					break;
				}
				case "EmployeeService": {
					res = (new ManagerService());
					break;
				}
				case "EmployeeRepo": {
					res = (new ManagerRepo());
					break;
				}
				default:
					System.out.println("exception here");
					throw new IllegalArgumentException("Unexpected value: " + value);
				}
			}
		}
		return res;
	}

}
