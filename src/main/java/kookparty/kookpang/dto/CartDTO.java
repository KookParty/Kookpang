package kookparty.kookpang.dto;

import java.time.LocalDateTime;

public class CartDTO {
	private long cartId;
	private long userId;
	private long productId;
	private int count;
	private String createdAt;

	public CartDTO() {

	}

	/**
	 * select용 생성자
	 * 
	 * @param cartId
	 * @param userId
	 * @param productId
	 * @param count
	 * @param createdAt
	 */
	public CartDTO(long cartId, long userId, long productId, int count, String createdAt) {
		this.cartId = cartId;
		this.userId = userId;
		this.productId = productId;
		this.count = count;
		this.createdAt = createdAt;
	}

	/**
	 * insert용 생성자
	 * @param userId
	 * @param productId
	 * @param count
	 */
	public CartDTO(long userId, long productId, int count) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.count = count;
	}

	
	/**
	 * update용 생성자
	 * @param cartId
	 * @param count
	 */
	public CartDTO(long cartId, int count) {
		super();
		this.cartId = cartId;
		this.count = count;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "CartDTO [cartId=" + cartId + ", userId=" + userId + ", productId=" + productId + ", count=" + count
				+ ", createdAt=" + createdAt + "]";
	}

}
