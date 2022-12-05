package service;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Item;

public interface ReportFactory {
	abstract public ArrayList<Item> GenerateReport() throws SQLException;
}
