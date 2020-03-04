package com.example.demo.dao;

import com.example.demo.model.Logging;
import com.example.demo.model.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    SqlSession session;
    @Autowired
    Logging logging;

    public List<User> getUserList() {
        logging.info(session.toString());
        logging.info("after session.......");
        List<User> userlist = session.selectList("getUserList");
        logging.info(userlist.toString());
        session.commit();
        return userlist;
    }

    public Integer addUser(User user) {
        logging.info("userDao Added: " + user.toString());
        Integer flg = -1;
        if(getUser(user) == null)
            flg = session.insert("addUser", user);
            session.commit();

        return flg;
    }

    public User getUser(User user) {
        logging.info("check user exist in dao");
        User userRecord = session.selectOne("getUser", user);
        session.commit();
        return userRecord;
    }
}
