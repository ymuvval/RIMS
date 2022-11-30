package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.OrderItem;

public class OrderItemRepo implements IOrderItemRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}

	private static final String ADD_ORDER_ITEM = "INSERT INTO `rims`.`item` (`order_id`, `item_id`, `price`, `quantity`) VALUES (?, ?, ?, ?);";
	private static final String UPDATE_ORDER_ITEM = "UPDATE `rims`.`item` SET `order_id` = ?, `item_id` = ?, `quantity` = ? WHERE `id` = ?;";
	private static final String DELETE_ORDER_ITEM = "DELETE FROM `rims`.`item` WHERE `id` = ?;";
	private static final String LIST_ORDER_ITEMS = "SELECT * FROM `rims`.`item` WHERE `order_id` = ?;";

	@Override
	public void Add(OrderItem orderItem) throws SQLException {
		System.out.println(ADD_ORDER_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_ORDER_ITEM);
			preparedStatement.setInt(1, orderItem.getOrder_id());
			preparedStatement.setInt(2, orderItem.getItem_id());
			preparedStatement.setDouble(3, orderItem.getPrice());
			preparedStatement.setInt(4, orderItem.getQuantity());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}
	
	@Override
	public void AddBatch(ArrayList<OrderItem> orderItems) throws SQLException {
		System.out.println(ADD_ORDER_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_ORDER_ITEM);
		    for (int i = 0; i < orderItems.size(); i++) {
		        OrderItem orderItem = orderItems.get(i);
		        preparedStatement.setInt(1, orderItem.getOrder_id());
				preparedStatement.setInt(2, orderItem.getItem_id());
				preparedStatement.setDouble(3, orderItem.getPrice());
				preparedStatement.setInt(4, orderItem.getQuantity());
		        preparedStatement.addBatch();
		    }
		    preparedStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

	@Override
	public void Update(OrderItem orderItem) throws SQLException {
		System.out.println(UPDATE_ORDER_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_ORDER_ITEM);
			preparedStatement.setInt(1, orderItem.getOrder_id());
			preparedStatement.setInt(2, orderItem.getItem_id());
			preparedStatement.setInt(3, orderItem.getQuantity());
			preparedStatement.setInt(4, orderItem.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return;
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_ORDER_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_ORDER_ITEM);
			preparedStatement.setInt(1, id);
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

	@Override
	public ArrayList<OrderItem> List(Integer orderId) throws SQLException {
		System.out.println(LIST_ORDER_ITEMS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(LIST_ORDER_ITEMS);
			preparedStatement.setInt(1, orderId);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
			if (rs.next()) {
				OrderItem orderItem = new OrderItem() {};
				orderItem.setId(rs.getInt("id"));
				orderItem.setOrder_id(rs.getInt("order_id"));
				orderItem.setItem_id(rs.getInt("item_id"));
				orderItem.setQuantity(rs.getInt("quantity"));
				orderItem.setPrice(rs.getDouble("price"));
				orderItems.add(orderItem);
			}
			return orderItems;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

}
