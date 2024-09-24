package com.iplab.crudnoticeboard.model;

import java.util.Date;
import java.util.List;

public class Post {
    private int id;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private Date createdAt;
    private boolean locked;
    private List<Attachment> attachments;

    // 기본 생성자
    public Post() {}

    // 모든 필드를 포함하는 생성자
    public Post(int id, String title, String content, String nickname, String password, Date createdAt, boolean locked, List<Attachment> attachments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
        this.locked = locked;
        this.attachments = attachments;
    }

    // Getter 및 Setter 메소드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    // toString 메소드 (디버깅 및 로깅에 유용)
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", nickname='" + nickname + '\'' +
                ", createdAt=" + createdAt +
                ", locked=" + locked +
                ", attachments=" + attachments +
                '}';
    }
}
