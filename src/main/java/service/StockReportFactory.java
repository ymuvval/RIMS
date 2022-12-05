package service;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Item;
import repository.ItemRepo;

public class StockReportFactory implements ReportFactory {
	private ItemRepo itemRepo;
	
	public StockReportFactory() {
		super();
	}
	
	public StockReportFactory(ItemRepo iR) {
		super();
		this.itemRepo = iR;
	}

	public ItemRepo getItemRepo() {
		return itemRepo;
	}

	public void setItemRepo(ItemRepo itemRepo) {
		this.itemRepo = itemRepo;
	}
	
	@Override
	public ArrayList<Item> GenerateReport() throws SQLException {
		ArrayList<Item> items = itemRepo.ListWithCategory();
		ArrayList<Item> lowStock = new ArrayList<Item>();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getQuantity().compareTo(5) == -1) {
				lowStock.add(items.get(i));
			}
		}
		return lowStock;
	}

}
