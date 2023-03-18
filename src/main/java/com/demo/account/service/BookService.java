package com.demo.account.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.BasicFund;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public interface BookService {

    HashMap<String,String> getExpenditureType(int uid,String bookKeepingName,String bookKeepingTypeName);

    HashMap<String,String> getIncomeType(int uid, String bookKeepingName, String bookKeepingTypeName);

    String outSettingChange(int uid, String bookKeepingName,String bookKeepingTypeName,JSONObject json);

    String inSettingChange(int uid, String bookKeepingName,String bookKeepingTypeName,JSONObject json);

    List<BasicFund> selectAllBasicFund();

    String bookkeepingPayment(int uid, String bookKeepingName, String bookKeepingTypeName, int accountId, String amount, Timestamp time,
                              String fundId,String customedFundId,String comment,String enclosure);

    String bookkeepingIncome(int uid, String bookKeepingName, String bookKeepingTypeName, int accountId, String amount, Timestamp time,
                             String fundId,String customedFundId,String comment,String enclosure);
}
