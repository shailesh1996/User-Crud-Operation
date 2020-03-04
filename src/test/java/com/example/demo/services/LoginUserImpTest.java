package com.example.demo.services;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserImpTest {
    public static final Jedis jedis = new Jedis("localhost", 6379);
    private JSONObject jsonObject;


    @Before
    public void setUp() {
        jsonObject = new JSONObject();
    }
    @InjectMocks
     LoginUserImp loginUserImp;
    @Mock
    UserDao userDao;

    @Test
    public void login() throws JSONException, UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        jsonObject.put("user_id", 5);
        jsonObject.put("password", "Singh");
        User userDetails = new Gson().fromJson(jsonObject.toString(), User.class);
        when(userDao.getUser(userDetails)).thenReturn(userDetails);
        loginUserImp.login(userDetails, request, response);
    }

    @Test
    public void loginRedisDelete() throws JSONException, UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        jedis.del("token");
        jsonObject.put("user_id", 5);
        jsonObject.put("password", "Singh");
        User userDetails = new Gson().fromJson(jsonObject.toString(), User.class);
        when(userDao.getUser(userDetails)).thenReturn(userDetails);
       // assert(userDetails, loginUserImp.login(userDetails, request, response));
    }

    @Test
    public void loginWrongCred() throws JSONException, UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        jsonObject.put("user_id", 5);
        jsonObject.put("password", "SinghShailesh");
        User userDetails = new Gson().fromJson(jsonObject.toString(), User.class);
        when(userDao.getUser(userDetails)).thenReturn(userDetails);
        loginUserImp.login(userDetails, request, response);
    }
}