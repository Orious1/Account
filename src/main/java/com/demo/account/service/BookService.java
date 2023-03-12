package com.demo.account.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.BasicFund;

import java.util.List;

public interface BookService {

    List<String> getExpenditureType(int uid,String bookKeepingName,String bookKeepingTypeName);

    List<String> getIncomeType(int uid,String bookKeepingName,String bookKeepingTypeName);

    String outSettingChange(int uid, String bookKeepingName,String bookKeepingTypeName,JSONObject json);

    String inSettingChange(int uid, String bookKeepingName,String bookKeepingTypeName,JSONObject json);

    List<BasicFund> selectAllBasicFund();
}
