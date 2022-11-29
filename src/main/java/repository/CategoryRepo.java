package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	private static final String LIST_CATEGORY = "SELECT * FROM `rims`.`category` ORDER BY `id`;";

	@Override
	public void Add(Category category) throws SQLException {
		System.out.println(ADD_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(ADD_CATEGORY);
			preparedStatement.setString(1, category.getType());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

	@Override
	public void Update(Category category) throws SQLException {
		System.out.println(UPDATE_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(UPDATE_CATEGORY);
			preparedStatement.setString(1, category.getType());
			preparedStatement.setInt(2, category.getId());
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
		System.out.println(DELETE_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(DELETE_CATEGORY);
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
	public void List() throws SQLException {
		System.out.println(LIST_CATEGORY);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = connpool.create();
			preparedStatement = dbConn.prepareStatement(LIST_CATEGORY);
//			preparedStatement.setString(1, category.getType());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			connpool.dead(dbConn);
		}
	}

}
