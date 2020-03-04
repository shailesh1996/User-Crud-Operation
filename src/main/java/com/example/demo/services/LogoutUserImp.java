package com.example.demo.services;

import com.example.demo.model.Logging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class LogoutUserImp implements LogoutUser {
    public static final Jedis jedis = new Jedis("localhost", 6379);

    private String token = "token";

    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie[] cookie = request.getCookies();
        for (int i = 0; i < cookie.length; i++) {
            log.info(cookie[i].getName());
            log.info(cookie[i].getValue());
            if (cookie[i].getName().equals(token)) {
                tokenValue = cookie[i].getValue();
                log.info("tkn:: " + tokenValue);
                break;
            }
        }
        if (tokenValue != null) {
            Cookie c = new Cookie(token, "123");
            c.setMaxAge(0);
            c.setHttpOnly(true);
            response.addCookie(c);
        }
        if(jedis.get(token) != null)
            jedis.del(token);
        return "User Successfully Logout";
    }

}
