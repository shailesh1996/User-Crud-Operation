package com.example.demo.services;

import com.example.demo.dao.UserDao;
import com.example.demo.model.Logging;
import com.example.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class RegisterImp implements Register {
    @Autowired
    UserDao userDao;

    public List<User> getAllUsers() {
        log.info("Enter register user class");
        return userDao.getUserList();
    }

    public Integer registerUser(User user) {
        log.info("registerImp:: ");
        return userDao.addUser(user);
    }


}