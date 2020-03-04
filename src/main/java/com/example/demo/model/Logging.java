package com.example.demo.model;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Logging {
    private static final Logger log = LoggerFactory.getLogger(Logging.class);

    public static Logger getLog() {
        return log;
    }

    public void debug(String s) {
        log.debug(s);
    }

    public void warn(String s) {
        log.warn(s);
    }

    public void trace(String s) {
        log.trace(s);
    }

    public void error(String s) {
        log.error(s);
    }

    public void info(String s) {
        log.info(s);
    }

    @Component
    @Data
    public static class UserMapper {
        private String email;
        private String phoneNumber;
        private String lname;
        private String fname;
        private String pswd;
    }
}
