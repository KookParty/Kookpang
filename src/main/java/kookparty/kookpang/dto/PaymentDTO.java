package kookparty.kookpang.dto;

public class PaymentDTO {
	private String OrderName;
	private String cid;
	private String tid;
	private String partnerOrderId;
	private String partnerUserId;
	private String pgToken;
	private int totalAmount;
	private int deliveryFee;
	private int usedPoint;
	public PaymentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 카카오페이 세션저장용
	 * @param orderName
	 * @param cid
	 * @param partnerOrderId
	 * @param partnerUserId
	 * @param totalAmount
	 * @param deliveryFee
	 * @param usedPoint
	 */
	public PaymentDTO(String orderName, String cid, String partnerOrderId, String partnerUserId, int totalAmount, int deliveryFee, int usedPoint) {
		this.OrderName = orderName;
		this.cid = cid;
		this.partnerOrderId = partnerOrderId;
		this.partnerUserId = partnerUserId;
		this.totalAmount = totalAmount;
		this.deliveryFee = deliveryFee;
		this.usedPoint = usedPoint;
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
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}


	public int getDeliveryFee() {
		return deliveryFee;
	}


	public void setDeliveryFee(int deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	
	

	public int getUsedPoint() {
		return usedPoint;
	}


	public void setUsedPoint(int usedPoint) {
		this.usedPoint = usedPoint;
	}


	@Override
	public String toString() {
		return "PaymentDTO [OrderName=" + OrderName + ", cid=" + cid + ", tid=" + tid + ", partnerOrderId="
				+ partnerOrderId + ", partnerUserId=" + partnerUserId + ", pgToken=" + pgToken + ", totalAmount="
				+ totalAmount + ", deliveryFee=" + deliveryFee + "]";
	}
	
	
	
	
	
}
