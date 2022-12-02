package repository;

import java.sql.SQLException;

import model.ShiftRequest;

public interface IShiftReqRepo {
	
	abstract public ShiftRequest Get(Integer id) throws SQLException;
	abstract public void Add(ShiftRequest sr) throws SQLException;
	abstract public void Update(ShiftRequest sr) throws SQLException;
	abstract public void UpdateStatus(ShiftRequest sr) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	
}
