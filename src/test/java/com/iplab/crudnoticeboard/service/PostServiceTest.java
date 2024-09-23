//package com.iplab.crudnoticeboard.service;
//
//import com.iplab.crudnoticeboard.model.Post;
//import com.iplab.crudnoticeboard.service.impl.PostServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PostServiceTest {
//
//    @Mock
//    private Connection mockConnection;
//    @Mock
//    private PreparedStatement mockPreparedStatement;
//    @Mock
//    private ResultSet mockResultSet;
//
//    private PostServiceImpl postService;
//
//    @BeforeEach
//    void setUp() throws SQLException {
//        MockitoAnnotations.openMocks(this);
//        postService = new PostServiceImpl() {
//            @Override
//            protected Connection getConnection() throws SQLException {
//                return mockConnection;
//            }
//        };
//        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
//        when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);
//    }
//
//    @Test
//    void createPost() throws SQLException {
//        Post post = new Post();
//        post.setTitle("Test Title");
//        post.setContent("Test Content");
//        post.setNickname("Test Nickname");
//        post.setPassword("Test Password");
//        post.setLocked(false);
//
//        postService.createPost(post);
//
//        verify(mockPreparedStatement).setString(1, "Test Title");
//        verify(mockPreparedStatement).setString(2, "Test Content");
//        verify(mockPreparedStatement).setString(3, "Test Nickname");
//        verify(mockPreparedStatement).setString(4, "Test Password");
//        verify(mockPreparedStatement).setBoolean(5, false);
//        verify(mockPreparedStatement).executeUpdate();
//    }
//
//    @Test
//    void getAllPosts() throws SQLException {
//        when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeQuery(anyString())).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true, true, false);
//        when(mockResultSet.getInt("id")).thenReturn(1, 2);
//        when(mockResultSet.getString("title")).thenReturn("Title 1", "Title 2");
//        when(mockResultSet.getString("nickname")).thenReturn("Nickname 1", "Nickname 2");
//
//        List<Post> posts = postService.getAllPosts();
//
//        assertEquals(2, posts.size());
//        assertEquals(1, posts.get(0).getId());
//        assertEquals("Title 1", posts.get(0).getTitle());
//        assertEquals("Nickname 1", posts.get(0).getNickname());
//        assertEquals(2, posts.get(1).getId());
//        assertEquals("Title 2", posts.get(1).getTitle());
//        assertEquals("Nickname 2", posts.get(1).getNickname());
//    }
//
//    @Test
//    void getPostById() throws SQLException {
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true);
//        when(mockResultSet.getInt("id")).thenReturn(1);
//        when(mockResultSet.getString("title")).thenReturn("Test Title");
//        when(mockResultSet.getString("content")).thenReturn("Test Content");
//        when(mockResultSet.getString("nickname")).thenReturn("Test Nickname");
//        when(mockResultSet.getBoolean("is_locked")).thenReturn(false);
//
//        Post post = postService.getPostById(1);
//
//        assertNotNull(post);
//        assertEquals(1, post.getId());
//        assertEquals("Test Title", post.getTitle());
//        assertEquals("Test Content", post.getContent());
//        assertEquals("Test Nickname", post.getNickname());
//        assertFalse(post.isLocked());
//    }
//
//    @Test
//    void verifyPassword() throws SQLException {
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true);
//        when(mockResultSet.getString("password")).thenReturn("correctPassword");
//
//        assertTrue(postService.verifyPassword(1, "correctPassword"));
//        assertFalse(postService.verifyPassword(1, "wrongPassword"));
//    }
//
//    @Test
//    void updatePost() throws SQLException {
//        Post post = new Post();
//        post.setId(1);
//        post.setTitle("Updated Title");
//        post.setContent("Updated Content");
//        post.setLocked(true);
//
//        postService.updatePost(post);
//
//        verify(mockPreparedStatement).setString(1, "Updated Title");
//        verify(mockPreparedStatement).setString(2, "Updated Content");
//        verify(mockPreparedStatement).setBoolean(3, true);
//        verify(mockPreparedStatement).setInt(4, 1);
//        verify(mockPreparedStatement).executeUpdate();
//    }
//
//    @Test
//    void deletePost() throws SQLException {
//        postService.deletePost(1);
//
//        verify(mockPreparedStatement).setInt(1, 1);
//        verify(mockPreparedStatement).executeUpdate();
//    }
//}