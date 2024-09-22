package com.iplab.crudnoticeboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iplab.crudnoticeboard.model.Post;
import com.iplab.crudnoticeboard.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostServiceImpl mockPostService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;

    private PostController postController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postController = new PostController();
        postController.setPostService(mockPostService);
        objectMapper = new ObjectMapper();
    }

    @Test
    void doPost() throws Exception {
        Post post = new Post();
        post.setTitle("Test Title");
        post.setContent("Test Content");

        String jsonPost = objectMapper.writeValueAsString(post);
        BufferedReader reader = new BufferedReader(new StringReader(jsonPost));
        when(mockRequest.getReader()).thenReturn(reader);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        postController.doPost(mockRequest, mockResponse);

        verify(mockPostService).createPost(any(Post.class));
        verify(mockResponse).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void doGet_getAllPosts() throws Exception {
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(mockPostService.getAllPosts()).thenReturn(posts);
        when(mockRequest.getPathInfo()).thenReturn("/");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        postController.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        String responseJson = stringWriter.toString();
        List<Post> responsePosts = objectMapper.readValue(responseJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
        assertEquals(2, responsePosts.size());
    }

    @Test
    void doGet_getPostById() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setTitle("Test Title");
        when(mockPostService.getPostById(1)).thenReturn(post);
        when(mockRequest.getPathInfo()).thenReturn("/1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        postController.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        String responseJson = stringWriter.toString();
        Post responsePost = objectMapper.readValue(responseJson, Post.class);
        assertEquals(1, responsePost.getId());
        assertEquals("Test Title", responsePost.getTitle());
    }

    @Test
    void doPut() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setTitle("Updated Title");
        post.setPassword("correctPassword");

        String jsonPost = objectMapper.writeValueAsString(post);
        BufferedReader reader = new BufferedReader(new StringReader(jsonPost));
        when(mockRequest.getReader()).thenReturn(reader);
        when(mockRequest.getPathInfo()).thenReturn("/1");

        when(mockPostService.verifyPassword(1, "correctPassword")).thenReturn(true);

        postController.doPut(mockRequest, mockResponse);

        verify(mockPostService).updatePost(any(Post.class));
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doDelete() throws Exception {
        when(mockRequest.getPathInfo()).thenReturn("/1");
        when(mockRequest.getParameter("password")).thenReturn("correctPassword");
        when(mockPostService.verifyPassword(1, "correctPassword")).thenReturn(true);

        postController.doDelete(mockRequest, mockResponse);

        verify(mockPostService).deletePost(1);
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
    }
}