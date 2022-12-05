package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.OrderItem;

public interface IOrderItemRepo {
	abstract public OrderItem Get(Integer orderId, Integer itemId) throws SQLException;
	abstract public ArrayList<OrderItem> List(Integer orderId) throws SQLException;
	abstract public void Add(OrderItem orderItem) throws SQLException;
	abstract public void AddBatch(ArrayList<OrderItem> orderItems) throws SQLException;
	abstract public void Update(OrderItem orderItem) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
}
