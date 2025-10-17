package kookparty.kookpang.dto;

import com.google.gson.annotations.SerializedName;

import kookparty.kookpang.common.RecipeType;

/**
 * 레시피의 정보를 저장하는 DTO
 * API로부터 정보를 가져올 때 JSON 키 이름과 멤버필드 이름 매핑을 위해 @SerializedName 사용
 */
public class RecipeDTO {
	private long recipeId;
	private long userId;
	@SerializedName("RCP_NM")
	private String title;
	private String description;
	@SerializedName("ATT_FILE_NO_MAIN")
	private String thumbnailUrl;
	private RecipeType recipeType;
	@SerializedName("RCP_WAY2")
	private String way; // 조리 방법
	@SerializedName("RCP_PAT2")
	private String category; // 요리 종류
	private long parentRecipeId;
	private String createdAt;
	
	public RecipeDTO() {}
	
	/**
	 * 회원의 변형 레시피 등록 시
	 * recipeType: RecipeType.VARIANT
	 */
	public RecipeDTO(long userId, String title, String description, String thumbnailUrl, String way, String category,
			long parentRecipeId) {
		super();
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
		this.recipeType = RecipeType.VARIANT;
		this.way = way;
		this.category = category;
		this.parentRecipeId = parentRecipeId;
	}

	/**
	 * 모든 멤버필드 들어간 생성자
	 */
	public RecipeDTO(long recipeId, long userId, String title, String description, String thumbnailUrl,
			RecipeType recipeType, String way, String category, long parentRecipeId, String createdAt) {
		super();
		this.recipeId = recipeId;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.thumbnailUrl = thumbnailUrl;
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
