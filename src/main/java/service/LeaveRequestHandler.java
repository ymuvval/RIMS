package service;

import java.sql.SQLException;

import model.Leave;

public interface LeaveRequestHandler {
	public void RequestLeave(Leave leave) throws SQLException;
	public void UpdateLeave(Leave leave) throws SQLException;
	public void DeleteLeave(Integer id) throws SQLException;
}
