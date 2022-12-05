package repository;

import java.sql.SQLException;

import model.ShiftType;
import model.User;

public interface IUserRepo {
	
	abstract public User GetByPk(Integer id) throws SQLException;
	abstract public User Get(String email) throws SQLException;
	abstract public void Update(Integer id, String name, ShiftType shift) throws SQLException;
	abstract public void Update(User user) throws SQLException;
	abstract public void UpdatePass(String Email, String pass) throws SQLException;
	abstract public void UpdateShift(Integer id, ShiftType shift) throws SQLException;

}
