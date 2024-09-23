package com.iplab.crudnoticeboard.service.impl;

import com.iplab.crudnoticeboard.config.DatabaseConfig;
import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.service.PostService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    public void createPost(Post post) throws SQLException {
        // TODO #3-1 : 게시물 생성 로직 구현
        // - SQL 쿼리 준비
        // - 데이터베이스 연결 - try-with-resources 사용
        // - PreparedStatement 설정
        // - 쿼리 실행 및 리소스 정리


        // TODO #3-1-1 : Connection 생성하는법. config에서 가져옴.
        // Connection conn = DatabaseConfig.getConnection();
        
        String sql = "INSERT INTO posts (title, content, nickname, password, is_locked, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());

            // 추가 #1 : content
            pstmt.setString(2, post.getContent());

            // 추가 #1 : nickname
            pstmt.setString(3, post.getNickname());

            // 추가 #1 : password
            pstmt.setString(4, post.getPassword());

            // 추가 #1 : is_locked
            pstmt.setBoolean(5, post.isLocked());

            pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // 현재 시간 추가
            pstmt.executeUpdate();
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        // TODO #3-2 : 모든 게시물 조회 로직 구현
        // - SQL 쿼리 준비
        // - 데이터베이스 연결 - try-with-resources 사용
        // - Statement 생성 및 쿼리 실행
        // - ResultSet 처리
        // - 결과 반환
        
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id, title, nickname FROM posts";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Post post = new Post();

                // 추가 : id
                post.setId(rs.getInt("id"));

                // 추가 : title
                post.setTitle(rs.getString("title"));

                // 추가 : nickname
                post.setNickname(rs.getString("nickname"));
                
                posts.add(post);
            }
        }
        return posts;
    }

    public Post getPostById(int id) throws SQLException {
        // TODO #3-3 : 특정 게시물 조회 로직 구현
        // - SQL 쿼리 준비
        // - 데이터베이스 연결 - try-with-resources 사용
        // - PreparedStatement 설정
        // - 쿼리 실행 및 ResultSet 처리
        // - 결과 반환

        String sql = "SELECT id, title, content, nickname, " +
                "password, is_locked, created_at posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post();

                    // 추가 : id
                    post.setId(rs.getInt("id"));

                    // 추가 : title
                    post.setTitle(rs.getString("title"));

                    // 추가 : nickname
                    post.setNickname(rs.getString("nickname"));

                    // 추가 : content
                    post.setContent(rs.getString("content"));


                    // 추가 : password
                    post.setPassword(rs.getString("password"));

                    // 추가 : is_locked
                    post.setLocked(rs.getBoolean("is_locked"));

                    // 추가 : created_at
                    post.setCreatedAt(rs.getTimestamp("created_at"));

                    return post;
                }
            }
        }
        return null;
    }

    public boolean verifyPassword(int id, String password) throws SQLException {
        // TODO #3-4 : 비밀번호 검증 로직 구현
        // - SQL 쿼리 준비
        // - 데이터베이스 연결 - try-with-resources 사용
        // - PreparedStatement 설정
        // - 쿼리 실행 및 ResultSet 처리
        // - 결과 반환

        String sql = "SELECT password FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String real_password = rs.getString("password");
                    // 비밀번호 일치한지 확인 (.equals)
                    return real_password.equals(password);
                }
            }
        }
        return false;
    }

    public void updatePost(Post post) throws SQLException {
        // TODO #3-5 : 게시물 업데이트 로직 구현
        // - SQL 쿼리 준비
        // - 데이터베이스 연결 - try-with-resources 사용
        // - PreparedStatement 설정
        // - 쿼리 실행 및 리소스 정리

        String sql = "UPDATE posts SET title = ?, content = ?, is_locked = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 추가 : title
            pstmt.setString(1, post.getTitle());

            // 추가 : content
            pstmt.setString(2,post.getContent());

            // 추가 : is_locked
            pstmt.setBoolean(3,post.isLocked());

            // 추가 : id
            pstmt.setInt(4, post.getId());
            
            //실행
            pstmt.executeUpdate();
        }
    }

    public void deletePost(int id) throws SQLException {
        // TODO #3-6 : 게시물 삭제 로직 구현
        // - SQL 쿼리 준비
        // - 데이터베이스 연결 - try-with-resources 사용
        // - PreparedStatement 설정
        // - 쿼리 실행 및 리소스 정리
        
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 추가 : id
            pstmt.setInt(1,id);
            // 실행
            pstmt.executeUpdate();
        }
    }

    // test용
//    protected Connection getConnection() throws SQLException {
//        // 실제 데이터베이스 연결 로직 구현
//        return DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "username", "password");
//    }
}
