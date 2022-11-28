package db;

import objectpool.ObjectPool;

import java.sql.*;

public class DBConnectionPool extends ObjectPool<Connection> {
	
	private String driver;
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
		this.setDriver(driver);
        this.jdbc_url = jdbc_url;
        this.user = user;
        this.pass = pass;
	}
	
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String getJdbc_url() {
		return jdbc_url;
	}

	public void setJdbc_url(String jdbc_url) {
		this.jdbc_url = jdbc_url;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
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
		System.out.println("Closing Connection...");
		if (c == null) {
			System.out.println("connection is null...");
		} else {
			try {
	            c.close();
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
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
