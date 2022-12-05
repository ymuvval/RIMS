package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DBConnectionPool;
import model.Task;
import model.TaskStatus;

public class TaskRepo implements ITaskRepo {
	private DBConnectionPool connpool;

	public DBConnectionPool getConnpool() {
		return connpool;
	}

	public void setConnpool(DBConnectionPool connpool) {
		this.connpool = connpool;
	}
	
	private static final String GET_TASK = "SELECT * FROM `rims`.`task` WHERE `id` = ?;";
	private static final String ADD_TASK = "INSERT INTO `rims`.`task` (`emp_id`, `manager_id`, `task`) VALUES (?, ?, ?);";
	private static final String UPDATE_TASK = "UPDATE `rims`.`task` SET `task` = ? WHERE `id` = ?;";
	private static final String UPDATE_STATUS = "UPDATE `rims`.`task` SET `status` = ? WHERE `id` = ?;";
	private static final String DELETE_TASK = "DELETE FROM `rims`.`task` WHERE `id` = ?;";
	private static final String LIST_TASK = "SELECT * FROM `rims`.`task`;";
	private static final String LIST_EMP_TASK = "SELECT * FROM `rims`.`task` WHERE `emp_id` = ?;";
	
	public Connection getConn() {
		return this.connpool.create();
	}
	
	@Override
	public Task Get(Integer id) throws SQLException {
		System.out.println(GET_TASK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(GET_TASK);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			Task task = new Task() {};
			if (rs.next()) {
				task.setId(rs.getInt("id"));
				task.setEmpId(rs.getInt("emp_id"));
				task.setManagerId(rs.getInt("manager_id"));
				task.setStatus(TaskStatus.valueOf(rs.getString("status")));
				task.setTask(rs.getString("task"));
			}
			return task;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public void Add(Task task) throws SQLException {
		System.out.println(ADD_TASK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(ADD_TASK);
			preparedStatement.setInt(1, task.getEmpId());
			preparedStatement.setInt(2, task.getManagerId());
			preparedStatement.setString(3, task.getTask());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public void UpdateTask(Task task) throws SQLException {
		System.out.println(UPDATE_TASK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_TASK);
			preparedStatement.setString(1, task.getTask());
			preparedStatement.setInt(2, task.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}
	
	@Override
	public void UpdateStatus(Task task) throws SQLException {
		System.out.println(UPDATE_STATUS);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(UPDATE_STATUS);
			preparedStatement.setString(1, task.getStatus().name());
			preparedStatement.setInt(2, task.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public void Delete(Integer id) throws SQLException {
		System.out.println(DELETE_TASK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(DELETE_TASK);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

	@Override
	public ArrayList<Task> List() throws SQLException {
		System.out.println(LIST_TASK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(LIST_TASK);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<Task> tasks = new ArrayList<Task>();
			while (rs.next()) {
				Task task = new Task() {};
				task.setId(rs.getInt("id"));
				task.setEmpId(rs.getInt("emp_id"));
				task.setManagerId(rs.getInt("manager_id"));
				task.setStatus(TaskStatus.valueOf(rs.getString("status")));
				task.setTask(rs.getString("task"));
				tasks.add(task);
			}
			return tasks;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}
	
	@Override
	public ArrayList<Task> ListEmpTasks(Integer id) throws SQLException {
		System.out.println(LIST_EMP_TASK);
		Connection dbConn = null;
		PreparedStatement preparedStatement = null;
		try {
			dbConn = this.getConn();
			preparedStatement = dbConn.prepareStatement(LIST_EMP_TASK);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			ArrayList<Task> tasks = new ArrayList<Task>();
			while (rs.next()) {
				Task task = new Task() {};
				task.setId(rs.getInt("id"));
				task.setEmpId(rs.getInt("emp_id"));
				task.setManagerId(rs.getInt("manager_id"));
				task.setStatus(TaskStatus.valueOf(rs.getString("status")));
				task.setTask(rs.getString("task"));
				tasks.add(task);
			}
			return tasks;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connpool != null) { 
				connpool.dead(dbConn);
			}
		}
	}

}
