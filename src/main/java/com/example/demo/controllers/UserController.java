package com.example.demo.controllers;

import com.example.demo.model.RouteConstants;
import com.example.demo.model.User;
import com.example.demo.services.LoginUser;
import com.example.demo.services.LogoutUser;
import com.example.demo.services.Register;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private Register register;
    @Autowired
    private LoginUser loginUser;
    @Autowired
    private LogoutUser logoutUser;

    @GetMapping(value = RouteConstants.home)
    public @ResponseBody
    String home() {
        log.info("home User");
        return "home";
    }

    @GetMapping(value = RouteConstants.getUser)
    public @ResponseBody
    List<User> getUsers() {
        log.info("Enter here");
        return register.getAllUsers();
    }

    @PostMapping(value = RouteConstants.addUser)
    public @ResponseBody
    Integer addUser(@RequestBody @Valid User userInfo) {
        log.info("user controller");
        return register.registerUser(userInfo);
    }

    @PostMapping(value = RouteConstants.login, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String login(@RequestBody @Valid User user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info(user.toString());
        return loginUser.login(user, request, response);
    }

    @GetMapping(value = RouteConstants.logout, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("logout Controller");
        return logoutUser.logout(request, response);
    }


}