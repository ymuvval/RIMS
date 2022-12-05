package repository;

import java.sql.SQLException;

import model.Invoice;

public interface IInvoiceRepo {
	abstract public Invoice GetByPk(Integer id) throws SQLException;
	abstract public Invoice Add(Invoice invoice) throws SQLException;
	abstract public void Update(Invoice invoice) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
}
