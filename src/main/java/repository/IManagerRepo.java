package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.User;

public interface IManagerRepo extends IUserRepo {
	
	abstract public User Create(User user) throws SQLException;
	abstract public User Add(Employee emp) throws SQLException;
	abstract public ArrayList<User> List(Integer id) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	abstract public void DeActivate(Integer id);

}
