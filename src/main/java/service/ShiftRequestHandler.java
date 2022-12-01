package service;

import java.sql.SQLException;

import model.ShiftRequest;

public interface ShiftRequestHandler {
	public void RequestShift(ShiftRequest shiftReq) throws SQLException;
	public void UpdateShift(ShiftRequest shiftReq) throws SQLException;
	public void DeleteShift(Integer id) throws SQLException;
}
