package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Order;

public class OrderRepo implements IOrderRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
//	'cake', 1, 2, 12, '2022-11-30'
	private static final String GET_ORDER = "SELECT * FROM `rims`.`order` WHERE `id` = ?;";
	private static final String ADD_ORDER = "INSERT INTO `rims`.`order` (`name`, `phone`, `discount`) VALUES (?, ?, ?);";
	private static final String UPDATE_ORDER = "UPDATE `rims`.`order` SET `name` = ?, `phone` = ?, `discount` = ? WHERE `id` = ?;";
	private static final String DELETE_ORDER = "DELETE FROM `rims`.`order` WHERE `id` = ?;";
	private static final String LIST_ORDER = "SELECT * FROM `rims`.`order` ORDER BY `id`;";

	@Override
	public Order Get(Integer id) throws SQLException {
		System.out.println(GET_ORDER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(GET_ORDER);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			Order order = new Order() {};
			if (rs.next()) {
				order.setName(rs.getString("name"));
				order.setPhone(rs.getString("phone"));
				order.setDiscount(rs.getDouble("discount"));
				order.setInvoiceId(rs.getInt("invoice_id"));
			}
			return order;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public Order Add(Order order) throws SQLException {
		System.out.println(ADD_ORDER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_ORDER);
			preparedStatement.setInt(1, order.getInvoiceId());
			preparedStatement.setString(2, order.getName());
			preparedStatement.setString(3, order.getPhone());
			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			order.setId(generatedKeys.getInt(1));
			return order;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public void Update(Order order) throws SQLException {
		System.out.println(UPDATE_ORDER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_ORDER);
			preparedStatement.setString(1, order.getName());
			preparedStatement.setString(2, order.getPhone());
			preparedStatement.setDouble(2, order.getDiscount());
			preparedStatement.setInt(4, order.getId());
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
	public ArrayList<Order> List() throws SQLException {
		System.out.println(LIST_ORDER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(LIST_ORDER);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<Order> orders = new ArrayList<Order>();
			if (rs.next()) {
				Order order = new Order() {};
				order.setId(rs.getInt("id"));
				order.setInvoiceId(rs.getInt("invoice_id"));
				order.setName(rs.getString("name"));
				order.setPhone(rs.getString("phone"));
				orders.add(order);
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_ORDER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_ORDER);
			preparedStatement.setInt(1, id);
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}
	
}
