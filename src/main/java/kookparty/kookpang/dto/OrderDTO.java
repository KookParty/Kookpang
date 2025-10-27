package kookparty.kookpang.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
	private long orderId;
	private long userId;
	private String createdAt;
	private int totalPrice;
	private int deliveryFee;
	private int usedPoint;
	private String shippingAddress;
	private String OrderName;
	private String cid;
	private String tid;
	private String partnerOrderId;
	private String partnerUserId;
	private String pgToken;
	private boolean status;
	private List<OrderItemDTO> itemList;

	public OrderDTO() {

	}

	/**
	 * select용 생성자
	 * @param orderId
	 * @param userId
	 * @param createdAt
	 * @param totalPrice
	 * @param deliveryFee
	 * @param usedPoint
	 * @param shippingAddress
	 * @param orderName
	 * @param cid
	 * @param tid
	 * @param partnerOrderId
	 * @param partnerUserId
	 * @param pgToken
	 * @param status
	 * @param itemList
	 */
	public OrderDTO(long orderId, long userId, String createdAt, int totalPrice, int deliveryFee, int usedPoint,
			String shippingAddress, String orderName, String cid, String tid, String partnerOrderId,
			String partnerUserId, String pgToken, boolean status, List<OrderItemDTO> itemList) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.createdAt = createdAt;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
		this.usedPoint = usedPoint;
		this.shippingAddress = shippingAddress;
		OrderName = orderName;
		this.cid = cid;
		this.tid = tid;
		this.partnerOrderId = partnerOrderId;
		this.partnerUserId = partnerUserId;
		this.pgToken = pgToken;
		this.status = status;
		this.itemList = itemList;
	}

	/**
	 * insert용 생성자
	 * 
	 * @param userId
	 * @param totalPrice
	 * @param deliveryFee
	 * @param shippingAddress
	 * @param status
	 * @param itemList
	 */
	public OrderDTO(long userId, int totalPrice, int deliveryFee, int usedPoint, String shippingAddress, List<OrderItemDTO> itemList) {
		super();
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.deliveryFee = deliveryFee;
		this.usedPoint = usedPoint;
		this.shippingAddress = shippingAddress;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(int deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getOrderName() {
		return OrderName;
	}

	public void setOrderName(String orderName) {
		OrderName = orderName;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getPartnerOrderId() {
		return partnerOrderId;
	}

	public void setPartnerOrderId(String partnerOrderId) {
		this.partnerOrderId = partnerOrderId;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getPgToken() {
		return pgToken;
	}

	public void setPgToken(String pgToken) {
		this.pgToken = pgToken;
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
	
	
	
	public int getUsedPoint() {
		return usedPoint;
	}

	public void setUsedPoint(int usedPoint) {
		this.usedPoint = usedPoint;
	}

	@Override
	public String toString() {
		return "OrderDTO [orderId=" + orderId + ", userId=" + userId + ", createdAt=" + createdAt + ", totalPrice="
				+ totalPrice + ", deliveryFee=" + deliveryFee + ", usedPoint=" + usedPoint + ", shippingAddress="
				+ shippingAddress + ", OrderName=" + OrderName + ", cid=" + cid + ", tid=" + tid + ", partnerOrderId="
				+ partnerOrderId + ", partnerUserId=" + partnerUserId + ", pgToken=" + pgToken + ", status=" + status
				+ ", itemList=" + itemList + "]";
	}

	


	

	

}
