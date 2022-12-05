package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Supplier;
import model.SupplierStatus;

public class SupplierRepo implements ISupplierRepo {

	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_SUPPLIER = "SELECT * FROM `rims`.`supplier` WHERE `id` = ?;";
	private static final String ADD_SUPPLIER = "INSERT INTO `rims`.`supplier` (`name`, `phone`, `address`) VALUES (?, ?, ?);";
	private static final String UPDATE_SUPPLIER = "UPDATE `rims`.`supplier` SET `name` = ?, `phone` = ?, `address` = ?, `status` = ? WHERE `id` = ?;";
	private static final String DELETE_SUPPLIER = "DELETE FROM `rims`.`supplier` WHERE `id` = ?;";
	private static final String LIST_SUPPLIER = "SELECT * FROM `rims`.`supplier`;";
	
	public Connection getConn() {
		return this.connpool.create();
	}
	
	
	@Override
	public Supplier Get(Integer id) throws SQLException {
		System.out.println(GET_SUPPLIER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_SUPPLIER);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			Supplier supplier = new Supplier() {};
			if (rs.next()) {
				supplier.setId(rs.getInt("id"));
				supplier.setName(rs.getString("name"));
				supplier.setPhone(rs.getString("phone"));
				supplier.setAddress(rs.getString("address"));
				supplier.setStatus(SupplierStatus.valueOf(rs.getString("status")));
			}
			return supplier;
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
	public void Add(Supplier supplier) throws SQLException {
		System.out.println(ADD_SUPPLIER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(ADD_SUPPLIER);
			preparedStatement.setString(1, supplier.getName());
			preparedStatement.setString(2, supplier.getPhone());
			preparedStatement.setString(3, supplier.getAddress());
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
	public void Update(Supplier supplier) throws SQLException {
		System.out.println(UPDATE_SUPPLIER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_SUPPLIER);
			preparedStatement.setString(1, supplier.getName());
			preparedStatement.setString(2, supplier.getPhone());
			preparedStatement.setString(3, supplier.getAddress());
			preparedStatement.setString(4, supplier.getStatus().name());
			preparedStatement.setInt(5, supplier.getId());
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
		System.out.println(DELETE_SUPPLIER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(DELETE_SUPPLIER);
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
	public ArrayList<Supplier> List() throws SQLException {
		System.out.println(LIST_SUPPLIER);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(LIST_SUPPLIER);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
			while (rs.next()) {
				Supplier supplier = new Supplier() {};
				supplier.setId(rs.getInt("id"));
				supplier.setName(rs.getString("name"));
				supplier.setPhone(rs.getString("phone"));
				supplier.setAddress(rs.getString("address"));
				supplier.setStatus(SupplierStatus.valueOf(rs.getString("status")));
				suppliers.add(supplier);
			}
			return suppliers;
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
