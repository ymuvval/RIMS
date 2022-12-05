package service;

import java.sql.SQLException;

public interface LeaveApprovalHandler {
	abstract public void ApproveLeave(Integer id, String status) throws SQLException;
}
