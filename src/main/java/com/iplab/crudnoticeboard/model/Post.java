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
    public Post() {
    }

    // Getter Setter
    public int getId() {
        return ;
    }

    public void setId(int id) {
        ;
    }

    public String getTitle() {
        return ;
    }

    public void setTitle(String title) {
        ;
    }

    public String getContent() {
        return ;
    }

    public void setContent(String content) {
        ;
    }

    public String getNickname() {
        return ;
    }

    public void setNickname(String nickname) {
        ;
    }

    public String getPassword() {
        return ;
    }

    public void setPassword(String password) {
        ;
    }

    public Date getCreatedAt() {
        return ;
    }

    public void setCreatedAt(Date createdAt) {
        ;
    }

    public boolean isLocked() {
        return ;
    }

    public void setLocked(boolean locked) {
        ;
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
