package repository;

import java.sql.SQLException;

import model.Leave;

public interface ILeaveRepo {
	
	abstract public Leave Get(Integer id) throws SQLException;
	abstract public void Add(Leave leave) throws SQLException;
	abstract public void Update(Leave leave) throws SQLException;
	abstract public void UpdateStatus(Leave leave) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;

}
