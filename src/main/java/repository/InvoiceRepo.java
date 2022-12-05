package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnectionPool;
import model.Invoice;

public class InvoiceRepo implements IInvoiceRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_INVOICE = "SELECT * FROM `rims`.`invoice` WHERE `id` = ?;";
	private static final String ADD_INVOICE = "INSERT INTO `rims`.`invoice` (`bill_amount`, `discount`, `final_amount`) VALUES (?, ?, ?);";
	private static final String UPDATE_INVOICE = "UPDATE `rims`.`invoice` SET `bill_amount` = ?, `discount` = ?, `final_amount` = ? WHERE `id` = ?;";
	private static final String DELETE_INVOICE = "DELETE FROM `rims`.`invoice` WHERE `id` = ?;";

	public Connection getConn() {
		return this.connpool.create();
	}
	
	@Override
	public Invoice GetByPk(Integer id) throws SQLException {
		System.out.println(GET_INVOICE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_INVOICE);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			Invoice invoice = new Invoice() {};
			if (rs.next()) {
				invoice.setId(rs.getInt("id"));
				invoice.setBill_amount(rs.getDouble("bill_amount"));
				invoice.setDiscount(rs.getDouble("discount"));
				invoice.setFinal_bill(rs.getDouble("final_amount"));
			}
			return invoice;
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
	public Invoice Add(Invoice invoice) throws SQLException {
		System.out.println(ADD_INVOICE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(ADD_INVOICE, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setDouble(1, invoice.getBill_amount());
			preparedStatement.setDouble(2, invoice.getDiscount());
			preparedStatement.setDouble(3, invoice.getFinal_bill());
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				invoice.setId(rs.getInt(1));
			}
			return invoice;
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
	public void Update(Invoice invoice) throws SQLException {
		System.out.println(UPDATE_INVOICE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_INVOICE);
			preparedStatement.setDouble(1, invoice.getBill_amount());
			preparedStatement.setDouble(2, invoice.getDiscount());
			preparedStatement.setDouble(3, invoice.getFinal_bill());
			preparedStatement.setInt(4, invoice.getId());
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
		return;
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_INVOICE);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(DELETE_INVOICE);
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

}
