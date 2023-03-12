package com.demo.account.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface BookService {

    List<String> getExpenditureType(int uid,String bookKeepingName,String bookKeepingTypeName);

    List<String> getIncomeType(int uid,String bookKeepingName,String bookKeepingTypeName);

    String outSettingAdd(int uid, String bookKeepingName,String bookKeepingTypeName,JSONObject json);

    String inSettingAdd(int uid, String bookKeepingName,String bookKeepingTypeName,JSONObject json);
}
