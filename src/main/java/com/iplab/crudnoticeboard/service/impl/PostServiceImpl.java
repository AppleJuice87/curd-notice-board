package com.iplab.crudnoticeboard.service.impl;

import com.iplab.crudnoticeboard.config.DatabaseConfig;
import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.model.Attachment;
import com.iplab.crudnoticeboard.service.PostService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    public void createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (title, content, nickname, password, is_locked) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getNickname());
            pstmt.setString(4, post.getPassword());
            pstmt.setBoolean(5, post.isLocked());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int postId = generatedKeys.getInt(1);
                    for (Attachment attachment : post.getAttachments()) {
                        saveAttachment(postId, attachment);
                    }
                }
            }
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id, title, nickname FROM posts";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setNickname(rs.getString("nickname"));
                posts.add(post);
            }
        }
        return posts;
    }

    public Post getPostById(int id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setNickname(rs.getString("nickname"));
                    post.setCreatedAt(rs.getDate("created_at"));
                    post.setLocked(rs.getBoolean("is_locked"));
                    post.setAttachments(getAttachmentsByPostId(id));
                    return post;
                }
            }
        }
        return null;
    }

    public boolean verifyPassword(int id, String password) throws SQLException {
        String sql = "SELECT password FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return password.equals(rs.getString("password"));
                }
            }
        }
        return false;
    }

    public void updatePost(Post post) throws SQLException {
        String sql = "UPDATE posts SET title = ?, content = ?, is_locked = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setBoolean(3, post.isLocked());
            pstmt.setInt(4, post.getId());
            pstmt.executeUpdate();

            // 기존 첨부파일 삭제
            deleteAttachmentsByPostId(post.getId());

            // 새 첨부파일 저장
            for (Attachment attachment : post.getAttachments()) {
                saveAttachment(post.getId(), attachment);
            }
        }
    }

    public void deletePost(int id) throws SQLException {
        // 첨부파일 먼저 삭제
        deleteAttachmentsByPostId(id);

        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void saveAttachment(int postId, Attachment attachment) throws SQLException {
        String sql = "INSERT INTO attachments (post_id, file_name, file_path) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setString(2, attachment.getFileName());
            pstmt.setString(3, attachment.getFilePath());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Attachment> getAttachmentsByPostId(int postId) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        String sql = "SELECT * FROM attachments WHERE post_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Attachment attachment = new Attachment();
                    attachment.setId(rs.getInt("id"));
                    attachment.setPostId(rs.getInt("post_id"));
                    attachment.setFileName(rs.getString("file_name"));
                    attachment.setFilePath(rs.getString("file_path"));
                    attachments.add(attachment);
                }
            }
        }
        return attachments;
    }

    @Override
    public void deleteAttachment(int attachmentId) throws SQLException {
        String sql = "DELETE FROM attachments WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, attachmentId);
            pstmt.executeUpdate();
        }
    }

    private void deleteAttachmentsByPostId(int postId) throws SQLException {
        String sql = "DELETE FROM attachments WHERE post_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        }
    }
}
