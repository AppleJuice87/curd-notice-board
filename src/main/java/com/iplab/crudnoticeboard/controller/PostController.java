package com.iplab.crudnoticeboard.controller;

import com.iplab.crudnoticeboard.model.Attachment;
import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.service.impl.PostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
                Post post = new Post();
                List<Attachment> attachments = new ArrayList<>();

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        // 일반 폼 필드 처리
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("UTF-8");
                        switch (fieldName) {
                            case "title":
                                post.setTitle(fieldValue);
                                break;
                            case "content":
                                post.setContent(fieldValue);
                                break;
                            case "nickname":
                                post.setNickname(fieldValue);
                                break;
                            case "password":
                                post.setPassword(fieldValue);
                                break;
                            case "locked":
                                post.setLocked(Boolean.parseBoolean(fieldValue));
                                break;
                        }
                    } else {
                        // 파일 처리
                        String fileName = new File(item.getName()).getName();
                        String filePath = getServletContext().getRealPath("/uploads") + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);

                        Attachment attachment = new Attachment();
                        attachment.setFileName(fileName);
                        attachment.setFilePath(filePath);
                        attachments.add(attachment);
                    }
                }

                post.setAttachments(attachments);
                postService.createPost(post);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
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
        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
                Post post = new Post();
                post.setId(id);
                List<Attachment> attachments = new ArrayList<>();

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        // 일반 폼 필드 처리
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("UTF-8");
                        switch (fieldName) {
                            case "title":
                                post.setTitle(fieldValue);
                                break;
                            case "content":
                                post.setContent(fieldValue);
                                break;
                            case "password":
                                post.setPassword(fieldValue);
                                break;
                            case "locked":
                                post.setLocked(Boolean.parseBoolean(fieldValue));
                                break;
                        }
                    } else {
                        // 파일 처리
                        String fileName = new File(item.getName()).getName();
                        String filePath = getServletContext().getRealPath("/uploads") + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);

                        Attachment attachment = new Attachment();
                        attachment.setFileName(fileName);
                        attachment.setFilePath(filePath);
                        attachments.add(attachment);
                    }
                }

                post.setAttachments(attachments);
                if (postService.verifyPassword(id, post.getPassword())) {
                    postService.updatePost(post);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid password");
                }
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
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
