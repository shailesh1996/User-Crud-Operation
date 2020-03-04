package com.example.demo.services;

import com.example.demo.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface LoginUser {
    String login(User user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
}
