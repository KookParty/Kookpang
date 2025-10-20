package kookparty.kookpang.dto;

public class ResponseCartDTO {
	private long cartId;
	private long productId;
	private String name;
	private int price;
	private int count;
	private String imageUrl;

	public ResponseCartDTO() {

	}

	public ResponseCartDTO(long cartId, long productId, String name, int price, int count, String imageUrl) {
		this.cartId = cartId;
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.count = count;
		this.imageUrl = imageUrl;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ResponseCartDTO [cartId=" + cartId + ", name=" + name + ", price=" + price + ", count=" + count
				+ ", imageUrl=" + imageUrl + "]";
	}

}
