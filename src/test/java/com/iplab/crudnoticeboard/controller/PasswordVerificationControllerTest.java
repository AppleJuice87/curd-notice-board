package com.iplab.crudnoticeboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iplab.crudnoticeboard.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PasswordVerificationControllerTest {

    @Mock
    private PostServiceImpl mockPostService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;

    private PasswordVerificationController passwordVerificationController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordVerificationController = new PasswordVerificationController(mockPostService);
        objectMapper = new ObjectMapper();
    }

    @Test
    void doPost() throws Exception {
        when(mockRequest.getPathInfo()).thenReturn("/1");
        when(mockRequest.getParameter("password")).thenReturn("correctPassword");
        when(mockPostService.verifyPassword(1, "correctPassword")).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        passwordVerificationController.doPost(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        String responseJson = stringWriter.toString();
        boolean result = objectMapper.readValue(responseJson, Boolean.class);
        assertEquals(true, result);
    }

    @Test
    void doPost_incorrectPassword() throws Exception {
        when(mockRequest.getPathInfo()).thenReturn("/1");
        when(mockRequest.getParameter("password")).thenReturn("wrongPassword");
        when(mockPostService.verifyPassword(1, "wrongPassword")).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        passwordVerificationController.doPost(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        String responseJson = stringWriter.toString();
        boolean result = objectMapper.readValue(responseJson, Boolean.class);
        assertEquals(false, result);
    }

    @Test
    void doPost_sqlException() throws Exception {
        when(mockRequest.getPathInfo()).thenReturn("/1");
        when(mockRequest.getParameter("password")).thenReturn("password");
        when(mockPostService.verifyPassword(1, "password")).thenThrow(new SQLException("Database error"));

        passwordVerificationController.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
    }
}