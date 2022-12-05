package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Category;

public interface ICategoryRepo {
	abstract public void Add(Category category) throws SQLException;
	abstract public void Update(Category category) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	abstract public ArrayList<Category> List() throws SQLException;
}
