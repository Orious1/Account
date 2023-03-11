package com.demo.account.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BookKeeping {
    private int bookkeeping_id;
    private int uid;
    private int bookkeeping_type_id;
    private String bookkeeping_cover;
    private String bookkeeping_name;
    private String bookkeeping_period;
    private Timestamp bookkeeping_create_date;
    private Timestamp bookkeeping_end_date;
    private String customed_funds_id;
    private int extra_member1;
    private int extra_member2;
}
