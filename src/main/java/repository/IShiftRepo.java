package repository;

import java.sql.SQLException;

import model.Shift;

public interface IShiftRepo {
	
	abstract public Shift GetByPk(Integer id) throws SQLException;
	abstract public Shift Get(Integer empId) throws SQLException;
	abstract public void Add(Shift shift) throws SQLException;
	abstract public void Update(Shift shift) throws SQLException;

}
