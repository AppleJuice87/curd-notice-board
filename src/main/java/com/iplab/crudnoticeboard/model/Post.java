package com.iplab.crudnoticeboard.model;

import java.util.Date;

public class Post {
    private int id;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private Date createdAt;
    private boolean locked;



    // 기본 생성자
    public Post() {}

    // 모든 필드를 포함하는 생성자 (필드 순서대로)
    public Post(int id, String title, String content, String nickname, String password, Date createdAt, boolean locked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
        this.locked = locked;
    }

    // Getter Setter
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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
                '}';
    }
}
