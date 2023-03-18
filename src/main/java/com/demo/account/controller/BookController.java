package com.demo.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.BasicFund;
import com.demo.account.service.BookService;
import com.demo.account.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    private BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @param bookKeepingTypeName 账本对应的模板名称
     * @return  List<String> 表示支出款项类型的列表
     * 功能：在”记一笔”支出页面中可以根据账本的类型渲染出对应的款项类型
     */
    @RequestMapping(method = RequestMethod.GET,value = "/out")
    HashMap<String,String> getExpenditureType(int uid, String bookKeepingName, String bookKeepingTypeName){
        return bookService.getExpenditureType(uid,bookKeepingName,bookKeepingTypeName);
    }

    /**
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @param bookKeepingTypeName 账本对应的模板名称
     * @return List<String> 表示收入款项类型的列表
     *功能：在”记一笔”收入页面中可以根据账本的类型渲染出对应的款项类型
     */
    @RequestMapping(method = RequestMethod.GET,value = "/in")
    HashMap<String,String> getIncomeType(int uid,String bookKeepingName,String bookKeepingTypeName){
        return bookService.getIncomeType(uid,bookKeepingName,bookKeepingTypeName);
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @param bookKeepingTypeName 账本对应的模板名称
     * @param json 支出款项的json数据
     * @return "success"
     * 功能：修改账本模板的支出项目,用于”记一笔”支出的设置页面
     */
    @RequestMapping(method = RequestMethod.POST,value = "/setting_add_out")
    String outSettingChange(int uid, String bookKeepingName,String bookKeepingTypeName,@RequestBody JSONObject json){
        return bookService.outSettingChange(uid, bookKeepingName, bookKeepingTypeName, json);
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @param bookKeepingTypeName 账本对应的模板名称
     * @param json 收入款项的json数据
     * @return "success"
     * 功能：修改账本模板的收入项目,用于”记一笔”收入的设置页面
     */
    @RequestMapping(method = RequestMethod.POST,value = "/setting_add_in")
    String inSettingChange(int uid, String bookKeepingName,String bookKeepingTypeName,@RequestBody JSONObject json){
        return bookService.inSettingChange(uid, bookKeepingName, bookKeepingTypeName, json);
    }

    /**
     *
     * @return 基本款项表，用于名称和编号的对照
     */
    @RequestMapping(method = RequestMethod.GET,value = "/basic")
    List<BasicFund> getAll(){
        return bookService.selectAllBasicFund();
    }

    /**
     *
     * @return "success"
     * 功能：添加一笔支出
     */
    @RequestMapping(method = RequestMethod.POST,value = "/out")
    String bookkeepingPayment(int uid, String bookKeepingName, String bookKeepingTypeName, int accountId, String amount, String time,
                              String fundId,String customedFundId,String comment,String enclosure){
        Timestamp timestamp= DateUtils.strToSqlDate(time,"yyyy-MM-dd HH:mm:ss");
        return bookService.bookkeepingPayment(uid, bookKeepingName, bookKeepingTypeName, accountId, amount, timestamp, fundId, customedFundId, comment, enclosure);
    }

    /**
     *
     * @return "success"
     * 功能：添加一笔收入
     */
    @RequestMapping(method = RequestMethod.POST,value = "/in")
    String bookkeepingIncome(int uid, String bookKeepingName, String bookKeepingTypeName, int accountId, String amount, String time,
                              String fundId,String customedFundId,String comment,String enclosure){
        Timestamp timestamp= DateUtils.strToSqlDate(time,"yyyy-MM-dd HH:mm:ss");
        return bookService.bookkeepingIncome(uid, bookKeepingName, bookKeepingTypeName, accountId, amount, timestamp, fundId, customedFundId, comment, enclosure);
    }
}
