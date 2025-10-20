package kookparty.kookpang.dto;

public class OrderItemDTO {
	private long itemId;
	private long orderId;
	private long productId;
	private int count;
	private int price; // 당시 가격 저장용

	public OrderItemDTO() {
	}

	/**
	 * select용 생성자
	 * 
	 * @param itemId
	 * @param orderId
	 * @param productId
	 * @param count
	 * @param price
	 */
	public OrderItemDTO(long itemId, long orderId, long productId, int count, int price) {
		super();
		this.itemId = itemId;
		this.orderId = orderId;
		this.productId = productId;
		this.count = count;
		this.price = price;
	}

	/**
	 * insert용 생성자
	 * 
	 * @param orderId
	 * @param productId
	 * @param count
	 * @param price
	 */
	public OrderItemDTO(long orderId, long productId, int count, int price) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.count = count;
		this.price = price;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderItemDTO [itemId=" + itemId + ", orderId=" + orderId + ", productId=" + productId + ", count="
				+ count + ", price=" + price + "]";
	}

}
