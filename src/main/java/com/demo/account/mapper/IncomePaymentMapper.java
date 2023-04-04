package com.demo.account.mapper;

import com.demo.account.entity.AccountDetail;
import com.demo.account.entity.Income;
import com.demo.account.entity.Payment;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IncomePaymentMapper {

    @Results(id="incomeMapper" ,value={
            @Result(id = true,column = "income_id",property = "incomeId"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "bookkeeping_id",property = "bookkeepingId"),
            @Result(column = "account_detail_id",property = "accountDetailId"),
            @Result(column = "amount",property = "amount"),
            @Result(column = "time",property = "time"),
            @Result(column = "fund_id",property = "fundId"),
            @Result(column = "customed_fund_id",property = "customedFundId"),
            @Result(column = "comment",property = "comment"),
            @Result(column = "enclosure",property = "enclosure")
    })
    @Select("select *from income where bookkeeping_id=#{bookkeepingId}")
    List<Income> selectAllIncome(int bookkeepingId);

    @Results(id="paymentMapper" ,value={
            @Result(id = true,column = "payment_id",property = "paymentId"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "bookkeeping_id",property = "bookkeepingId"),
            @Result(column = "account_detail_id",property = "accountDetailId"),
            @Result(column = "amount",property = "amount"),
            @Result(column = "time",property = "time"),
            @Result(column = "fund_id",property = "fundId"),
            @Result(column = "customed_fund_id",property = "customedFundId"),
            @Result(column = "comment",property = "comment"),
            @Result(column = "enclosure",property = "enclosure")
    })
    @Select("select *from payment where bookkeeping_id=#{bookkeepingId}")
    List<Payment> selectAllPayment(int bookkeepingId);

    @Results(id="accountMapper" ,value={
            @Result(id = true,column = "account_detail_id",property = "accountDetailId"),
            @Result(column = "account_detail_type_id",property = "accountDetailType"),
            @Result(column = "account_id",property = "accountId"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "balance",property = "balance"),
            @Result(column = "budget",property = "budget"),
            @Result(column = "comment",property = "comment")
    })
    @Select("SELECT *FROM account_details WHERE account_detail_id=#{accountDetailId}")
    AccountDetail selectAccountDetails(Integer accountDetailId);

    @Select("select account_detail_type_name from account_details_type where account_detail_type_id = #{accountDetailTypeId}")
    String getAccountTypeName(Integer accountDetailTypeId);
}
