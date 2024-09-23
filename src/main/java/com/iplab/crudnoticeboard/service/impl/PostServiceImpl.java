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
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getNickname());
            pstmt.setString(4, post.getPassword());
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

        // 게시판 특성상 날짜 데이터와 잠김 유무를 불러 올 수 있게 추가로 쿼리문을 작성하였습니다.
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id, title, nickname, created_at, is_locked FROM posts";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setNickname(rs.getString("nickname"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setLocked(rs.getBoolean("is_locked"));
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
                    post.setPassword(rs.getString("password"));
                    post.setLocked(rs.getBoolean("is_locked"));
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
                    return password.equals(rs.getString("password"));
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

        String sql = "UPDATE posts SET title = ?, content = ?, nickname = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getNickname());
            pstmt.setInt(4, post.getId());

            // executeUpdate() 메소드는 Update, Delete, Insert 구문에 영향을 받은 row를 counting 하여 반환합니다.
            int result = pstmt.executeUpdate();

            // 반환 받은 결과를 임시로 출력 해보았습니다.
            System.out.println(result);
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
            pstmt.setInt(1, id);

            // 쿼리 결과를 출력하기 위해서 int 형으로 받아서 임시로 출력해보았습니다.
            int result = pstmt.executeUpdate();
            System.out.println(result);
        }
    }

    // test용
//    protected Connection getConnection() throws SQLException {
//        // 실제 데이터베이스 연결 로직 구현
//        return DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "username", "password");
//    }
}
