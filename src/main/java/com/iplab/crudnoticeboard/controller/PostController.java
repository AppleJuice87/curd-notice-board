package com.iplab.crudnoticeboard.controller;

import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.service.impl.PostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/posts/*")
public class PostController extends HttpServlet {
    private PostServiceImpl postService = new PostServiceImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void setPostService(PostServiceImpl postService) {
        this.postService = postService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Post post = objectMapper.readValue(req.getReader(), Post.class);
        try {
            postService.createPost(post);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllPosts(resp);
        } else {
            int id = Integer.parseInt(pathInfo.substring(1));
            getPostById(id, resp);
        }
    }

    private void getAllPosts(HttpServletResponse resp) throws IOException {
        try {
            List<Post> posts = postService.getAllPosts();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), posts);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void getPostById(int id, HttpServletResponse resp) throws IOException {
        try {
            Post post = postService.getPostById(id);
            if (post != null) {
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), post);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo().substring(1));
        Post post = objectMapper.readValue(req.getReader(), Post.class);
        post.setId(id);
        try {
            if (postService.verifyPassword(id, post.getPassword())) {
                postService.updatePost(post);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid password");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo().substring(1));
        String password = req.getParameter("password");
        try {
            if (postService.verifyPassword(id, password)) {
                postService.deletePost(id);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid password");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
