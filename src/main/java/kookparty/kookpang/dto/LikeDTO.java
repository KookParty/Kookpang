package kookparty.kookpang.dto;

import kookparty.kookpang.common.TargetType;

public class LikeDTO {
	private long likeId;
	private long userId;
	private TargetType targetType;
	private long targetId;
	private String createdAt;
	
	public LikeDTO() {}
	
	/**
	 * 좋아요 등록 시 사용
	 */
	public LikeDTO(long userId, TargetType targetType, long targetId) {
		this.userId = userId;
		this.targetType = targetType;
		this.targetId = targetId;
	}

	/**
	 * 모든 멤버필드 들어간 생성자
	 */
	public LikeDTO(long likeId, long userId, TargetType targetType, long targetId, String createdAt) {
		super();
		this.likeId = likeId;
		this.userId = userId;
		this.targetType = targetType;
		this.targetId = targetId;
		this.createdAt = createdAt;
	}

	public long getLikeId() {
		return likeId;
	}

	public void setLikeId(long likeId) {
		this.likeId = likeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
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
		builder.append("LikeDTO [likeId=");
		builder.append(likeId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", targetType=");
		builder.append(targetType);
		builder.append(", targetId=");
		builder.append(targetId);
		builder.append(", createdAt=");
		builder.append(createdAt);
		builder.append("]");
		return builder.toString();
	}
	
}
