package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Category;

public class CategoryRepo implements ICategoryRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String ADD_CATEGORY = "INSERT INTO `rims`.`category` (`type`) VALUES (?);";
	private static final String UPDATE_CATEGORY = "UPDATE `rims`.`category` SET `type` = ? WHERE `id` = ?;";
	private static final String DELETE_CATEGORY = "DELETE FROM `rims`.`category` WHERE `id` = ?;";
	private static final String LIST_CATEGORY = "SELECT * FROM `rims`.`category`;";
	
	public Connection getConn() {
		return this.connpool.create();
	}

	@Override
	public void Add(Category category) throws SQLException {
		System.out.println(ADD_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(ADD_CATEGORY);
			preparedStatement.setString(1, category.getType());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public void Update(Category category) throws SQLException {
		System.out.println(UPDATE_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_CATEGORY);
			preparedStatement.setString(1, category.getType());
			preparedStatement.setInt(2, category.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(DELETE_CATEGORY);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public ArrayList<Category> List() throws SQLException {
		System.out.println(LIST_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(LIST_CATEGORY);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<Category> categories = new ArrayList<Category>();
			while (rs.next()) {
				Category category = new Category() {};
				category.setType(rs.getString("type"));
				category.setId(rs.getInt("id"));
				categories.add(category);
			}
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

}
