package service;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Item;
import model.ReportType;
import repository.ItemRepo;

public class ReportService {
	private ItemRepo itemRepo;
	
	public ItemRepo getItemRepo() {
		return itemRepo;
	}

	public void setItemRepo(ItemRepo itemRepo) {
		this.itemRepo = itemRepo;
	}
	
	public ArrayList<Item> GetReport(ReportType reportType) throws SQLException {
		final ReportFactory reportFactory = Report.FactoryMaker.makeFactory(reportType, itemRepo);
		return reportFactory.GenerateReport();
	}
}
