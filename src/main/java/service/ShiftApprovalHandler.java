package service;

import java.sql.SQLException;

public interface ShiftApprovalHandler {
	abstract public void ApproveShift(Integer id) throws SQLException;
}
