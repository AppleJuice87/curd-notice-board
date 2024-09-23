package com.iplab.crudnoticeboard.service.impl;

import com.iplab.crudnoticeboard.config.DatabaseConfig;
import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.service.PostService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    public void createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (title, content, nickname, password, is_locked) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getNickname());
            pstmt.setString(4, post.getPassword());
            pstmt.setBoolean(5, post.isLocked());
            pstmt.executeUpdate();
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
        }
    }

    public void deletePost(int id) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

//    protected Connection getConnection() throws SQLException {
//        // 실제 데이터베이스 연결 로직 구현
//        return DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "username", "password");
//    }
}
