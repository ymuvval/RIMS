package model;

public class Invoice {
	private Integer id;
	private Double bill_amount;
	private Double discount;
	private Double final_amount;
	
	public Invoice () {
		super();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(Double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getFinal_bill() {
		return final_amount;
	}
	public void setFinal_bill(Double final_amount) {
		this.final_amount = final_amount;
	}
	
	public Invoice Copy() {
		Invoice copy = new Invoice();
		copy.setBill_amount(this.getBill_amount());
		copy.setDiscount(this.getDiscount());
		copy.setFinal_bill(this.getFinal_bill());
		return copy;
	}
}
