package model;

import java.sql.Date;

public class Item {
	private Integer id;
	private String name;
	private Integer categoryId;
	private Integer quantity;
	private Double price;
	private Date expiry;
	private String categoryType;
	
	public Item() {
		super();
	}
	
	public Item(String name, Integer categoryId, Integer quantity, Double price, Date expiry) {
		this.name = name;
		this.categoryId = categoryId;
		this.quantity = quantity;
		this.price = price;
		this.expiry = expiry;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
}
