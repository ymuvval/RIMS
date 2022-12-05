package model;

import java.util.ArrayList;

public class Order {
	private Integer id;
	private String name;
	private String phone;
	private ArrayList<OrderItem> items;
	private Integer invoiceId;
	private Double discount;
	
	public Order() {
		super();
	}
	
	public Order(String name, String phone, Integer invoiceId, Double discount) {
		super();
		this.name = name;
		this.phone = phone;
		this.invoiceId = invoiceId;
		this.discount = discount;
	}
	
	
	public ArrayList<OrderItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	public Order Clone() {
		Order clone = new Order();
		clone.setId(this.getId());
		clone.setInvoiceId(this.getInvoiceId());
		clone.setDiscount(this.getDiscount());
		clone.setName(this.getName());
		clone.setPhone(this.getPhone());
		return clone;
	}
	
	public Double CalculateBill() {
		Double amount = 0.0;
		for (OrderItem item: this.items) {
			amount += item.getPrice() * (double) item.getQuantity();
		};
		return amount;
	}
}
