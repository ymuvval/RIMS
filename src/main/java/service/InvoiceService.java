package service;

import java.sql.SQLException;

import model.Invoice;
import model.Order;
import repository.InvoiceRepo;

public class InvoiceService {
	private InvoiceRepo invoiceRepo = null;

	public void setInvoiceRepo(InvoiceRepo invoiceRepo) {
		this.invoiceRepo = invoiceRepo;
	}

	public InvoiceService() {
		super();
	}
	
	public InvoiceService(InvoiceRepo iR) {
		super();
		this.invoiceRepo = iR;
	}
	
	public Invoice makeInvoice() {
		return new Invoice();
	}
	
	public Invoice GenerateInvoice(Order order) {
		Invoice invoice = this.makeInvoice();
		invoice.setBill_amount(order.CalculateBill());
		invoice.setDiscount(order.getDiscount());
		invoice.setFinal_bill(invoice.getBill_amount() - order.getDiscount());
		try {
			invoice =  this.invoiceRepo.Add(invoice);
			return invoice;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void UpdateInvoice(Order order) {
		Invoice invoice = this.makeInvoice();
		invoice.setId(order.getInvoiceId());
		invoice.setBill_amount(order.CalculateBill());
		invoice.setDiscount(order.getDiscount());
		invoice.setFinal_bill(invoice.getBill_amount() - order.getDiscount());
		if (invoice.getFinal_bill() < 0.0) {
			invoice.setFinal_bill(0.0);
		}
		try {
			this.invoiceRepo.Update(invoice);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteInvoice(Integer id) throws SQLException {
		try {
			this.invoiceRepo.Delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
