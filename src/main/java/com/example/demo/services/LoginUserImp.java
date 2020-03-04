package com.example.demo.services;

import com.example.demo.dao.UserDao;
import com.example.demo.model.Logging;
import com.example.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.server.UID;

@Service
@Slf4j
public class LoginUserImp implements LoginUser {
    public static final Jedis jedis = new Jedis("localhost", 6379);
    @Autowired
    UserDao userDao;

    public String login(User user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String tokenValue = null;
        String tokenRedis = null;
        String userId = user.getUser_id();
        String pswd = user.getPassword();
        String sucessMessageDb = "userId:: " + userId + " SuccessfullyLogin from Db";
        String sucessMessageRedis = "userId:: " + userId + " SuccessfullyLogin from Redis";
        String failureMessage = "Invalid User Credentials";
        log.info("login service");
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("session is null");
            Cookie[] cookie = request.getCookies();
            for (int i = 0; (cookie!=null) && (i < cookie.length) ; i++) {
                log.info(cookie[i].getName());
                log.info(cookie[i].getValue());
                if (cookie[i].getName().equals("token")) {
                    tokenValue = cookie[i].getValue();
                    log.info("tkn:: " + tokenValue);
                    break;
                }
            }

            if (tokenValue == null) { //token not stored in Cookie
                log.info("cookie is null");
                tokenRedis = jedis.get("token");
                if (tokenRedis == null) {
                    //check in db
                    log.info("token is null");
                    if (userDao.getUser(user) != null) {// If Found  create session, store in redis, add cookie
                        log.info("found");
                        tokenValue = generateSessionId();
                        setSession(request, user, tokenValue);
                        setCookie(response, tokenValue);
                        insertEntryInRedis(tokenValue, user);
                        return sucessMessageDb;
                    } else {
                        log.info("Not Found");
                        return "Invalid User";
                    }
                } else { //we can confirm validity from Redis
                    log.info("check in redis");
                    if (checkInRedis(tokenRedis, user, response)) {
                        return sucessMessageRedis;
                    } else {
                        if (userDao.getUser(user) != null) {
                            tokenValue = generateSessionId();
                            setSession(request, user, tokenValue);
                            setCookie(response, tokenValue);
                            insertEntryInRedis(tokenValue, user);
                            return sucessMessageDb;
                        } else {
                            return failureMessage;
                        }
                    }
                }
            } else {//check cookie
                log.info("check in cookie");
                log.info("token " + tokenValue);
                String userIdRedis = jedis.get(tokenValue);
                String pswdRedis = jedis.get(userIdRedis);
                if (userIdRedis != null && pswdRedis != null) { //Redis
                    log.info("check in redis");
                    if (checkInRedis(tokenValue, user, response)) {
                        return sucessMessageRedis;
                    } else {
                        if (userDao.getUser(user) != null) {
                            tokenValue = generateSessionId();
                            setSession(request, user, tokenValue);
                            setCookie(response, tokenValue);
                            insertEntryInRedis(tokenValue, user);
                            return sucessMessageDb;
                        } else {
                            return "Invalid User Credentials";
                        }
                    }
                } else { // Check in Db
                    log.info("check in Db");
                    if (userDao.getUser(user) != null) {
                        tokenValue = generateSessionId();
                        setSession(request, user, tokenValue);
                        setCookie(response, tokenValue);
                        insertEntryInRedis(tokenValue, user);
                        return sucessMessageDb;
                    } else {
                        return failureMessage;
                    }
                }
            }
        } else {
            log.info("check in session");
            String userIdSession = session.getAttribute("userId").toString();
            String pswdSession = session.getAttribute("pswd").toString();
            log.info(userIdSession);
            log.info(pswdSession);
            if ((userIdSession != null && userIdSession.equals(userId)) && (pswdSession != null && pswdSession.equals(pswd))) {
                setCookie(response, session.getAttribute("token").toString());
                insertEntryInRedis(session.getAttribute("token").toString(), user);
                return "userId:: " + userId + "successsfully Login from session";
            } else {
                log.info("uuuu : " + userDao.getUser(user));
                if (userDao.getUser(user) != null) {
                    tokenValue = generateSessionId();
                    setSession(request, user, tokenValue);
                    setCookie(response, tokenValue);
                    insertEntryInRedis(tokenValue, user);
                    return sucessMessageDb;
                } else {
                    return failureMessage;
                }
            }
        }
    }

    String generateSessionId() throws UnsupportedEncodingException {
        String id = new UID().toString();
        URLEncoder.encode(id, "UTF-8");
        return id;
    }

    boolean checkInRedis(String token, User user, HttpServletResponse response) {
        String userId = user.getUser_id();
        String pswd = user.getPassword();
        String userIdRedis = jedis.get(token);
        String pswdRedis = jedis.get(userIdRedis);
        log.info("userid: " + userIdRedis);
        log.info("pswd:: " + pswdRedis);
        if (userIdRedis.equals(userId) && pswdRedis.equals(pswd)) {
            setCookie(response, token);//check for bug
            return true;
        } else {
            return false;
        }
    }

    void setSession(HttpServletRequest request, User user, String token) throws UnsupportedEncodingException {
        String userId = user.getUser_id();
        String pswd = user.getPassword();
        HttpSession session = request.getSession();
        session.setAttribute("token", token);
        session.setAttribute("userId", userId);
        session.setAttribute("pswd", pswd);
    }

    void setCookie(HttpServletResponse response, String token) {
        Cookie c;
        log.info("tkn: " + token);
        c = new Cookie("token", token);
        c.setMaxAge(60*60);
        //c.setSecure(true);
        c.setHttpOnly(true);
        response.addCookie(c);
    }

    void insertEntryInRedis(String token, User user) {
        String userId = user.getUser_id();
        String pswd = user.getPassword();
        jedis.set("token", token);
        jedis.set(token, userId);
        jedis.set(userId, pswd);
    }
}

