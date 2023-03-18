package com.demo.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.BasicFund;
import com.demo.account.entity.BookKeeping;
import com.demo.account.entity.CustomFund;
import com.demo.account.mapper.BookMapper;
import com.demo.account.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;

    @Override
    public List<BasicFund> selectAllBasicFund() {
        return bookMapper.selectAllBasicFunds();
    }

    @Override
    public HashMap<String,String> getExpenditureType(int uid, String bookKeepingName,String bookKeepingTypeName) {
        HashMap<String,String> l=getPaymentType(uid,bookKeepingName,"O",bookKeepingTypeName);
        return l;
    }

    @Override
    public HashMap<String,String> getIncomeType(int uid, String bookKeepingName,String bookKeepingTypeName) {
        HashMap<String,String> l=getPaymentType(uid,bookKeepingName,"I",bookKeepingTypeName);
        return l;
    }


    /*遇到的问题：如果要提供三个模板的话那么那张bookkeeping_type的前三项就无法进行更改，因为所有用户都要用到这三条记录
                 如果这时候要对模板进行修改的话就产生了问题。
                 1.如果不存在模板的话，只需要更该对应模板的bookkeeping_type_funds_id项的值，
                 如果有用户自定义的款项就再在customed_fund表添加记录就可以完成。
                 此时对于新建一个模板也只需要在bookkeeping_type表中添加一条记录，然后按照上述规则更新customed_fund表即可。
                 添加的时候还要保证一个用户的一个账本在bookkeeping_type表中的类型名称字段(bookkeeping_type_name)要互不相同,不同用户间可以存在相同。
                 2.如果存在模板的话，由于前三项不可更改，那么对于默认模板的更改操作可以认为是新建模板操作，这样子就需要用户把支出的项目、收入的项目全部更改完毕，
                 然后命名完成(命名规则同上)，在bookkeeping_type表中添加一条记录即可。对于用户自定义的一些款项同样在customed_fund表添加记录就可以完成。
                 但这种更改模式不符合常识，所以如果需要做成第一种情况的前端效果的话就需要，增加一张表，那一张表就拿来存系统的模板不变动，用户对模板的更改
                 完成后只需要在另一张表中存入一条记录即可，接下来对模板的修改操作都会反映在这一张表上
               * */
    @Override
    public String outSettingChange(int uid, String bookKeepingName, String bookKeepingTypeName, JSONObject json) {
        List<String> l=(List<String>)json.get("json");
        StringBuilder basicFunds= new StringBuilder(); // 用于记录更新后的款项字符序列
        List<BookKeeping> bookKeeping=bookMapper.selectByUidAndName(uid,bookKeepingName);
        for (BookKeeping i:bookKeeping){
            int k=i.getBookkeeping_type_id();
            String temp=bookMapper.selectBookkeepingTypeName(k);
            if (Objects.equals(temp, bookKeepingTypeName)) {
                for (String j : l) {
                    if (j.contains("BO")) {
                        basicFunds.append(j).append("-");
                    } else {
                        //把自定义的款项添加到customed_funds表中
                        //自定义的款项是“CO****”格式
                        bookMapper.insertCFund("CO",uid,j.substring(2),k);
                    }
                }
                String ls=bookMapper.selectBookkeepingTypeList(i.getBookkeeping_type_id());
                String boLs=ls.substring(ls.indexOf("BI"));
                basicFunds.append(boLs);
                bookMapper.updateBookKeepingTypeList(String.valueOf(basicFunds),k);
            }
        }
        return "success";
    }

    @Override
    public String inSettingChange(int uid, String bookKeepingName, String bookKeepingTypeName, JSONObject json) {
        List<String> l=(List<String>)json.get("json");
        StringBuilder basicFunds= new StringBuilder(); // 用于记录更新后的款项字符序列
        List<BookKeeping> bookKeeping=bookMapper.selectByUidAndName(uid,bookKeepingName);
        for (BookKeeping i:bookKeeping){
            int k=i.getBookkeeping_type_id();
            String temp=bookMapper.selectBookkeepingTypeName(k);
            if (Objects.equals(temp, bookKeepingTypeName)) {
                for (String j : l) {
                    if (j.contains("BI")) {
                        basicFunds.append("-").append(j);
                    } else {
                        //把自定义的款项添加到customed_funds表中
                        //自定义的款项是“CI****”格式 ***是款项名称
                        bookMapper.insertCFund("CI",uid,j.substring(2),k);
                    }
                }
                String ls=bookMapper.selectBookkeepingTypeList(i.getBookkeeping_type_id());
                String biLs=ls.substring(0,ls.indexOf("BI")-1);
                bookMapper.updateBookKeepingTypeList(biLs+basicFunds,k);
            }
        }
        return "success";
    }

    @Override
    public String bookkeepingPayment(int uid, String bookKeepingName, String bookKeepingTypeName,int accountId, String amount, Timestamp time,
                                     String fundId, String customedFundId, String comment, String enclosure)
    {
        int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        bookMapper.insertIntoPayment(uid, bookKeepingId, accountId, amount, time, fundId, customedFundId, comment, enclosure);
        return "success";
    }

    @Override
    public String bookkeepingIncome(int uid, String bookKeepingName, String bookKeepingTypeName, int accountId, String amount, Timestamp time, String fundId, String customedFundId, String comment, String enclosure) {
        int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        bookMapper.insertIntoIncome(uid, bookKeepingId, accountId, amount, time, fundId, customedFundId, comment, enclosure);
        return "success";
    }

    private HashMap<String,String> getPaymentType(int uid, String bookKeepingName, String type, String bookKeepingTypeName){
        List<BookKeeping> bookKeeping=bookMapper.selectByUidAndName(uid,bookKeepingName);
        HashMap<String,String> l=new HashMap<>();
        for (BookKeeping i: bookKeeping){
            String temp=bookMapper.selectBookkeepingTypeName(i.getBookkeeping_type_id());
            // 找到对应的模板类型的处理
            if (Objects.equals(temp, bookKeepingTypeName)){
                String ls=bookMapper.selectBookkeepingTypeList(i.getBookkeeping_type_id());
                String[] split=ls.split("-");
                for (String j: split){
                    //通过type 判断是支出还是收入 O为支出 I为收入
                    if (j.contains(type)){
                        l.put(j,bookMapper.selectBasicFundName(j));
                    }
                }
                // 从用户自定义款项中找是否存在，存在加入，不存在不做处理
                List<CustomFund> lcf=bookMapper.selectCustomFund(uid,i.getBookkeeping_type_id());
                if (lcf!=null && !lcf.isEmpty()){
                    for (CustomFund j:lcf){
                        if (j.getCustomed_fund_id().contains(type)){
                            l.put(j.getCustomed_fund_id(),j.getCustomed_fund_name());
                        }
                    }
                }
            }
        }
        return l;
    }
}
