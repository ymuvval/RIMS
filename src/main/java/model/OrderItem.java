package model;

public class OrderItem {
	private Integer id;
	private Integer order_id;
	private Integer item_id;
	private Double price;
	private Integer quantity;
	
	public OrderItem() {
		super();
	}
	
	public OrderItem(Integer orderId, Integer itemId, Double price, Integer quantity) {
		this.order_id = orderId;
		this.item_id = itemId;
		this.price = price;
		this.quantity = quantity;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public OrderItem Clone() { 
		OrderItem oi = new OrderItem();
		oi.setId(this.getId());
		oi.setItem_id(this.getItem_id());
		oi.setOrder_id(this.getOrder_id());
		oi.setQuantity(this.getQuantity());
		oi.setPrice(this.getPrice());
		return oi;
	}
}
