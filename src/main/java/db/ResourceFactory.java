package db;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class ResourceFactory implements ObjectFactory {

	@Override
	public Object getObjectInstance(Object obj, Name _name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {
		String driver = null;
		String user = null;
		String pass = null;
		String jdbc_url = null;
		Reference ref = (Reference) obj;
		Enumeration<RefAddr> addrs = ref.getAll();
		while (addrs.hasMoreElements()) {
			RefAddr addr = (RefAddr) addrs.nextElement();
			String name = addr.getType();
			String value = (String) addr.getContent();
			System.out.println("[DB Resource Factory] --> " + "name:" + name + " value:" +  value);
			if (name.equals("driver")) {
				driver = value;
			} else if (name.equals("username")) {
				user = value;
			} else if (name.equals("password")) {
				pass = value;
			} else if (name.equals("url")) {
				jdbc_url = value;
			}
		}
		return (new DBConnectionPool(driver, jdbc_url, user, pass));
	}

}
