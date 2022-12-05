package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Supplier;

public interface ISupplierRepo {
	abstract public Supplier Get(Integer id) throws SQLException;
	abstract public void Add(Supplier supplier) throws SQLException;
	abstract public void Update(Supplier supplier) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	abstract public ArrayList<Supplier> List() throws SQLException;
}
