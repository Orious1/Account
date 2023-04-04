package com.demo.account.mapper;

import com.demo.account.entity.Budget;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.omg.PortableInterceptor.INACTIVE;

public interface BudgetMapper {

    @Results(id="budgetMapper" ,value={
            @Result(id = true,column = "budget_id",property = "budgetId"),
            @Result(column = "bookkeeping_id",property = "bookkeepingId"),
            @Result(column = "jan",property = "jan"),
            @Result(column = "feb",property = "feb"),
            @Result(column = "mar",property = "mar"),
            @Result(column = "apr",property = "apr"),
            @Result(column = "may",property = "may"),
            @Result(column = "jun",property = "jun"),
            @Result(column = "jul",property = "jul"),
            @Result(column = "aug",property = "aug"),
            @Result(column = "sept",property = "sept"),
            @Result(column = "oct",property = "oct"),
            @Result(column = "nov",property = "nov"),
            @Result(column = "dec",property = "dec")
    })
    @Select("SELECT *FROM budget WHERE bookkeeping_id=#{bookkeepingId}")
    Budget selectBookkeepingBudget(int bookkeepingId);

    @Update("UPDATE budget SET ${month}=#{budget} WHERE bookkeeping_id=#{bookkeepingId}")
    int updateBudget(String month,String budget,int bookkeepingId);
}
