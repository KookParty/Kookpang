package kookparty.kookpang.dto;

public class IngredientDTO {
	private long ingredientId;
	private long recipeId;
	private long productId;	// 보류
	private String name;
	private String quantity;
	
	public IngredientDTO() {}

	/**
	 * products 와 매핑X (API로 레시피 데이터 불러올 시)
	 */
	public IngredientDTO(long recipeId, String name, String quantity) {
		super();
		this.recipeId = recipeId;
		this.name = name;
		this.quantity = quantity;
	}

	/**
	 * products 와 매핑O (레시피 등록 시)
	 */
	public IngredientDTO(long recipeId, long productId, String name, String quantity) {
		super();
		this.recipeId = recipeId;
		this.productId = productId;
		this.name = name;
		this.quantity = quantity;
	}

	public IngredientDTO(long ingredientId, long recipeId, long productId, String name, String quantity) {
		super();
		this.ingredientId = ingredientId;
		this.recipeId = recipeId;
		this.productId = productId;
		this.name = name;
		this.quantity = quantity;
	}

	public long getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(long recipeId) {
		this.recipeId = recipeId;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IngredientDTO [ingredientId=");
		builder.append(ingredientId);
		builder.append(", recipeId=");
		builder.append(recipeId);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append("]");
		return builder.toString();
	}
}
