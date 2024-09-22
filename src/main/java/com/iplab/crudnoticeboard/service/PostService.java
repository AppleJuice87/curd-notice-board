package com.iplab.crudnoticeboard.service;

import com.iplab.crudnoticeboard.model.Post;

import java.sql.SQLException;
import java.util.List;

// TODO #2 : 읽어보기
// 게시물 관련 JDBC 로직을 처리하는 메서드를 정의
// 데이터베이스 연결 및 쿼리 실행 등의 작업을 수행

// 이 인터페이스에 대한 구현은 PostServiceImpl 클래스에서 수행

public interface PostService {
    void createPost(Post post) throws SQLException;
    List<Post> getAllPosts() throws SQLException;
    Post getPostById(int id) throws SQLException;
    boolean verifyPassword(int id, String password) throws SQLException;
    void updatePost(Post post) throws SQLException;
    void deletePost(int id) throws SQLException;
}
