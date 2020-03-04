package com.example.demo.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LogoutUser {
    String logout(HttpServletRequest request, HttpServletResponse response);
}
