package ru.kpfu.itis.amirova.dto;

import java.time.LocalDateTime;

public class NoteDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isPublic;
    private String authorUsername;

    public NoteDto() {}

    public NoteDto(Long id, String title, String content, LocalDateTime createdAt,
                   Boolean isPublic, String authorUsername) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.authorUsername = authorUsername;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
}