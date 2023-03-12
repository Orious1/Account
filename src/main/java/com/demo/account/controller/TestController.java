package com.demo.account.controller;

import com.demo.account.entity.BookKeeping;
import com.demo.account.mapper.BookMapper;
import com.demo.account.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    UserMapper userMapper;

    @Resource
    BookMapper bookMapper;
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
        return bookMapper.insertCFund("CO",1,"你好",1);
    }
}
