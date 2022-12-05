package service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Clock;
import java.util.ArrayList;

import model.Item;
import repository.ItemRepo;

public class ExpiryReportFactory implements ReportFactory {
	private ItemRepo itemRepo;
	
	public ExpiryReportFactory() {
		super();
	}
	
	public ExpiryReportFactory(ItemRepo iR) {
		super();
		this.itemRepo = iR;
	}

	public ItemRepo getItemRepo() {
		return itemRepo;
	}

	public void setItemRepo(ItemRepo itemRepo) {
		this.itemRepo = itemRepo;
	}
	
	private Date sqlDatePlusDays(Date date, int days) {
	    return Date.valueOf(date.toLocalDate().plusDays(days));
	}
	
	@Override
	public ArrayList<Item> GenerateReport() throws SQLException {
		Date currentDate = new Date(Clock.systemUTC().millis());
		ArrayList<Item> items = itemRepo.ListWithCategory();
		ArrayList<Item> expiring = new ArrayList<Item>();
		for (int i = 0; i < items.size(); i++) {
//			System.out.println(items.get(i).getExpiry().toString());
			if (items.get(i).getExpiry().before(sqlDatePlusDays(currentDate, 15))) {
				expiring.add(items.get(i));
			}
		}
		return expiring;
	}

}
