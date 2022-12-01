package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Order;

public interface IOrderRepo {
	
	abstract public Order Get(Integer id) throws SQLException;
	abstract public Order Add(Order order) throws SQLException;
	abstract public void Update(Order order) throws SQLException;
	abstract public ArrayList<Order> List() throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	
}
