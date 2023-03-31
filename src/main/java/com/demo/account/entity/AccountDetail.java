package com.demo.account.entity;

import lombok.Data;

@Data
public class AccountDetail {
    private Long accountDetailId;
    private Integer accountDetailType;
    private Integer accountId;
    private Integer uid;
    private String balance;
    private String budget;
    private String comment;
}
