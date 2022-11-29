package repository;

import java.sql.SQLException;

import model.Employee;
import model.User;

public interface IEmployeeRepo {
	abstract public Employee Get(String email) throws SQLException;
	abstract public User GetManager(Integer id) throws SQLException; // pass employee id and get manager
}
