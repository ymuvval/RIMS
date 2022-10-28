package db;

import objectpool.ObjectPool;
import java.sql.*;

public class DBConnectionPool extends ObjectPool<Connection> {
	
	private String jdbc_url;
	private String user;
	private String pass;

	public DBConnectionPool(String driver, String jdbc_url, String user, String pass) {
		super();
		try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.jdbc_url = jdbc_url;
        this.user = user;
        this.pass = pass;
	}
	
	public Connection create() {
		System.out.println("Creating the Connecion...");
		try {
            return (DriverManager.getConnection(jdbc_url, user, pass));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return (null);
        }
	}
	
	public void dead(Connection c) {
		System.out.println("Connection was dead...");
        try {
//        	((Connection)o).close();
            c.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public boolean validate(Connection c) {
        try {
            return (!c.isClosed());
        }
        catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }
	
}
