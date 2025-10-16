package kookparty.kookpang.dto;

import kookparty.kookpang.common.Difficulty;
import kookparty.kookpang.common.RecipeType;

public class RecipeDTO {
	private long recipeId;
	private long userId;
	private String title;
	private String description;
	private String thumbnailUrl;
	private int cookingTime;
	private Difficulty difficulty;
	private RecipeType recipeType;
	private String way; // 조리 방법
	private String category; // 요리 종류
	private long parentRecipeId;
	private String createdAt;
	
	public RecipeDTO() {}
	
	/**
	 * 회원의 변형 레시피 작성 시 (조리방법, 요리종류 미포함)
	 * recipeType: RecipeType.VARIANT
	 */
	public RecipeDTO(long userId, String title, String description, String thumbnailUrl, int cookingTime,
			Difficulty difficulty, long parentRecipeId, String createdAt) {
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
		this.cookingTime = cookingTime;
		this.difficulty = difficulty;
		this.recipeType = RecipeType.VARIANT;
		this.parentRecipeId = parentRecipeId;
		this.createdAt = createdAt;
	}

	/** 
	 * 회원의 변형 레시피 작성 시 (조리방법, 요리종류 포함)
	 */
	public RecipeDTO(long userId, String title, String description, String thumbnailUrl, int cookingTime,
			Difficulty difficulty, String way, String category, long parentRecipeId, String createdAt) {
		this(userId, title, description, thumbnailUrl, cookingTime, difficulty, parentRecipeId, createdAt);
		this.way = way;
		this.category = category;
	}

	/**
	 * 모든 멤버필드 들어간 생성자 (fetch용)
	 */
	public RecipeDTO(long recipeId, long userId, String title, String description, String thumbnailUrl, int cookingTime,
			Difficulty difficulty, RecipeType recipeType, String way, String category, long parentRecipeId,
			String createdAt) {
		this.recipeId = recipeId;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
		this.cookingTime = cookingTime;
		this.difficulty = difficulty;
		this.recipeType = recipeType;
		this.way = way;
		this.category = category;
		this.parentRecipeId = parentRecipeId;
		this.createdAt = createdAt;
	}

	public long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(long recipeId) {
		this.recipeId = recipeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public int getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(int cookingTime) {
		this.cookingTime = cookingTime;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public RecipeType getRecipeType() {
		return recipeType;
	}

	public void setRecipeType(RecipeType recipeType) {
		this.recipeType = recipeType;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getParentRecipeId() {
		return parentRecipeId;
	}

	public void setParentRecipeId(long parentRecipeId) {
		this.parentRecipeId = parentRecipeId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecipeDTO [recipeId=");
		builder.append(recipeId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", thumbnailUrl=");
		builder.append(thumbnailUrl);
		builder.append(", cookingTime=");
		builder.append(cookingTime);
		builder.append(", difficulty=");
		builder.append(difficulty);
		builder.append(", recipeType=");
		builder.append(recipeType);
		builder.append(", way=");
		builder.append(way);
		builder.append(", category=");
		builder.append(category);
		builder.append(", parentRecipeId=");
		builder.append(parentRecipeId);
		builder.append(", createdAt=");
		builder.append(createdAt);
		builder.append("]");
		return builder.toString();
	}
	
}
