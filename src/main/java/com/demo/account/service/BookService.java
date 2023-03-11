package com.demo.account.service;

import java.util.List;

public interface BookService {

    List<String> getExpenditureType(int uid,String bookKeepingName,String bookKeepingTypeName);

    List<String> getIncomeType(int uid,String bookKeepingName,String bookKeepingTypeName);

}
