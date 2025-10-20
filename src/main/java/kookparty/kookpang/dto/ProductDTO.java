package kookparty.kookpang.dto;

import java.time.LocalDateTime;

public class ProductDTO {
	private long productId;
	private String name;
	private int price;
	private String description;
	private String category;
	private String brand;
	private String imageUrl;
	private String createdAt;
	
	

	public ProductDTO() {
		
	}
	
	
	/**
	 * insert용 생성자
	 * @param name
	 * @param price
	 * @param description
	 * @param category
	 * @param brand
	 * @param imageUrl
	 */
	public ProductDTO(String name, int price, String description, String category, String brand, String imageUrl) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.brand = brand;
		this.imageUrl = imageUrl;
	}


	/**
	 * select용 생성자
	 * @param productId
	 * @param name
	 * @param price
	 * @param description
	 * @param category
	 * @param brand
	 * @param imageUrl
	 * @param createdAt
	 */
	public ProductDTO(long productId, String name, int price, String description, String category, String brand,
			String imageUrl, String createdAt) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.brand = brand;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}
	
	
	/**
	 * update용 생성자
	 * @param productId
	 * @param name
	 * @param price
	 * @param description
	 * @param category
	 * @param brand
	 * @param imageUrl
	 */
	public ProductDTO(long productId, String name, int price, String description, String category, String brand,
			String imageUrl) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.brand = brand;
		this.imageUrl = imageUrl;
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", name=" + name + ", price=" + price + ", description="
				+ description + ", category=" + category + ", brand=" + brand + ", imageUrl=" + imageUrl
				+ ", createdAt=" + createdAt + "]";
	}
	
	

}
