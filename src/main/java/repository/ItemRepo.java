package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Item;

public class ItemRepo implements IItemRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}

//	'cake', 1, 2, 12, '2022-11-30'
	private static final String GET_ITEM = "SELECT * FROM `rims`.`item` WHERE `id` = ?;";
	private static final String ADD_ITEM = "INSERT INTO `rims`.`item` (`name`, `category_id`, `quantity`, `price`, `expiry`) VALUES (?, ?, ?, ?, ?);";
	private static final String UPDATE_ITEM = "UPDATE `rims`.`item` SET `name` = ?, `category_id` = ?, `quantity` = ?, `price` = ?, `expiry` = ? WHERE `id` = ?;";
	private static final String DELETE_ITEM = "DELETE FROM `rims`.`item` WHERE `id` = ?;";
	private static final String LIST_ITEM = "SELECT * FROM `rims`.`item` ORDER BY `id`;";

	@Override
	public Item Get(Integer id) throws SQLException {
		System.out.println(GET_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(GET_ITEM);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			Item item = new Item() {};
			if (rs.next()) {
				item.setName(rs.getString("name"));
				item.setCategoryId(rs.getInt("category_id"));
				item.setQuantity(rs.getInt("quantity"));
				item.setPrice(rs.getDouble("price"));
				item.setExpiry(rs.getDate("expiry"));
			}
			return item;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
		return null;
	}

	@Override
	public void Add(Item item) throws SQLException {
		System.out.println(ADD_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_ITEM);
			preparedStatement.setString(1, item.getName());
			preparedStatement.setInt(2, item.getCategoryId());
			preparedStatement.setInt(3, item.getQuantity());
			preparedStatement.setDouble(4, item.getPrice());
			preparedStatement.setDate(5, item.getExpiry());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

	@Override
	public void Update(Item item) throws SQLException {
		System.out.println(UPDATE_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_ITEM);
			preparedStatement.setString(1, item.getName());
			preparedStatement.setInt(2, item.getCategoryId());
			preparedStatement.setInt(3, item.getQuantity());
			preparedStatement.setDouble(4, item.getPrice());
			preparedStatement.setDate(5, item.getExpiry());
			preparedStatement.setInt(6, item.getId());
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
	public ArrayList<Item> List() throws SQLException {
		System.out.println(LIST_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(LIST_ITEM);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<Item> items = new ArrayList<Item>();
			if (rs.next()) {
				Item item = new Item() {};
				item.setName(rs.getString("name"));
				item.setCategoryId(rs.getInt("category_id"));
				item.setQuantity(rs.getInt("quantity"));
				item.setPrice(rs.getDouble("price"));
				item.setExpiry(rs.getDate("expiry"));
				items.add(item);
			}
			return items;
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
		System.out.println(DELETE_ITEM);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_ITEM);
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
