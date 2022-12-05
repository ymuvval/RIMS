package repository;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Task;

public interface ITaskRepo {
	abstract public Task Get(Integer id) throws SQLException;
	abstract public void Add(Task task) throws SQLException;
	abstract public void UpdateTask(Task task) throws SQLException;
	abstract public void UpdateStatus(Task task) throws SQLException;
	abstract public void Delete(Integer id) throws SQLException;
	abstract public ArrayList<Task> List() throws SQLException;
	abstract public ArrayList<Task> ListEmpTasks(Integer id) throws SQLException;
}
