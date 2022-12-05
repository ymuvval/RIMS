package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Item;

public interface IItemRepo {
	
	abstract public Item Get(Integer id) throws SQLException;
	abstract public void Add(Item item) throws SQLException;
	abstract public void Update(Item item) throws SQLException;
	abstract public ArrayList<Item> List() throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	abstract public ArrayList<Item> ListWithCategory() throws SQLException;

}
