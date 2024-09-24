package com.iplab.crudnoticeboard.model;

public class Attachment {
    private int id;
    private int postId;
    private String fileName;
    private String filePath;

    public Attachment() {}

    public Attachment(int id, int postId, String fileName, String filePath) {
        this.id = id;
        this.postId = postId;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    // Getter 및 Setter 메소드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", postId=" + postId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
