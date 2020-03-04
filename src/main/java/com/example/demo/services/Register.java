package com.example.demo.services;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Register {
    List<User> getAllUsers();

    Integer registerUser(User user);
}