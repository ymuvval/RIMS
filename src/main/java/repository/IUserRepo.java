package repository;

import java.sql.SQLException;

import model.User;

public interface IUserRepo {
	
	abstract public User GetByPk(Integer id) throws SQLException;
	abstract public User Get(String email);
	abstract public void Update(Integer id, String name) throws SQLException;
	abstract public void UpdatePass(String Email, String pass) throws SQLException;
	
}
