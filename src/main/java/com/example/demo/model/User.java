package com.example.demo.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

public @Data
class User {

    @Min(1)
    private String user_id;
    private String password;
    private String phone_number;
    private String first_name;
    private String last_name;
    @Email
    private String email;
}