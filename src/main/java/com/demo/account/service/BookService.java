package com.demo.account.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.BasicFund;
import com.demo.account.entity.Income;
import com.demo.account.entity.Payment;

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

    String bookkeepingAdd(int uid,String bookKeepingName,String bookKeepingCover,String bookkeepingPeriod,Timestamp bookkeepingCreateDate,
                          Timestamp bookkeepingEndDate,Integer extraMember1,Integer extraMember2,String template,String bookKeepingTypeName);

    String bookkeepingChange(int uid,String bookKeepingName,String bookKeepingCover,String bookkeepingPeriod,Timestamp bookkeepingCreateDate,
                             Timestamp bookkeepingEndDate,Integer extraMember1,Integer extraMember2);

    List<String> bookkeepingTypeNamesFind(int uid ,String bookKeepingName);

    List<JSONObject> selectBookkeepingIncome(int uid,String bookKeepingName, String bookKeepingTypeName);

    List<JSONObject> selectBookkeepingPayment(int uid, String bookKeepingName, String bookKeepingTypeName);
}
