package kookparty.kookpang.dto;

public class ReviewDTO {
	private long reviewId;
	private long recipeId;
	private long userId;
	private int rating;
	private String content;
	private String imageUrl;
	private String createdAt;
	
	private String nickname;

	public ReviewDTO() {}
	
	/**
	 * 리뷰 등록용 생성자
	 */
	public ReviewDTO(long recipeId, long userId, int rating, String content, String imageUrl) {
		this.recipeId = recipeId;
		this.userId = userId;
		this.rating = rating;
		this.content = content;
		this.imageUrl = imageUrl;
	}

	/**
	 * 모든 필드
	 */
	public ReviewDTO(long reviewId, long recipeId, long userId, int rating, String content, String imageUrl,
			String createdAt, String nickname) {
		this(recipeId, userId, rating, content, imageUrl);
		this.reviewId = reviewId;
		this.createdAt = createdAt;
		this.nickname = nickname;
	}

	public long getReviewId() {
		return reviewId;
	}

	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReviewDTO [reviewId=");
		builder.append(reviewId);
		builder.append(", recipeId=");
		builder.append(recipeId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", rating=");
		builder.append(rating);
		builder.append(", content=");
		builder.append(content);
		builder.append(", imageUrl=");
		builder.append(imageUrl);
		builder.append(", createdAt=");
		builder.append(createdAt);
		builder.append(", nickname=");
		builder.append(nickname);
		builder.append("]");
		return builder.toString();
	}

}
