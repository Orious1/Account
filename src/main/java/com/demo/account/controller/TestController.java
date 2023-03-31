package com.demo.account.controller;

import com.demo.account.entity.Income;
import com.demo.account.entity.Payment;
import com.demo.account.mapper.BookMapper;
import com.demo.account.mapper.IncomePaymentMapper;
import com.demo.account.mapper.UserMapper;
import com.demo.account.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    UserMapper userMapper;

    @Resource
    BookMapper bookMapper;

    @Resource
    IncomePaymentMapper incomePaymentMapper;

    @RequestMapping("/1")
    public String test(){
        //Date date = new Date();
        //Timestamp timeStamp = new Timestamp(date.getTime());
        //userMapper.addUser(2,"h","j",timeStamp);
        //int k=userMapper.uidGenerate();
        //System.out.println(k);
        //bookMapper.selectByUidAndName(1,"日常开销");
        String l=bookMapper.selectBookkeepingTypeList(1);
        String[] split=l.split("-");
        System.out.println(Arrays.toString(split));
        return l;
    }

    @RequestMapping("/2")
    public int test1(){
        String a="BO1-BO2-B03-BI1-BI2-BI3";
        String b=a.substring(a.indexOf("BI"));
        String c=a.substring(0,a.indexOf("BI")-1);
        System.out.println(b);
        System.out.println(c);
        return 1;
    }

    @RequestMapping("/3")
    public String test2(){
        //HashMap<String,Integer> map=new HashMap<>();
        //map.put("bookkeepingTypeId",-1);
        Integer i=bookMapper.findBookKeepingTypeIdInConditions(1,"我的账簿1","家庭账本");
        System.out.println(i);
        return "success";
    }
    @RequestMapping("/4")
    public  String test3(){
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        bookMapper.insertIntoPayment(1,1,1,"200",timeStamp,"B02","","无","无");
        return "success";
    }

    @RequestMapping("/5")
    public  String test4(){
        Timestamp timestamp= DateUtils.strToSqlDate("2023-1-1 13:25:30","yyyy-MM-dd HH:mm:ss");
        System.out.println(timestamp);
        return "success";
    }
    @RequestMapping("/6")
    public List<Income> test5(){
        return incomePaymentMapper.selectAllIncome(1);
    }
    @RequestMapping("/7")
    public List<Payment> test6(){
        return incomePaymentMapper.selectAllPayment(1);
    }
}
