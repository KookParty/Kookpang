package kookparty.kookpang.dto;

public class StepDTO {
	private long stepId;
	private String description;
	private String imageUrl;
	
	public StepDTO() {}
	public StepDTO(String description, String imageUrl) {
		super();
		this.description = description;
		this.imageUrl = imageUrl;
	}

	public StepDTO(long stepId, String description, String imageUrl) {
		super();
		this.stepId = stepId;
		this.description = description;
		this.imageUrl = imageUrl;
	}
	
	public long getStepId() {
		return stepId;
	}
	public void setStepId(long stepId) {
		this.stepId = stepId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StepDTO [stepId=");
		builder.append(stepId);
		builder.append(", description=");
		builder.append(description);
		builder.append(", imageUrl=");
		builder.append(imageUrl);
		builder.append("]\n");
		return builder.toString();
	}
}
