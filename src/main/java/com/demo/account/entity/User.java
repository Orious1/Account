package com.demo.account.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private int uid;
    private String userName;
    private String wxId;
    private Timestamp registerDate;

    public User(String userName, String wxId) {
        this.userName = userName;
        this.wxId = wxId;
    }
}
