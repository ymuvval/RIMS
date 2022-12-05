package service;

import java.util.ArrayList;

import model.Item;
import model.ReportType;
import repository.ItemRepo;

public class Report {
	private ArrayList<Item> items;
	
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	public static class FactoryMaker {
		public static ReportFactory makeFactory(ReportType reportType, ItemRepo itemRepo) {
			switch (reportType) {
			case STOCK: {
				return new StockReportFactory(itemRepo);
			}
			case EXPIRY: {
				return new ExpiryReportFactory(itemRepo);
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + reportType);
			}
		}
	}
}
