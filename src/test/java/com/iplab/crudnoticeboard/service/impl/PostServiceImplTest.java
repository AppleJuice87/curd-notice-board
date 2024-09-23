package com.iplab.crudnoticeboard.service.impl;

import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.service.PostService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {
    private final PostService postService;
    private Post post;
    private final int postId = 1;
    private final String postPassword = "Password";
    private final String postTitle = "Title";
    private final String postContent = "Content";
    private final String postNickname = "Nickname";

    PostServiceImplTest() {
        postService = new PostServiceImpl();
        post = new Post();
    }


    @Test
    void createPost() {
        post.setTitle(postTitle);
        post.setContent(postContent);
        post.setNickname(postNickname);
        post.setPassword(postPassword);

        try {
            postService.createPost(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();

            for (Post post : posts) {
                System.out.println(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getPostById() {
        try {
            Post post = postService.getPostById(postId);
            System.out.println(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void verifyPassword() {

        try {
            boolean result = postService.verifyPassword(postId, postPassword);
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updatePost() {
        post.setTitle("Title_Updated");
        post.setContent("Content_Updated");
        post.setNickname("Nickname_Updated");
        post.setId(postId);

        try {
            // 테스트 결과
            postService.updatePost(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deletePost() {
        try {
            postService.deletePost(postId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}