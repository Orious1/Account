package com.demo.account.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.*;
import com.demo.account.exception.BizException;
import com.demo.account.mapper.BookMapper;
import com.demo.account.mapper.BudgetMapper;
import com.demo.account.mapper.IncomePaymentMapper;
import com.demo.account.service.BookService;
import com.demo.account.utils.DateUtils;
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

    @Resource
    private IncomePaymentMapper incomePaymentMapper;

    @Resource
    private BudgetMapper budgetMapper;

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
    public String bookkeepingPayment(int uid, String bookKeepingName,int accountId, String amount, Timestamp time,
                                     String fundId, String customedFundId, String comment, String enclosure)
    {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        bookMapper.insertIntoPayment(uid, bookKeepingId, accountId, amount, time, fundId, customedFundId, comment, enclosure);
        return "success";
    }

    @Override
    public String bookkeepingIncome(int uid, String bookKeepingName, int accountId, String amount, Timestamp time, String fundId, String customedFundId, String comment, String enclosure) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        bookMapper.insertIntoIncome(uid, bookKeepingId, accountId, amount, time, fundId, customedFundId, comment, enclosure);
        return "success";
    }

    @Override
    public List<JSONObject> selectBookkeepingIncome(int uid, String bookKeepingName) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Income> ls=incomePaymentMapper.selectAllIncome(bookKeepingId);
        List<JSONObject> lsobject=new ArrayList<>();
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }
        for (Income i:ls){
            JSONObject object=new JSONObject();
            AccountDetail ac=incomePaymentMapper.selectAccountDetails(i.getAccountDetailId());
            object.put("时间",i.getTime());
            object.put("备注",i.getComment());
            object.put("金额",i.getAmount());
            String fundName=bfm.get(i.getFundId());
            if (fundName!=null){
                object.put("款项名称",fundName);
                object.put("款项类型","基本");
            }else {
                String customName=bookMapper.selectCustomedFundName(i.getCustomedFundId());
                object.put("款项名称",customName);
                object.put("款项类型","自定义");
            }
            object.put("账户名称",incomePaymentMapper.getAccountTypeName(ac.getAccountDetailType()));
            lsobject.add(object);
        }
        return lsobject;
    }

    @Override
    public List<JSONObject> selectBookkeepingPayment(int uid, String bookKeepingName) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Payment> ls=incomePaymentMapper.selectAllPayment(bookKeepingId);
        List<JSONObject> lsobject=new ArrayList<>();
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }
        for (Payment i:ls){
            JSONObject object=new JSONObject();
            AccountDetail ac=incomePaymentMapper.selectAccountDetails(i.getAccountDetailId());
            object.put("时间",i.getTime());
            object.put("备注",i.getComment());
            object.put("金额","-"+i.getAmount());
            String fundName=bfm.get(i.getFundId());
            if (fundName!=null){
                object.put("款项名称",fundName);
                object.put("款项类型","基本");
            }else {
                String customName=bookMapper.selectCustomedFundName(i.getCustomedFundId());
                object.put("款项名称",customName);
                object.put("款项类型","自定义");
            }
            object.put("账户名称",incomePaymentMapper.getAccountTypeName(ac.getAccountDetailType()));
            lsobject.add(object);
        }
        return lsobject;
    }

    @Override
    public String bookkeepingAdd(int uid, String bookKeepingName, String bookKeepingCover, Integer extraMember1, Integer extraMember2,
                                 String template,String bookKeepingTypeName) {
        List<String> bookKeepingNameList=bookMapper.selectUserBookkeeping(uid);
        if (bookKeepingNameList.contains(bookKeepingName)) throw new BizException("-1","账本名不能重复");
        int typeId=bookMapper.generalTypeId()+1;
        bookMapper.insertIntoBookKeeping(uid,bookKeepingName,bookKeepingCover,extraMember1,extraMember2,typeId);
        bookMapper.insertIntoBookkeepingType(typeId,bookKeepingTypeName,template);
        return "success";
    }

    @Override
    public String bookkeepingChange(int uid, String bookKeepingName,String bookKeepingNameNew, String bookKeepingCover, Integer extraMember1, Integer extraMember2) {
        List<String> bookKeepingNameList=bookMapper.selectUserBookkeeping(uid);
        if (!bookKeepingNameList.contains(bookKeepingName)) throw new BizException("-1","账本名不存在");
        bookKeepingNameList.remove(bookKeepingName);
        if (bookKeepingNameList.contains(bookKeepingNameNew)) throw new BizException("-1","新的账本名重名");
        bookMapper.changeBookKeeping(uid, bookKeepingName, bookKeepingNameNew,bookKeepingCover, extraMember1, extraMember2);
        return "success";
    }

    @Override
    public List<String> bookkeepingTypeNamesFind(int uid, String bookKeepingName) {
        List<BookKeeping> ls=bookMapper.selectByUidAndName(uid,bookKeepingName);
        List<String> typeNames=new ArrayList<>();
        for (BookKeeping i: ls){
            typeNames.add(bookMapper.selectBookkeepingTypeName(i.getBookkeeping_type_id()));
        }
        return typeNames;
    }

    //用于收入款项统计的私有方法用于 countWeekIncome countMonthIncome countYearIncome
    private void incomeFundMapCreate(String fundName,Income i,HashMap<String,Integer> custom,HashMap<String,Integer> funds){
        if (fundName!=null){ // 如果是基础款项
            if (!funds.containsKey(fundName))
                funds.put(fundName,Integer.parseInt(i.getAmount()));
            else
                funds.put(fundName,funds.get(fundName)+Integer.parseInt(i.getAmount()));
        }else { // 如果是自定义款项
            String customName=bookMapper.selectCustomedFundName(i.getCustomedFundId());
            if (!custom.containsKey(customName))
                custom.put(customName,Integer.parseInt(i.getAmount()));
            else
                custom.put(customName,custom.get(customName)+Integer.parseInt(i.getAmount()));
        }
    }

    private void  paymentFundMapCreate(String fundName,Payment i,HashMap<String,Integer> custom,HashMap<String,Integer> funds){
        if (fundName!=null){ // 如果是基础款项
            if (!funds.containsKey(fundName))
                funds.put(fundName,Integer.parseInt(i.getAmount()));
            else
                funds.put(fundName,funds.get(fundName)+Integer.parseInt(i.getAmount()));
        }else { // 如果是自定义款项
            String customName=bookMapper.selectCustomedFundName(i.getCustomedFundId());
            if (!custom.containsKey(customName))
                custom.put(customName,Integer.parseInt(i.getAmount()));
            else
                custom.put(customName,custom.get(customName)+Integer.parseInt(i.getAmount()));
        }
    }
    @Override
    public HashMap<String,HashMap<String, Integer>> countWeekIncome(int uid, String bookKeepingName, String nowTime) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Income> ls=incomePaymentMapper.selectAllIncome(bookKeepingId); // 获取对应账本的收入记录
        HashMap<String,Integer> hm= new HashMap<>();
        //初始化 hm
        String[] weeks={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        String format="yyyy-MM-dd HH:mm:ss";
        for (String i:weeks){
            hm.put(i,0);
        }

        // 获取当前日期所对应那一周的范围 例如今天是 2023年4月3日 那么一周对应的日期范围是 [2023-04-03 00:00:00 2023-04-09 23:59:59]
        List<String> startEndTimeList=DateUtils.getStartAndEndTime(DateUtils.strToSqlDate(nowTime,format));
        Timestamp startTime=DateUtils.strToSqlDate(startEndTimeList.get(0),format);
        Timestamp endTime=DateUtils.strToSqlDate(startEndTimeList.get(1),format);

        HashMap<String,Integer> funds=new HashMap<>(); // 记录基础款项的HashMap
        HashMap<String,Integer> custom=new HashMap<>(); // 记录自定义款项的HashMap
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }

        for (Income i:ls){
            String time= DateUtils.dateToStr(i.getTime(),format);

            // 判断这个时间是星期几
            String week=DateUtils.getWeek(time,format);

            // 判断支出记录的时间是否在这个区间内
            boolean whetherInThisMonth=DateUtils.isEffectiveDate(i.getTime(),startTime,endTime);

            // 如果记录中的时间是对应区间中的,就获取hp中对应星期的值并且更改
            if (whetherInThisMonth){
                Integer temp=hm.get(week)+Integer.parseInt(i.getAmount());
                hm.put(week,temp);
                /*-------------------------*/
                String fundName=bfm.get(i.getFundId()); //表示BI*所对应的款项名称
                incomeFundMapCreate(fundName,i,custom,funds);
            }
        }
        //System.out.println(hm);
        HashMap<String,HashMap<String, Integer>> result=new HashMap<>();
        result.put("周收入总和",hm);
        result.put("基础款项总和",funds);
        result.put("自定义款项总和",custom);
        System.out.println(result);
        return result;
    }

    @Override
    public HashMap<String,HashMap<String, Integer>>  countMonthIncome(int uid, String bookKeepingName, String startTime, String endTime) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Income> ls=incomePaymentMapper.selectAllIncome(bookKeepingId);
        HashMap<String,Integer> hm= new HashMap<>();
        String format="yyyy-MM-dd HH:mm:ss";
        int maxDayOfMonth=DateUtils.getDaysOfMonth(DateUtils.strToSqlDate(startTime,format));
        for (Integer i=1;i<=maxDayOfMonth;i++){
            hm.put(i.toString(),0);
        }

        HashMap<String,Integer> funds=new HashMap<>(); // 记录基础款项的HashMap
        HashMap<String,Integer> custom=new HashMap<>(); // 记录自定义款项的HashMap
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }

        for(Income i:ls){
            boolean whetherInThisMonth=DateUtils.isEffectiveDate(i.getTime(),DateUtils.strToSqlDate(startTime,format),DateUtils.strToSqlDate(endTime,format));
            if (whetherInThisMonth){
                String day=DateUtils.getMonthDay2Set(DateUtils.dateToStr(i.getTime(),format),"day").toString();
                Integer temp=hm.get(day)+Integer.parseInt(i.getAmount());
                hm.put(day,temp);
                /*-------------------------*/
                String fundName=bfm.get(i.getFundId()); //表示BI*所对应的款项名称
                incomeFundMapCreate(fundName,i,custom,funds);
            }
        }
        //System.out.println(hm);
        HashMap<String,HashMap<String, Integer>> result=new HashMap<>();
        result.put("当月每天收入",hm);
        result.put("基础款项总和",funds);
        result.put("自定义款项总和",custom);
        System.out.println(result);
        return result;
    }

    @Override
    public HashMap<String,HashMap<String, Integer>> countYearIncome(int uid, String bookKeepingName, String startTime, String endTime) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Income> ls=incomePaymentMapper.selectAllIncome(bookKeepingId);
        HashMap<String,Integer> hm= new HashMap<>();
        String format="yyyy-MM-dd HH:mm:ss";
        for (Integer i=1;i<=12;i++){
            hm.put(i.toString(),0);
        }

        HashMap<String,Integer> funds=new HashMap<>(); // 记录基础款项的HashMap
        HashMap<String,Integer> custom=new HashMap<>(); // 记录自定义款项的HashMap
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }

        for(Income i:ls){
            boolean whetherInThisYear=DateUtils.isEffectiveDate(i.getTime(),DateUtils.strToSqlDate(startTime,format),DateUtils.strToSqlDate(endTime,format));
            if (whetherInThisYear){
                String month=DateUtils.getMonthDay2Set(DateUtils.dateToStr(i.getTime(),format),"month").toString();
                Integer temp=hm.get(month)+Integer.parseInt(i.getAmount());
                hm.put(month,temp);
                /*-------------------------*/
                String fundName=bfm.get(i.getFundId()); //表示BI*所对应的款项名称
                incomeFundMapCreate(fundName,i,custom,funds);
            }
        }
        //System.out.println(hm);
        HashMap<String,HashMap<String, Integer>> result=new HashMap<>();
        result.put("当年每月收入",hm);
        result.put("基础款项总和",funds);
        result.put("自定义款项总和",custom);
        System.out.println(result);
        return result;
    }

    @Override
    public HashMap<String, HashMap<String, Integer>> countWeekPayment(int uid, String bookKeepingName, String nowTime) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Payment> ls=incomePaymentMapper.selectAllPayment(bookKeepingId); // 获取对应账本的收入记录
        HashMap<String,Integer> hm= new HashMap<>();
        //初始化 hm
        String[] weeks={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        String format="yyyy-MM-dd HH:mm:ss";
        for (String i:weeks){
            hm.put(i,0);
        }

        // 获取当前日期所对应那一周的范围 例如今天是 2023年4月3日 那么一周对应的日期范围是 [2023-04-03 00:00:00 2023-04-09 23:59:59]
        List<String> startEndTimeList=DateUtils.getStartAndEndTime(DateUtils.strToSqlDate(nowTime,format));
        Timestamp startTime=DateUtils.strToSqlDate(startEndTimeList.get(0),format);
        Timestamp endTime=DateUtils.strToSqlDate(startEndTimeList.get(1),format);

        HashMap<String,Integer> funds=new HashMap<>(); // 记录基础款项的HashMap
        HashMap<String,Integer> custom=new HashMap<>(); // 记录自定义款项的HashMap
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }

        for (Payment i:ls){
            String time= DateUtils.dateToStr(i.getTime(),format);

            // 判断这个时间是星期几
            String week=DateUtils.getWeek(time,format);

            // 判断支出记录的时间是否在这个区间内
            boolean whetherInThisMonth=DateUtils.isEffectiveDate(i.getTime(),startTime,endTime);

            // 如果记录中的时间是对应区间中的,就获取hp中对应星期的值并且更改
            if (whetherInThisMonth){
                Integer temp=hm.get(week)+Integer.parseInt(i.getAmount());
                hm.put(week,temp);
                /*-------------------------*/
                String fundName=bfm.get(i.getFundId()); //表示BO*所对应的款项名称
                paymentFundMapCreate(fundName,i,custom,funds);
            }
        }
        //System.out.println(hm);
        HashMap<String,HashMap<String, Integer>> result=new HashMap<>();
        result.put("周支出总和",hm);
        result.put("基础款项总和",funds);
        result.put("自定义款项总和",custom);
        System.out.println(result);
        return result;
    }

    @Override
    public HashMap<String, HashMap<String, Integer>> countMonthPayment(int uid, String bookKeepingName, String startTime, String endTime) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Payment> ls=incomePaymentMapper.selectAllPayment(bookKeepingId);
        HashMap<String,Integer> hm= new HashMap<>();
        String format="yyyy-MM-dd HH:mm:ss";
        int maxDayOfMonth=DateUtils.getDaysOfMonth(DateUtils.strToSqlDate(startTime,format));
        for (Integer i=1;i<=maxDayOfMonth;i++){
            hm.put(i.toString(),0);
        }

        HashMap<String,Integer> funds=new HashMap<>(); // 记录基础款项的HashMap
        HashMap<String,Integer> custom=new HashMap<>(); // 记录自定义款项的HashMap
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }

        for(Payment i:ls){
            boolean whetherInThisMonth=DateUtils.isEffectiveDate(i.getTime(),DateUtils.strToSqlDate(startTime,format),DateUtils.strToSqlDate(endTime,format));
            if (whetherInThisMonth){
                String day=DateUtils.getMonthDay2Set(DateUtils.dateToStr(i.getTime(),format),"day").toString();
                Integer temp=hm.get(day)+Integer.parseInt(i.getAmount());
                hm.put(day,temp);
                /*-------------------------*/
                String fundName=bfm.get(i.getFundId()); //表示BO*所对应的款项名称
                paymentFundMapCreate(fundName,i,custom,funds);
            }
        }
        //System.out.println(hm);
        HashMap<String,HashMap<String, Integer>> result=new HashMap<>();
        result.put("当月每天支出",hm);
        result.put("基础款项总和",funds);
        result.put("自定义款项总和",custom);
        System.out.println(result);
        return result;
    }

    @Override
    public HashMap<String, HashMap<String, Integer>> countYearPayment(int uid, String bookKeepingName, String startTime, String endTime) {
        //int bookKeepingTypeId=bookMapper.findBookKeepingTypeIdInConditions(uid,bookKeepingName,bookKeepingTypeName);
        //int bookKeepingId=bookMapper.findBookKeepingId(uid,bookKeepingName,bookKeepingTypeId);
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        List<Payment> ls=incomePaymentMapper.selectAllPayment(bookKeepingId);
        HashMap<String,Integer> hm= new HashMap<>();
        String format="yyyy-MM-dd HH:mm:ss";
        for (Integer i=1;i<=12;i++){
            hm.put(i.toString(),0);
        }

        HashMap<String,Integer> funds=new HashMap<>(); // 记录基础款项的HashMap
        HashMap<String,Integer> custom=new HashMap<>(); // 记录自定义款项的HashMap
        List<BasicFund> basicFunds=bookMapper.selectAllBasicFunds();
        HashMap<String,String> bfm=new HashMap<>(); // 记录基础款项的HashMap
        // 给bfm赋值
        for (BasicFund i:basicFunds){
            bfm.put(i.getFund_id(),i.getFund_name());
        }

        for(Payment i:ls){
            boolean whetherInThisYear=DateUtils.isEffectiveDate(i.getTime(),DateUtils.strToSqlDate(startTime,format),DateUtils.strToSqlDate(endTime,format));
            if (whetherInThisYear){
                String month=DateUtils.getMonthDay2Set(DateUtils.dateToStr(i.getTime(),format),"month").toString();
                Integer temp=hm.get(month)+Integer.parseInt(i.getAmount());
                hm.put(month,temp);
                /*-------------------------*/
                String fundName=bfm.get(i.getFundId()); //表示BI*所对应的款项名称
                paymentFundMapCreate(fundName,i,custom,funds);
            }
        }
        //System.out.println(hm);
        HashMap<String,HashMap<String, Integer>> result=new HashMap<>();
        result.put("当年每月支出",hm);
        result.put("基础款项总和",funds);
        result.put("自定义款项总和",custom);
        System.out.println(result);
        return result;
    }

    @Override
    public List<String> selectUserBookkeeping(int uid) {
        return bookMapper.selectUserBookkeeping(uid);
    }

    private boolean monthInputCheck(String month){
        String[] months={"jan","feb","mar","apr","may","jun","jul","aug","sept","oct","nov","dec"};
        for (String i: months){
            if (Objects.equals(i, month)){
                return true;
            }
        }
        return false;
    }
    @Override
    public JSONObject getBookkeepingBudget(int uid, String bookKeepingName,String month) {
        int bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        boolean validOrNot;
        validOrNot=monthInputCheck(month);
        JSONObject object=new JSONObject();
        if (validOrNot){
            Budget budget=budgetMapper.selectBookkeepingBudget(bookKeepingId);
            JSONObject budgetMonth=(JSONObject) JSONObject.toJSON(budget);
            object.put("该月预算为",budgetMonth.get(month));
        }else {
            throw new BizException("-1","月份的输入不正确应为jan/feb/mar/apr/may/jun/jul/aug/sept/oct/nov/dec其中之一");
        }
        return object;
    }

    @Override
    public String changeBookkeepingBudget(int uid, String bookKeepingName, String month, String budget) {
        int bookKeepingId;
        try{
            bookKeepingId=bookMapper.selectBookkeepingId(uid,bookKeepingName);
        }catch (Exception e){
            throw new BizException("-1","没找到该账簿");
        }
        boolean validOrNot;
        validOrNot=monthInputCheck(month);
        if (validOrNot){
            budgetMapper.updateBudget(month,budget,bookKeepingId);
        }else {
            throw new BizException("-1","月份的输入不正确应为jan/feb/mar/apr/may/jun/jul/aug/sept/oct/nov/dec其中之一");
        }
        return "success";
    }

    @Override
    public List<JSONObject> getPartRecordOfIncomeAndPayment(int uid, String bookKeepingName, String classification,List<JSONObject> allPayment,List<JSONObject> allIncome) {
        String[] ys={"餐饮","蔬菜","水果","零食"}; // 饮食大类
        String[] yl={"通讯","社交","旅行","购物","娱乐"}; // 娱乐大类
        String[] jt={"快递","交通"}; // 交通大类
        String[] sh={"服饰","家庭","住房","日用","运动"}; // 生活大类
        String[] gz={"工资","年终奖","收款"}; // 工资大类
        String[] lc={"分红","理财"}; // 理财大类
        String[] rq={"借入","礼金","红包"}; // 人情大类
        String[] other={"生活费","其它","租金"}; // 其它大类
        String[] whichClassification;
        switch (classification){
            case "饮食": whichClassification=ys;break;
            case "娱乐": whichClassification=yl;break;
            case "交通": whichClassification=jt;break;
            case "生活": whichClassification=sh;break;
            case "工资": whichClassification=gz;break;
            case "理财": whichClassification=lc;break;
            case "人情": whichClassification=rq;break;
            case "其它": whichClassification=other;break;
            default: throw new BizException("-1","类别输入错误可选输入有{饮食/娱乐/交通/生活/工资/理财/人情/其它}");
        }
        List<JSONObject> result=new ArrayList<>();
        if (classification.equals("其它")){
            for (JSONObject i:allPayment){
                if (i.get("款项类型").toString().equals("自定义")){
                    result.add(i);
                }
            }
            for (JSONObject i:allIncome){
                if (i.get("款项类型").toString().equals("自定义")){
                    result.add(i);
                }
            }
        }
        for (JSONObject i: allPayment){
            for (String j:whichClassification){
                if (i.get("款项名称").toString().equals(j)){
                    result.add(i);
                }
            }
        }
        for (JSONObject i:allIncome){
            for (String j:whichClassification){
                if (i.get("款项名称").toString().equals(j)){
                    result.add(i);
                }
            }
        }
        return result;
    }

    @Override
    public String deleteBookkeeping(int uid, String bookKeepingName) {
        try{
            bookMapper.selectBookkeepingId(uid,bookKeepingName);
        }catch (Exception e){
            throw new BizException("-1","没找到该账簿");
        }
        bookMapper.deleteBookkeeping(uid, bookKeepingName);
        return "success";
    }

    @Override
    public String deleteCustomFund(String customFundId) {
        List<String> list=bookMapper.selectAllCustomFundId();
        if (!list.contains(customFundId)) throw new BizException("-1","自定义款项不存在");
        bookMapper.deleteCustomFund(customFundId);
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
