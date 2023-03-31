package com.demo.account.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Income {
    private int incomeId;
    private int uid;
    private int bookkeepingId;
    private int accountDetailId;
    private String amount;
    private Timestamp time;
    private String fundId;
    private String customedFundId;
    private String comment;
    private String enclosure;
}
