package com.iplab.crudnoticeboard.controller;

import com.iplab.crudnoticeboard.service.impl.PostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/verify-password/*")
public class PasswordVerificationController extends HttpServlet {
    private PostServiceImpl postService;

    public PasswordVerificationController(PostServiceImpl postService) {
        this.postService = postService;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo().substring(1));
        String password = req.getParameter("password");
        try {
            boolean isValid = postService.verifyPassword(id, password);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), isValid);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
