package service.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import model.ReportType;
import repository.ItemRepo;
import service.ExpiryReportFactory;
import service.Report;
import service.ReportFactory;
import service.StockReportFactory;

class TestReport {
	
	@Mock ItemRepo itemRepo;

	@Test
	void testExpiryReportFactoryCreation() {
		ReportFactory erf = Report.FactoryMaker.makeFactory(ReportType.EXPIRY, itemRepo);
		
		assertTrue(erf instanceof ExpiryReportFactory);
	}
	
	@Test
	void testStockReportFactoryCreation() {
		ReportFactory srf = Report.FactoryMaker.makeFactory(ReportType.STOCK, itemRepo);
		
		assertTrue(srf instanceof StockReportFactory);
	}

}
