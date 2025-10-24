package kookparty.kookpang.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BoardDTO {
    private Long postId;
    private Long userId;
    private String category; // notice | free
    private String title;
    private String content;
    private long viewCount;
    private long commentCount;
    private LocalDateTime createdAt;
    private String nickname;              // users.nickname
    private List<Image> images;           // post_images
    private List<Comment> comments;       // comments

    // --- nested types ---
    public static class Image {
        private Long imageId;
        private Long postId;
        private String imageUrl;
        private int imageOrder;
        public Long getImageId() { return imageId; }
        public void setImageId(Long v) { imageId = v; }
        public Long getPostId() { return postId; }
        public void setPostId(Long v) { postId = v; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String v) { imageUrl = v; }
        public int getImageOrder() { return imageOrder; }
        public void setImageOrder(int v) { imageOrder = v; }
    }
    public static class Comment {
        private Long commentId;
        private Long postId;
        private Long userId;
        private String content;
        private String nickname; // users.nickname
        private LocalDateTime createdAt;
        public Long getCommentId() { return commentId; }
        public void setCommentId(Long v) { commentId = v; }
        public Long getPostId() { return postId; }
        public void setPostId(Long v) { postId = v; }
        public Long getUserId() { return userId; }
        public void setUserId(Long v) { userId = v; }
        public String getContent() { return content; }
        public void setContent(String v) { content = v; }
        public String getNickname() { return nickname; }
        public void setNickname(String v) { nickname = v; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime v) { createdAt = v; }
    }

    // --- getters/setters ---
    public Long getPostId() { return postId; }
    public void setPostId(Long v) { postId = v; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { userId = v; }
    public String getCategory() { return category; }
    public void setCategory(String v) { category = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { title = v; }
    public String getContent() { return content; }
    public void setContent(String v) { content = v; }
    public long getViewCount() { return viewCount; }
    public void setViewCount(long v) { viewCount = v; }
    public long getCommentCount() { return commentCount; }
    public void setCommentCount(long v) { commentCount = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { createdAt = v; }
    public String getNickname() { return nickname; }
    public void setNickname(String v) { nickname = v; }
    public List<Image> getImages() { return images; }
    public void setImages(List<Image> v) { images = v; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> v) { comments = v; }
}
