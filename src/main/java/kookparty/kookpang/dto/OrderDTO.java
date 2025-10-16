package kookparty.kookpang.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
	private long orderId;
	private long userId;
	private LocalDateTime createdAt;
	private int totalPrice;
	private String shippingAddress;
	private boolean status;
	private List<OrderItemDTO> itemList;

	public OrderDTO() {

	}

	/**
	 * select용 생성자
	 * 
	 * @param orderId
	 * @param userId
	 * @param createdAt
	 * @param totalPrice
	 * @param shippingAddress
	 * @param status
	 * @param itemList
	 */
	public OrderDTO(long orderId, long userId, LocalDateTime createdAt, int totalPrice, String shippingAddress,
			boolean status, List<OrderItemDTO> itemList) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.createdAt = createdAt;
		this.totalPrice = totalPrice;
		this.shippingAddress = shippingAddress;
		this.status = status;
		this.itemList = itemList;
	}

	/**
	 * insert용 생성자
	 * 
	 * @param userId
	 * @param totalPrice
	 * @param shippingAddress
	 * @param status
	 * @param itemList
	 */
	public OrderDTO(long userId, int totalPrice, String shippingAddress, boolean status, List<OrderItemDTO> itemList) {
		super();
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.shippingAddress = shippingAddress;
		this.status = status;
		this.itemList = itemList;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<OrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "OrderDTO [orderId=" + orderId + ", userId=" + userId + ", createdAt=" + createdAt + ", totalPrice="
				+ totalPrice + ", shippingAddress=" + shippingAddress + ", status=" + status + ", itemList=" + itemList
				+ "]";
	}

}
