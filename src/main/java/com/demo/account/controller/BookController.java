package com.demo.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.account.entity.BasicFund;
import com.demo.account.exception.BizException;
import com.demo.account.exception.ResultBody;
import com.demo.account.service.BookService;
import com.demo.account.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     * @return  List<String> 表示支出款项类型的列表
     * 功能：在”记一笔”支出页面中可以根据账本的类型渲染出对应的款项类型
     */
    @RequestMapping(method = RequestMethod.GET,value = "/out")
    ResultBody getExpenditureType(int uid, String bookKeepingName){
        List<String> list=bookService.bookkeepingTypeNamesFind(uid,bookKeepingName);
        if (list.size()==0) throw new BizException("-1","账本不存在");
        String bookKeepingTypeName=list.get(0);
        HashMap<String,String> map=bookService.getExpenditureType(uid,bookKeepingName,bookKeepingTypeName);
        return ResultBody.success(map);
    }

    /**
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @return List<String> 表示收入款项类型的列表
     *功能：在”记一笔”收入页面中可以根据账本的类型渲染出对应的款项类型
     */
    @RequestMapping(method = RequestMethod.GET,value = "/in")
    ResultBody getIncomeType(int uid,String bookKeepingName){
        List<String> list=bookService.bookkeepingTypeNamesFind(uid,bookKeepingName);
        if (list.size()==0) throw new BizException("-1","账本不存在");
        String bookKeepingTypeName=list.get(0);
        HashMap<String,String> map=bookService.getIncomeType(uid,bookKeepingName,bookKeepingTypeName);
        return ResultBody.success(map);
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @param json 支出款项的json数据
     * @return "success"
     * 功能：修改账本模板的支出项目,用于”记一笔”支出的设置页面
     */
    @RequestMapping(method = RequestMethod.POST,value = "/setting_add_out")
    ResultBody outSettingChange(int uid, String bookKeepingName,@RequestBody JSONObject json){
        List<String> list=bookService.bookkeepingTypeNamesFind(uid,bookKeepingName);
        if (list.size()==0) throw new BizException("-1","账本不存在");
        String bookKeepingTypeName=list.get(0);
        bookService.outSettingChange(uid, bookKeepingName, bookKeepingTypeName, json);
        return ResultBody.success("设置成功");
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账本名
     * @param json 收入款项的json数据
     * @return "success"
     * 功能：修改账本模板的收入项目,用于”记一笔”收入的设置页面
     */
    @RequestMapping(method = RequestMethod.POST,value = "/setting_add_in")
    ResultBody inSettingChange(int uid, String bookKeepingName,@RequestBody JSONObject json){
        List<String> list=bookService.bookkeepingTypeNamesFind(uid,bookKeepingName);
        if (list.size()==0) throw new BizException("-1","账本不存在");
        String bookKeepingTypeName=list.get(0);
        bookService.inSettingChange(uid, bookKeepingName, bookKeepingTypeName, json);
        return ResultBody.success("设置成功");
    }

    /**
     *
     * @return 基本款项表，用于名称和编号的对照
     */
    @RequestMapping(method = RequestMethod.GET,value = "/basic")
    ResultBody getAll(){
        List<BasicFund> list=bookService.selectAllBasicFund();
        return ResultBody.success(list);
    }

    /**
     *
     * @return "success"
     * 功能：添加一笔支出
     */
    @RequestMapping(method = RequestMethod.POST,value = "/out")
    ResultBody bookkeepingPayment(int uid, String bookKeepingName, int accountId, String amount, String time,
                              String fundId,String customedFundId,String comment,String enclosure){
        Timestamp timestamp= DateUtils.strToSqlDate(time,"yyyy-MM-dd HH:mm:ss");
        bookService.bookkeepingPayment(uid, bookKeepingName, accountId, amount, timestamp, fundId, customedFundId, comment, enclosure);
        return ResultBody.success("添加成功");
    }

    /**
     *
     * @return "success"
     * 功能：添加一笔收入
     */
    @RequestMapping(method = RequestMethod.POST,value = "/in")
    ResultBody bookkeepingIncome(int uid, String bookKeepingName, int accountId, String amount, String time,
                              String fundId,String customedFundId,String comment,String enclosure){
        Timestamp timestamp= DateUtils.strToSqlDate(time,"yyyy-MM-dd HH:mm:ss");
        bookService.bookkeepingIncome(uid, bookKeepingName, accountId, amount, timestamp, fundId, customedFundId, comment, enclosure);
        return ResultBody.success("添加成功");
    }

    /**
     * @return "success"
     *功能：添加一个账本
     */
    @RequestMapping(method = RequestMethod.POST,value = "/new")
    ResultBody bookkeepingAdd(int uid,String bookKeepingName,String bookKeepingCover,Integer extraMember1,Integer extraMember2,
                          String template,String bookKeepingTypeName){
        bookService.bookkeepingAdd(uid, bookKeepingName, bookKeepingCover, extraMember1, extraMember2,template,bookKeepingTypeName);
        return ResultBody.success("添加成功");
    }

    /**
     * @return "success"
     *功能：修改账本设置
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/change")
    ResultBody bookkeepingChange(int uid,String bookKeepingName,String bookKeepingNameNew,String bookKeepingCover,Integer extraMember1,Integer extraMember2){
        bookService.bookkeepingChange(uid,bookKeepingName,bookKeepingNameNew,bookKeepingCover,extraMember1,extraMember2);
        return ResultBody.success("修改成功");
    }

    /**
     * @return List<String>
     *功能：获取账本对应模板的名字
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getTypeNames")
    ResultBody bookkeepingTypeNamesFind(int uid,String bookKeepingName){
        List<String> list=bookService.bookkeepingTypeNamesFind(uid,bookKeepingName);
        return  ResultBody.success(list);
    }

    /**
     *
     * @param uid
     * @return 账本名称的列表
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getBookNames")
    ResultBody selectUserBookkeeping(int uid){
        return ResultBody.success(bookService.selectUserBookkeeping(uid));
    }

    /**
     *
     * @return List<Income'sJSONObject>
     * 功能：获取对于账本的收入明细
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getIncome")
    ResultBody selectBookkeepingIncome(int uid, String bookKeepingName){
        return ResultBody.success(bookService.selectBookkeepingIncome(uid, bookKeepingName));
    }

    /**
     *
     * @return List<Payment'sJSONObject>
     * 功能：获取对于账本的支出明细
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getPayment")
    ResultBody selectBookkeepingPayment(int uid, String bookKeepingName){
        return ResultBody.success(bookService.selectBookkeepingPayment(uid, bookKeepingName));
    }

    /**
     *
     * @param nowTime 用于查询一周账单,提供查询时的时间,在按月/年查询时可为空
     * @param startTime 用于查询一个月/年的账单,提供该月/年开始时间,在按周查询时可为空
     * @param endTime 用于查询一个月/年的账单,提供该月/年结束时间,在按周查询时可为空
     * @param getWhat 用于区别你查询的是周/月/年 提供的输入为{week,month,year}
     * @return HashMap<String,Integer> or HashMap<Integer,Integer>
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getPeriodIncome")
    ResultBody getPeriodIncome(int uid, String bookKeepingName,
                               String nowTime,String startTime,String endTime,String getWhat){
        try{
            switch (getWhat){
                case "week":
                    return ResultBody.success(bookService.countWeekIncome(uid, bookKeepingName, nowTime));
                case "month":
                    return ResultBody.success(bookService.countMonthIncome(uid, bookKeepingName, startTime, endTime));
                case "year":
                    return ResultBody.success(bookService.countYearIncome(uid, bookKeepingName, startTime, endTime));
                default:
                    return ResultBody.error("getWhat参数错误 提供的输入应为week/month/year}");
            }
        }catch (Exception e){
          throw new BizException("-1","日期格式不对,应为yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     *
     * @param nowTime 用于查询一周账单,提供查询时的时间,在按月/年查询时可为空
     * @param startTime 用于查询一个月/年的账单,提供该月/年开始时间,在按周查询时可为空
     * @param endTime 用于查询一个月/年的账单,提供该月/年结束时间,在按周查询时可为空
     * @param getWhat 用于区别你查询的是周/月/年 提供的输入为{week,month,year}
     * @return HashMap<String,Integer> or HashMap<Integer,Integer>
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getPeriodPayment")
    ResultBody getPeriodPayment(int uid, String bookKeepingName,
                               String nowTime,String startTime,String endTime,String getWhat){
        try{
            switch (getWhat){
                case "week":
                    return ResultBody.success(bookService.countWeekPayment(uid, bookKeepingName, nowTime));
                case "month":
                    return ResultBody.success(bookService.countMonthPayment(uid, bookKeepingName, startTime, endTime));
                case "year":
                    return ResultBody.success(bookService.countYearPayment(uid, bookKeepingName, startTime, endTime));
                default:
                    return ResultBody.error("getWhat参数错误 提供的输入应为week/month/year}");
            }
        }catch (Exception e){
            throw new BizException("-1","日期格式不对,应为yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账簿名称
     * @param month 月份
     * @return 功能是获取账簿指定月份的预算
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getBudget")
    ResultBody getBookkeepingBudget(int uid, String bookKeepingName,String month){
        return ResultBody.success(bookService.getBookkeepingBudget(uid, bookKeepingName,month));
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账簿名称
     * @param month 月份
     * @param budget 预算
     * @return 功能是改变账簿某月的预算
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/changeBudget")
    ResultBody changeBudget(int uid,String bookKeepingName,String month,String budget){
        bookService.changeBookkeepingBudget(uid, bookKeepingName, month, budget);
        return ResultBody.success("更新成功");
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账簿名称
     * @param classification 类别
     * @return 功能是根据几个大类查询,支出收入记录
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getPartRecord")
    ResultBody getPartRecordOfIncomeAndPayment(int uid,String bookKeepingName,String classification){
        List<JSONObject> allPayment=bookService.selectBookkeepingPayment(uid,bookKeepingName);
        List<JSONObject> allIncome=bookService.selectBookkeepingIncome(uid, bookKeepingName);
        return ResultBody.success(bookService.getPartRecordOfIncomeAndPayment(uid,bookKeepingName,classification,allPayment,allIncome));
    }

    /**
     *
     * @param uid 用户id
     * @param bookKeepingName 账簿名称
     * @return 功能是删除指定账簿
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/deleteBookkeeping")
    ResultBody deleteBookkeeping(int uid,String bookKeepingName){
        bookService.deleteBookkeeping(uid, bookKeepingName);
        return ResultBody.success("删除成功");
    }

    /**
     *
     * @param customFundId 自定义款项ID
     * @return 功能是删除自定义款项
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/deleteCustomFund")
    ResultBody deleteCustomFund(String customFundId){
        bookService.deleteCustomFund(customFundId);
        return ResultBody.success("删除成功");
    }
}
